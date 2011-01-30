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
#include "tPLMutex.h"
#include <pthread.h>
#include <stdlib.h>

/** Create the mutex. */
int PLCreateMutex(tPLMutex* mutex)
{
	if(!mutex)
		return -1;
	else
	{
		pthread_mutex_t *pmutex = (pthread_mutex_t*)malloc(sizeof(pthread_mutex_t));
		if(!pmutex)
		{
			/* not enough memory. */
			return -1;
		}
		pthread_mutex_init(pmutex,NULL);
		*mutex = pmutex;
		return 0; /* success. */
	}
}
/** Lock the mutex. */
int PLLockMutex(tPLMutex mutex)
{
	if(!mutex)
		return -1;
	if( pthread_mutex_lock( (pthread_mutex_t*)mutex) != 0 )
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
	if( pthread_mutex_unlock( (pthread_mutex_t*)mutex) != 0 )
	{
		/* unlocking failed. */
		return -1;
	}
	return 0;
	
}
/** Destroy the mutex. */
int PLDestroyMutex(tPLMutex* mutex)
{
	if( !mutex || !(*mutex) )
		return -1;
	if( pthread_mutex_destroy( (pthread_mutex_t*)*mutex ) != 0 )
	{
		/* destroy failed. */
		return -1;
	}
	free(*mutex);
	*mutex = 0;
	return 0;

}
