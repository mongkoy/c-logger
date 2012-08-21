
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
 * \file A log server using TCP implementations by 
 * Reihmon P. Estremos koydaut@gmail.com
 * */
#include <unistd.h>
#include <errno.h>
#include <stdlib.h>
#include <stdio.h>       /* Basic I/O routines          */
#include <sys/types.h>   /* standard system types       */
#include <netinet/in.h>  /* Internet address structures */
#include <sys/socket.h>  /* socket interface functions  */
#include <netdb.h>       /* host to IP resolution       */
#include <string.h>
#include <arpa/inet.h>
#include <time.h>


#include <logserver_temp.h>
#define LOGSERVER_LOG_FILENAME_FORMAT "%Y-%m-%d_%H.%M.%S.log"

void print_usage() {
        puts("logserver PORT_NUMBER MAX_MESSAGE_SIZE");
        puts("PORT_NUMBER --> port number ");
        puts("MAX_MESSAGE_SIZE --> maximum message size");
        puts("sample usage: iplogserv 444444 1024");

}

static FILE *open_logfile()
{
        FILE *logfile;
        char log_filename[24];
        time_t cur_time;
        struct tm *time_s;
        size_t nRet;

        logfile = NULL;

        cur_time = time(NULL);
        time_s = localtime(&cur_time);
        nRet = strftime(log_filename, sizeof (log_filename),
                        LOGSERVER_LOG_FILENAME_FORMAT, time_s);
        if(nRet > 0) {
                logfile = fopen(log_filename, "wb");
        }

        return logfile;
}

static void close_logfile(FILE *logfile)
{
        fclose(logfile);
}

static void print_data(FILE *dest, void *data, size_t data_size) {
        uint8_t *buffer;

        buffer = (uint8_t *) data;

        while (data_size--) {
                if(*buffer > 0) {
                        fputc(*buffer, dest);
                }
                buffer++;
        }
}

int create_socket(uint16_t port) {
        int ip_socket;
        struct sockaddr_in sockaddr;

        /* Create the socket. */
        ip_socket = socket(AF_INET, SOCK_STREAM, 0);
        if (ip_socket < 0) {
                perror("socket: allocation failed");
        }

        sockaddr.sin_family = AF_INET;
        sockaddr.sin_port = htons(port);
        sockaddr.sin_addr.s_addr = htonl(INADDR_ANY);
        if (bind(ip_socket, (struct sockaddr *) &sockaddr, sizeof(sockaddr)) < 0) {
                perror("bind");
                exit(EXIT_FAILURE);
        }
        return ip_socket;
}

void *mem_zalloc(size_t size) {
        void *heap_ptr;
        heap_ptr = malloc(size + 1);
        memset((void *) heap_ptr, 0, size);
        return heap_ptr;
}

void free_zalloc(void * heap_ptr) {
        free(heap_ptr);
}

int read_client(int fd, struct iplogserver_t *logbuff_server) {
        char *buffer;
        int numbytes;
        int buff_size;

        buffer = logbuff_server->message_buffer;
        buff_size = logbuff_server->message_buffsize;

        while((numbytes = read(fd, buffer, buff_size)) > 0){
            if(numbytes < 0){
                fprintf(stderr, "ERROR reading from socket");
                return -1;
            }
        }

        if (numbytes < 0) {
                /* Read error. */
                perror("read");
                exit(EXIT_FAILURE);
        } else if (numbytes == 0)
                /* End-of-file. */
                return -1;
        else {
                /* Data read. */
                /*fwrite(buffer, sizeof(char), numbytes, stdout);*/
            print_data(stdout, buffer, numbytes);
            /*print_data(logfile,buffer,numbytes); */
                return 0;
        }
}

int main(int argc, char *argv[]) {

        int fd_socket;
        fd_set active_fd, read_fd;
        struct sockaddr_in csa;
        int i = 0;
        uint16_t port;
        int dsize; /* size of file descriptors table     */
        struct iplogserver_t logbuff = { 0, "" };

        /* Check params */
        if (3 == argc) {
                logbuff.message_buffsize = atoi(argv[2]);
                logbuff.message_buffer = (char *) mem_zalloc(logbuff.message_buffsize);
                port = atoi(argv[1]);
                if (NULL == logbuff.message_buffer) {
                        fprintf(stdout, "Can't allocate %d buffer size\n",
                                        logbuff.message_buffsize);
                        return EXIT_FAILURE;
                }
        } else {
                print_usage();
                return EXIT_FAILURE;
        }

        /*create a socket to listen to*/

        fd_socket = create_socket(port);
        /* Initialise file descriptors sockets. */

        if (listen(fd_socket, 5) < 0) {
                perror("listen");
                exit(EXIT_FAILURE);
        }
        /* remember size for later usage */

        /* calculate size of file descriptors table */
        dsize = getdtablesize();

        FD_ZERO(&active_fd);
        FD_SET(fd_socket, &active_fd);

        while (1) {
                /*blocking socket block until input arrives*/
                read_fd = active_fd;
                if (select(dsize, &read_fd, NULL, NULL, (struct timeval *) NULL) < 0) {
                        perror("select");
                        exit(EXIT_FAILURE);
                }
                if (FD_ISSET(fd_socket,&read_fd)) {
                        printf("fd socket is set \n");
                        int size_csa = sizeof(csa);
                        int connectd_socket = accept(fd_socket, (struct sockaddr *) &csa,
                                        (socklen_t *) &size_csa);
                        /*are we connected*/
                        if (connectd_socket < 0) {
                                perror("socket accept error");
                                continue;
                        }
                        fprintf(stdout, "Server: connected from host %s, port %d.\n",
                                        (char *) inet_ntoa(csa.sin_addr),
                                        ntohs(csa.sin_port));
                        FD_SET(connectd_socket, &active_fd);
                }
                for (i = 0; i < dsize; ++i) {

                        if ((i != fd_socket && FD_ISSET(i,&read_fd))) {
                                if (read_client(i, &logbuff) < 0) {
                                        close(i);
                                        FD_CLR(i, &active_fd);
                                }
                        }

                }

        }
        free_zalloc(&logbuff);
        return 0;
}