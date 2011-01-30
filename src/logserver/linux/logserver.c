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
 * \file A simple log server using TCP
 * */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>

static void print_data(void* data, size_t data_size);
static void print_usage();

int main(int argc, char *argv[]) {
	int sockfd;
	int newsockfd;
	int buffer_size;
	uint16_t portno;
	socklen_t clilen;
	char *buffer;
	struct sockaddr_in serv_addr, cli_addr;
	int n;
	if (argc != 3) {
		print_usage();
		goto EXIT_FUNC;
	}
	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockfd < 0) {
		fprintf(stderr, "ERROR opening socket");
		goto EXIT_FUNC;
	}
	bzero((char *) &serv_addr, sizeof (serv_addr));
	portno = atoi(argv[1]);
	buffer_size = atoi(argv[2]);
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = INADDR_ANY;
	serv_addr.sin_port = htons(portno);
	if (bind(sockfd, (struct sockaddr *) &serv_addr,
		sizeof (serv_addr)) < 0) {
		fprintf(stderr, "ERROR on binding");
		goto EXIT_FUNC;
	}
	listen(sockfd, 1);
	clilen = sizeof (cli_addr);
	buffer = (char *) malloc(buffer_size);
	if (NULL == buffer) {
		fprintf(stderr, "ERROR on malloc");
		goto EXIT_FUNC;
	}

	while (1) {
		newsockfd = accept(sockfd,
			(struct sockaddr *) &cli_addr,
			&clilen);
		if (newsockfd < 0) {
			fprintf(stderr, "ERROR on accept");
			goto EXIT_SOCKFD;
		}
		while ((n = read(newsockfd, buffer, buffer_size)) > 0) {
			if (n < 0) {
				fprintf(stderr, "ERROR reading from socket");
				goto EXIT_NEWSOCKFD;
			}
			print_data(buffer, n);
		}
EXIT_NEWSOCKFD:
		close(newsockfd);
	}
EXIT_SOCKFD:
	close(sockfd);
	free(buffer);
EXIT_FUNC:
	return 0;
}

static void print_data(void* data, size_t data_size) {
	uint8_t *buffer;

	buffer = (uint8_t *) data;

	while (data_size--) {
		fputc(*buffer, stdout);
		buffer++;
	}
}

static void print_usage() {
	puts("logserver PORT BUF_MAX_SIZE");
	puts("Example: logserver 50007 1024");
}
