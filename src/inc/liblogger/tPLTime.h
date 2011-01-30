/*
 * c-logger - copyright 2011, Jefty E. Negapatan, jeftyneg@gmail.com
 * This file is part of c-logger.
 *
 * c-logger is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * c-logger is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * \file Platform Layer for time functions.
 * */
#ifndef TPLTIME_H
#define	TPLTIME_H

#ifdef	__cplusplus
extern "C" {
#endif

#include <stdint.h>

/** Function to retrieve the current time in milliseconds to be used as
 * timestamp value.
 * \returns Time in milliseconds
 * */
uint64_t PLGetCurTimeInMillis();

#ifdef	__cplusplus
}
#endif

#endif	/* #ifndef TPLTIME_H */
