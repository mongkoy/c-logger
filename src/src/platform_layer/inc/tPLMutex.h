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
#ifndef __T_PLMUTEX_H__
#define __T_PLMUTEX_H__

#if defined(WIN32) || (_WIN32)
/* Windows */
#include <windows.h>
/** The mutex handle. */
typedef HANDLE tPLMutex;
#elif defined(__unix) || defined(__linux)
#include <pthread.h>
/* A Unix system */
/** The mutex handle. */
typedef pthread_mutex_t* tPLMutex;
#else
/* Unsupported platform. */
#endif

/** Create the mutex. */
int PLCreateMutex(tPLMutex* mutex);
/** Lock the mutex. */
int PLLockMutex(tPLMutex mutex);
/** Release the mutex. */
int PLUnLockMutex(tPLMutex mutex);
/** Destroy the mutex. */
int PLDestroyMutex(tPLMutex* mutex);

#endif /* #ifndef __T_PLMUTEX_H__ */
