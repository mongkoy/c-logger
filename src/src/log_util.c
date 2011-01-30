/*
 * c-logger - copyright 2011, Jefty E. Negapatan, jeftyneg@gmail.com
 * This file is part of c-logger.
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

#include "log_util.h"

static const char *f_astrLogPrefix[] =
		{
			"TRACE", /**< The trace log level */
			"DEBUG", /**< The debug log level */
			"INFO", /**< The info log level */
			"WARN", /**< The warn log level */
			"ERROR", /**< The error log level */
			"FATAL" /**< The fatal log level */
		};

/* helper function to get the log prefix */
const char* sGetLogPrefix(const LogLevel logLevel)
{
	return f_astrLogPrefix[logLevel];
}
