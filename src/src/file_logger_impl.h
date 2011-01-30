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
#ifndef __FILE_LOGGER_IMPL_H__
#define __FILE_LOGGER_IMPL_H__

#include <liblogger/liblogger.h>
#include <liblogger/logger_object.h>
#include <liblogger/file_logger.h>

/** Factory Function to create the File Logger. 
 * \param [out] logWriter 	The log writer handle.
 * \param [in]	initParams	The log writer initialization parameters.
 * \returns 0 on success , -1 on failure.
 * */
int InitFileLogger(LogWriter** logWriter,tFileLoggerInitParams* initParams );

/** Factory Function to create the Console Logger. 
 * A console logger is a special case of file logging (where the file is stdout or stderr)
 * \param [out] logWriter 	The log writer handle.
 * \param [in]	initParams	The log writer initialization parameters.
 * \returns 0 on success , -1 on failure.
 * */
int InitConsoleLogger(LogWriter** logWriter,void* initParams);

#endif // __FILE_LOGGER_IMPL_H__
