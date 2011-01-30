/*
 * c-logger - copyright 2011, Jefty E. Negapatan, jeftyneg@gmail.com
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

#ifndef LOG_UTIL_H
#define	LOG_UTIL_H

#ifdef	__cplusplus
extern "C" {
#endif

#include "liblogger/liblogger_levels.h"

const char* sGetLogPrefix(const LogLevel logLevel);

#ifdef	__cplusplus
}
#endif

#endif	/* LOG_UTIL_H */
