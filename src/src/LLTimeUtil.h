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
#ifndef __LLTIMEUTIL_H__
#define __LLTIMEUTIL_H__

/**
 * Returns the current date time as a string.
 * \param [out] str String where the date time is returned.
 * \param [in]	strLen The length of the arg \a str.
 * \returns 0 on success, -1 on failure.
 * */
int LLGetCurDateTime(char* str, int strLen);

#endif // __LLTIMEUTIL_H__
