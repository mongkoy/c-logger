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
/**
 * \file Implementation of the Mutex Abstraction API for Win32 platform.
 * */
#include "tPLMutex.h"
#include <windows.h>
#include <stdio.h>

/** Create the mutex. */
int PLCreateMutex(tPLMutex* mutex)
{
	if(!mutex)
		return -1;
	else
	{
		HANDLE _mutex = CreateMutex( 
				NULL,              /* default security attributes */
				FALSE,             /* initially not owned */
				NULL);             /* unnamed mutex */
		if ( !_mutex)
		{
			fprintf(stderr,"Mutex creation failed.");
			return -1;
		}
		*mutex = _mutex;
		return 0; /* success. */
	}
}
/** Lock the mutex. */
int PLLockMutex(tPLMutex mutex)
{
	if(!mutex)
		return -1;
	if( WaitForSingleObject(mutex,INFINITE) != WAIT_OBJECT_0 )
	{
		/* locking failed. */
		return -1;
	}
	return 0;
	
}
/** Unlock the mutex. */
int PLUnLockMutex(tPLMutex mutex)
{
	if(!mutex)
		return -1;
	if( ReleaseMutex (mutex) == 0  )
	{
		/*
		 * If the function succeeds, the return value is nonzero.
		 * unlocking failed.
		 */
		return -1;
	}
	return 0;
}

/** Destroy the mutex. */
int PLDestroyMutex(tPLMutex* mutex)
{
	if( !mutex || !(*mutex) )
		return -1;
	if( CloseHandle( (HANDLE)*mutex ) != 0 )
	{
		/* destroy failed. */
		return -1;
	}
	*mutex = 0;
	return 0;

}
