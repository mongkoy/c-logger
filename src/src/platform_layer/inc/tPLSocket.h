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
 * \file Platform Layer for socket functions.
 * */
#ifndef __PLSOCKET_H__
#define __PLSOCKET_H__

#if defined(WIN32) || (_WIN32)
/* Windows */
#include <winsock2.h>
/** Abstract handle for socket. */
typedef SOCKET tPLSocket;
#elif defined(__unix) || defined(__linux)
/* A Unix system */
/** Abstract handle for socket. */
typedef int tPLSocket;
#else
/* Unsupported platform. */
#endif

/** Function to create a \b connected socket.
 * \param [in]  server 	The log server.
 * \param [in]  port	The log server port.
 * \param [out] sock	The socket handle.
 * \returns -1 on failure and \a sock will be -1 , or else 0 on success and sock will be non zero.
 * */
int PLCreateConnectedSocket(const char* server, int port, tPLSocket *sock);

/** Send data over socket.
 * \param [in] tPLSocket 	The socket handle created via \ref CreateConnectedSocket.
 * \param [in] data			The data to send. 
 * \param [in] dataSize		The size of data.
 * \return On success, the amount of bytes sent, -1 otherwise.
 * */
int PLSockSend(tPLSocket sock,const void* data,const int dataSize);

/** Close the Socket.
 * \param [in,out] sock	The socket handle created via \ref CreateConnectedSocket.
 * */
void PLDestroySocket(tPLSocket * sock);

#endif /* #ifndef __PLSOCKET_H__ */
