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
#include "socket_logger_impl.h"
#include "tPLSocket.h"
#include "log_util.h"
#include "win32_support.h"

/** The maximum size of the log. */
#define BUF_MAX 128

/** Helper function to print the logs to a buffer and send to a socket or
 * other consumer */
static int sSendToSock(LogWriter *_this,const LogLevel logLevel,
#ifdef VARIADIC_MACROS
		const uint64_t curTimeInMillis,
		const char* moduleName,
		const char* file,const char* funcName, const int lineNum, 
#endif
		const char* fmt,va_list ap);

int sSockFuncLogEntry(LogWriter *_this,const char* funcName);

int sSockFuncLogExit(LogWriter* _this,const char* funcName,const int lineNumber);

int sSockLoggerDeInit(LogWriter* _this);

typedef struct SockLogWriter
{
	LogWriter	base;
	tPLSocket	sock;
}SockLogWriter;

static SockLogWriter sSockLogWriter = 
{
	{
		/* .base.log 		= */sSendToSock, 
		/* .base.logFuncEntry 	= */sSockFuncLogEntry,
		/* .base.logFuncExit	= */sSockFuncLogExit,
		/* .base.loggerDeInit 	= */sSockLoggerDeInit,	
	},
	/* .sock  = */0
};


int InitSocketLogger(LogWriter** logWriter,tSockLoggerInitParams *initParams)
{
	if(!logWriter || !initParams || !initParams->server)
	{
		fprintf(stderr,"Invalid args to function InitSocketLogger\n");
		return -1;
	}
	*logWriter = 0;

	if (sSockLogWriter.sock)
	{
		sSockLoggerDeInit((LogWriter*)&sSockLogWriter);
	}
	if( -1 == PLCreateConnectedSocket(initParams->server, initParams->port, &sSockLogWriter.sock) )
	{
		fprintf(stderr,"could not connect to log server %s:%d",initParams->server,initParams->port);
		return -1;
	}
	*logWriter = (LogWriter*)&sSockLogWriter;
	return 0; /* success! */
}

/** Helper function to print the logs to a buffer and send to a socket or
 * other consumer */
static int sSendToSock(LogWriter *_this,const LogLevel logLevel,
#ifdef VARIADIC_MACROS
		const uint64_t curTimeInMillis,
		const char* moduleName,
		const char* file,const char* funcName, const int lineNum, 
#endif
		const char* fmt,va_list ap)
{
	SockLogWriter *slw = (SockLogWriter*) _this;
	if(!_this || (-1 == slw->sock))
	{
		fprintf(stderr,"invalid args for sSendToSock");
		return -1;
	}
	else
	{
		char buf[BUF_MAX];
		int bytes = 0;
#ifdef VARIADIC_MACROS
		bytes = snprintf(buf,BUF_MAX-1,"\n[%s][%llu][%s:%s:%s:%d]",
				sGetLogPrefix(logLevel),curTimeInMillis,
				moduleName,file,funcName,lineNum);
#else
		bytes = snprintf(buf,BUF_MAX-1,"\n[%s]",sGetLogPrefix(logLevel));
#endif
		/* to be on safer side, check if required size is available. */
		if(bytes < (BUF_MAX -1) )
			bytes += vsnprintf(buf+bytes,BUF_MAX-1-bytes,fmt,ap);
		buf[BUF_MAX-1] = 0;
		if((-1 == bytes ) || (bytes>BUF_MAX-1))
		{
			fprintf(stderr,"WARNING : socket log truncated, increase BUF_MAX\n");
			bytes = BUF_MAX-1;
		}
		return PLSockSend(slw->sock,buf,bytes);
	}
}

int sSockFuncLogEntry(LogWriter *_this,const char* funcName)
{

	SockLogWriter *slw = (SockLogWriter*) _this;
	if(!_this || (-1 == slw->sock))
	{
		fprintf(stderr,"invalid args for sSockFuncLogEntry");
		return -1;
	}
	else
	{
		char buf[BUF_MAX];
		int bytes;
#ifdef WIN32
		bytes = _snprintf(buf,BUF_MAX-1,"\n{ %s ", funcName);
#else
		bytes = snprintf(buf,BUF_MAX-1,"\n{ %s", funcName);
#endif
		buf[BUF_MAX-1] = 0;
		if((-1 == bytes ) || (bytes>BUF_MAX-1))
			bytes = BUF_MAX-1;
		return PLSockSend(slw->sock,buf,bytes);
	}
	
}

int sSockFuncLogExit(LogWriter* _this,const char* funcName,const int lineNumber)
{

	SockLogWriter *slw = (SockLogWriter*) _this;
	if(!_this || (-1 == slw->sock))
	{
		fprintf(stderr,"invalid args for sSockFuncLogExit");
		return -1;
	}
	else
	{
		char buf[BUF_MAX];
		int bytes;
#ifdef WIN32
		bytes = _snprintf(buf,BUF_MAX-1,"\n%s : %d }", funcName,lineNumber);
#else
		bytes = snprintf(buf,BUF_MAX-1,"\n%s : %d }", funcName,lineNumber);
#endif
		buf[BUF_MAX-1] = 0;
		if((-1 == bytes ) || (bytes>BUF_MAX-1))
			bytes = BUF_MAX-1;
		return PLSockSend(slw->sock,buf,bytes);
	}
}

int sSockLoggerDeInit(LogWriter* _this)
{
	SockLogWriter *slw = (SockLogWriter*) _this;
	if(slw)
	{
		PLDestroySocket(&slw->sock);
	}
	return 0;
}
