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

#include "liblogger/tPLTime.h"

#include <stdlib.h>
#include <sys/time.h>

/** Function to retrieve the current time in milliseconds to be used as
 * timestamp value.
 * \returns Time in milliseconds
 * */
uint64_t PLGetCurTimeInMillis() {
	uint64_t nCurTimeInMillis;
	struct timeval stTimeValue;
	int iRet;

	nCurTimeInMillis = 0;

	iRet = gettimeofday(&stTimeValue, NULL);

	if (0 == iRet) {
		nCurTimeInMillis = (stTimeValue.tv_sec * 1000) +
			(stTimeValue.tv_usec / 1000);
	}

	return nCurTimeInMillis;
}
