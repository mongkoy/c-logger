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
#include <liblogger/liblogger.h>
#include "file_logger_impl.h"
#include "socket_logger_impl.h"

#ifndef DISABLE_THREAD_SAFETY
	#include "tPLMutex.h"
	#define __LOCK_MUTEX 	if(sMutex) PLLockMutex(sMutex)
	#define __UNLOCK_MUTEX	if(sMutex) PLUnLockMutex(sMutex)
#else
	#define __LOCK_MUTEX 	/* NOP */
	#define __UNLOCK_MUTEX	/* NOP */
#endif /* #ifndef DISABLE_THREAD_SAFETY */

#include <stdio.h>
#include <stdarg.h>

#ifndef DISABLE_ALL_LOGS 

/** The log writer func ptr is initialized depending on the log destination. */
static LogWriter *pLogWriter = 0;
#ifndef DISABLE_THREAD_SAFETY
static tPLMutex	sMutex = 0;
#endif


/** Macro to check if logger subsystem is initialize, 
 * if not, then it is initialized to log to file
 * */
#define CHECK_AND_INIT_LOGGER	if(!pLogWriter)	\
	{ 											\
		fprintf(stderr,"\n[liblogger]liblogger not initialized, logging will be done to console (stdout)\n");\
		if(InitLogger(LogToConsole,stdout)) 			\
			return -1; 							\
		if(!pLogWriter)							\
			return -1;							\
	}											\


/** Function to initialize the logger. */
int InitLogger(LogDest ldest,void* loggerInitParams)
{
	int retVal = 0;
	if(pLogWriter)
	{
		fprintf(stderr,"\n [liblogger]Deinitializing the current log writer\n");
		DeInitLogger();
	}
#ifndef DISABLE_THREAD_SAFETY
	if(!sMutex)
		PLCreateMutex(&sMutex);
#endif
	__LOCK_MUTEX;

	switch(ldest)
	{
		/* log to a socket. */
		case LogToSocket:
			#ifndef DISABLE_SOCKET_LOGGER
			{
				if( -1 == InitSocketLogger(&pLogWriter,loggerInitParams) )
				{
					fprintf(stderr,"\n [liblogger] could not init socket logging \n");
					retVal = -1;
					goto UNLOCK_RETURN;
				}
			}
			#else
			{
				fprintf(stderr,"\n [liblogger] Socket logger not enabled during build\n");
				retVal = -1;
				goto UNLOCK_RETURN;
			}
			#endif
			break;

		/* log to a console. */
		case LogToConsole:
			{
				if( -1 == InitConsoleLogger(&pLogWriter,loggerInitParams) )
				{
					/* control should never reach here, this should always succeed. */
					fprintf(stderr,"\n [liblogger] could not initialize console logger \n");
					retVal = -1;
					goto UNLOCK_RETURN;
				}
			}
		break;

		/* log to a file. */
		case LogToFile:
			{
				if( -1 == InitFileLogger(&pLogWriter,loggerInitParams) )
				{
					fprintf(stderr,"\n [liblogger] could not initialize file logger, check file path/name \n");
					retVal = -1;
					goto UNLOCK_RETURN;
				}
			}
			break;
	}
	retVal = 0;
UNLOCK_RETURN:
	__UNLOCK_MUTEX;
	return retVal; /* success. */
}

/** Deinitialize the logger, the file / socket is closed here. */
void DeInitLogger()
{
	__LOCK_MUTEX;
	pLogWriter->loggerDeInit(pLogWriter);
	pLogWriter = 0;
	__UNLOCK_MUTEX;

#ifndef DISABLE_THREAD_SAFETY
	PLDestroyMutex(&sMutex);
	sMutex = 0;
#endif
}

int vsLogStub(LogLevel logLevel,
#ifdef VARIADIC_MACROS
		const uint64_t curTimeInMillis,
		const char* moduleName,const char* file,
		const char* funcName, const int lineNum,
#endif
	const char* fmt,va_list ap)
{
	int retVal = 0;
	CHECK_AND_INIT_LOGGER;

	__LOCK_MUTEX;

	retVal = pLogWriter->log(pLogWriter,logLevel,
#ifdef VARIADIC_MACROS
			curTimeInMillis,moduleName,file,funcName,lineNum,
#endif
			fmt,ap);

	__UNLOCK_MUTEX;

	return retVal;
}

#ifdef VARIADIC_MACROS
int LogStub_vm(LogLevel logLevel,
		const uint64_t curTimeInMillis,
		const char* moduleName,const char* file,
		const char* funcName, const int lineNum,
		const char* fmt,...)
{

	va_list ap; 
	int retVal = 0;
	va_start(ap,fmt);
	retVal = vsLogStub(logLevel,curTimeInMillis,moduleName,file,funcName,lineNum,fmt,ap);
	va_end(ap);
	return retVal;
}

#else

int LogTrace(const char* fmt,...)
{
	int retVal = 0;
	va_list ap; 
	va_start(ap,fmt);
	retVal = vsLogStub(Trace,fmt,ap);
	va_end(ap);
	return retVal;
}

int LogDebug(const char* fmt,...)
{
	int retVal = 0;
	va_list ap; 
	va_start(ap,fmt);
	retVal = vsLogStub(Debug,fmt,ap);
	va_end(ap);
	return retVal;
}

int LogInfo(const char* fmt,...)
{
	int retVal = 0;
	va_list ap; 
	va_start(ap,fmt);
	retVal = vsLogStub(Info,fmt,ap);
	va_end(ap);
	return retVal;
}

int LogWarn(const char* fmt,...)
{
	int retVal = 0;
	va_list ap; 
	va_start(ap,fmt);
	retVal = vsLogStub(Warn,fmt,ap);
	va_end(ap);
	return retVal;
}

int LogError(const char* fmt,...)
{
	int retVal = 0;
	va_list ap; 
	va_start(ap,fmt);
	retVal = vsLogStub(Error,fmt,ap);
	va_end(ap);
	return retVal;
}

int LogFatal (const char* fmt,...)
{
	int retVal = 0;
	va_list ap; 
	va_start(ap,fmt);
	retVal = vsLogStub(Fatal,fmt,ap);
	va_end(ap);
	return retVal;
}

#endif /* #ifdef VARIADIC_MACROS */

#endif /* #ifndef DISABLE_ALL_LOGS */
