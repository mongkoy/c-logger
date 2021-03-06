/*
 * liblogger - copyright 2007, Vineeth Neelakant, nvineeth@gmail.com
 * This file is part of liblogger.
 *
 * liblogger is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * liblogger is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
#include "file_logger_impl.h"
#include "log_util.h"
#include <stdio.h>
#include <memory.h>

/** Default log file name, if InitLogger() is not done and a 
  logger function is directly called. */
#define FILE_NAME_LOG "NoNameLogFile.txt"
#define MAX_PATH 255

/* win32 support */
#ifdef _WIN32
	#define  vsnprintf(buf,buf_size,fmt,ap) _vsnprintf(buf,buf_size,fmt,ap);
	#define	inline __inline
#endif

/** Helper function to write the logs to file */
static int sWriteToFile(LogWriter *_this,
		const LogLevel logLevel ,
#ifdef VARIADIC_MACROS
		const uint64_t curTimeInMillis,
		const char* moduleName,
		const char* file,const char* funcName, const int lineNum, 
#endif
		const char* fmt,va_list ap);

/** File Logger object function deinitialization function */
static int sFileLoggerDeInit(LogWriter* _this);

/** The File logger object. */
typedef struct FileLogWriter
{
	/** Base logger object. */
	LogWriter	 base;
#ifdef _ENABLE_LL_ROLLBACK_
	/** The rollback size, if \ref tFileOpenMode::RollbackMode is specified.
	 * if rollback is not aplicable, then it will be 0*/
	unsigned long rollbackSize;
#endif /* #ifdef _ENABLE_LL_ROLLBACK_ */
	/** The log file pointer. */
	FILE		*fp;
}FileLogWriter;

#ifdef _ENABLE_LL_ROLLBACK_
/** function to check and rollback the file, once the indicated size is reached. */
static void __CHECK_AND_ROLLBACK(FileLogWriter* flw);
#endif /* #ifdef _ENABLE_LL_ROLLBACK_ */

static FileLogWriter sFileLogWriter = 
{
	{
		/*.base.log		= */sWriteToFile,
		/*.base.loggerDeInit	= */sFileLoggerDeInit,
	},
#ifdef _ENABLE_LL_ROLLBACK_
	/*.rollbackSize		= */ 0,
#endif /* #ifdef _ENABLE_LL_ROLLBACK_ */
		/* .fp					= */ 0
};

/* Function to initialize the console logger, a console logger is a special case of file logger, 
 * where the file is stdout / stderr
 * */
int InitConsoleLogger(LogWriter** logWriter,void* dest)
{
	if(!logWriter)
	{
		fprintf(stderr,"Invalid args to function InitFileLogger\n");
		return -1;
	}
	*logWriter = 0;

	if (sFileLogWriter.fp)
	{
		sFileLoggerDeInit((LogWriter*)&sFileLogWriter);
	}
	if ( (dest != stdout) && (dest != stderr) )
	{
		fprintf(stderr,"Incorrect init params for console logger, stdout will be used.\n");
		dest = stdout;
	}
#ifdef _ENABLE_LL_ROLLBACK_
	sFileLogWriter.rollbackSize = 0;
#endif /* #ifdef _ENABLE_LL_ROLLBACK_ */
	sFileLogWriter.fp = dest;
	*logWriter = (LogWriter*)&sFileLogWriter;
	return 0; /* success! */
}

/* Function to initialize the file logger.
 * */
int InitFileLogger(LogWriter** logWriter,tFileLoggerInitParams* initParams)
{
	/* default file open mode is write... */
	char* fileOpenMode = "w";
	if(!logWriter || !initParams)
	{
		fprintf(stderr,"Invalid args to function InitFileLogger\n");
		return -1;
	}
	*logWriter = 0;
	if(!initParams->fileName)
	{
		fprintf(stderr,"filename is null, error \n");
		return -1;
	}

	/* deinitialize the file logger if already initialized. */
	if (sFileLogWriter.fp)
		sFileLoggerDeInit((LogWriter*)&sFileLogWriter);


	/* check if append mode or rollback mode is specified and open the file accrodingly*/
	switch(initParams->fileOpenMode)
	{
		/* Opening a file with append mode (a as the first character in the mode argument) 
		 * shall cause all subsequent writes to the file to be forced to the then 
		 * current end-of-file, regardless of intervening calls to fseek().
		 * */
		case AppendMode: 	fileOpenMode = "a"; break;
#ifdef _ENABLE_LL_ROLLBACK_
		case RollbackMode:	fileOpenMode = "w+"; break;
#endif /* #ifdef _ENABLE_LL_ROLLBACK_ */
		default:			fileOpenMode = "w"; break;
	}

	sFileLogWriter.fp = fopen(initParams->fileName,fileOpenMode);
	if( !sFileLogWriter.fp )
	{
		fprintf(stderr,"could not open log file %s",initParams->fileName);
		return -1;
	}
	else
	{
#ifdef _ENABLE_LL_ROLLBACK_
		/* if the file open is successful, and rollback mode is specified, note down the
		 * rollback size. 
		 * */
		if(RollbackMode == initParams->fileOpenMode)
		{
			sFileLogWriter.rollbackSize = initParams->rollbackSize;
			fseek(sFileLogWriter.fp,0L,SEEK_END);
			__CHECK_AND_ROLLBACK(&sFileLogWriter);
		}
		else
			sFileLogWriter.rollbackSize = 0;
#endif /* #ifdef _ENABLE_LL_ROLLBACK_ */
	}

	/* Log the current date time when the log is started. */
	*logWriter = (LogWriter*)&sFileLogWriter;
	return 0; /* success! */
}

/** Helper function to write the logs to file */
static int sWriteToFile(LogWriter *_this,
		const LogLevel logLevel,
#ifdef VARIADIC_MACROS
		const uint64_t curTimeInMillis,
		const char* moduleName,
		const char* file,const char* funcName, const int lineNum, 
#endif
		const char* fmt,va_list ap)
{
	FileLogWriter *flw = (FileLogWriter*) _this;
	if(!_this || !flw->fp)
	{
		fprintf(stderr,"Invalid args to sWriteToFile.");
		return -1;
	}
	else
	{
		fprintf(flw->fp, "[%s]", sGetLogPrefix(logLevel));
#ifdef VARIADIC_MACROS
		fprintf(flw->fp,"[%llu][%s:%s:%s:%d]",curTimeInMillis,
			moduleName,file,funcName,lineNum);
#endif
		vfprintf(flw->fp,fmt,ap); 
		fprintf(flw->fp,"\n");
		fflush(flw->fp);
#ifdef _ENABLE_LL_ROLLBACK_
		__CHECK_AND_ROLLBACK(flw);
#endif /* #ifdef _ENABLE_LL_ROLLBACK_ */
		return 0;
	}
}

/** File Logger object function deinitialization function */
int sFileLoggerDeInit(LogWriter* _this)
{
	FileLogWriter *flw = (FileLogWriter*) _this;
	if(flw && flw->fp)
	{
		if( (flw->fp != stdout) && (flw->fp != stderr) )
			fclose(flw->fp);
	}
	flw->fp = 0;
#ifdef _ENABLE_LL_ROLLBACK_
	flw->rollbackSize = 0;
#endif /* #ifdef _ENABLE_LL_ROLLBACK_ */
	return 0;
}

#ifdef _ENABLE_LL_ROLLBACK_
/** function to check and rollback the file, once the indicated size is reached. */
static void __CHECK_AND_ROLLBACK(FileLogWriter* flw)
{
	if(flw->rollbackSize != 0)
	{					
		long _curOffset = ftell(flw->fp);					
		if((_curOffset != -1) && (_curOffset >= flw->rollbackSize))	 
		{	
			fprintf(flw->fp,"\n --- Rolling back log --- \n"); 
			if( -1 == fseek(flw->fp,0L,SEEK_SET) ) fprintf(stderr,"[liblogger]fseek failed \n");
			rewind(flw->fp);
			fprintf(flw->fp,"ftell %d\n", ftell(flw->fp) );
			fprintf(flw->fp,"\n --- log Rolled Back... --- \n"); 
		}
	} 
}
#endif /* #ifdef _ENABLE_LL_ROLLBACK_ */
