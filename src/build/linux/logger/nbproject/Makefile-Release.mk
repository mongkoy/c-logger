#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc
CCC=g++
CXX=g++
FC=gfortran
AS=as

# Macros
CND_PLATFORM=GNU-Linux-x86
CND_DLIB_EXT=so
CND_CONF=Release
CND_DISTDIR=dist
CND_BUILDDIR=build

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=${CND_BUILDDIR}/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/_ext/1386528437/socket_logger.o \
	${OBJECTDIR}/_ext/1967181203/tPLMutex.o \
	${OBJECTDIR}/_ext/1967181203/tPLSocket.o \
	${OBJECTDIR}/_ext/1386528437/liblogger.o \
	${OBJECTDIR}/_ext/1967181203/tPLTime.o \
	${OBJECTDIR}/_ext/1386528437/log_util.o \
	${OBJECTDIR}/_ext/1386528437/file_logger.o


# C Compiler Flags
CFLAGS=-Wextra

# CC Compiler Flags
CCFLAGS=
CXXFLAGS=

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk ${CND_DISTDIR}/${CND_CONF}/liblogger.so

${CND_DISTDIR}/${CND_CONF}/liblogger.so: ${OBJECTFILES}
	${MKDIR} -p ${CND_DISTDIR}/${CND_CONF}
	${LINK.c} -shared -o ${CND_DISTDIR}/${CND_CONF}/liblogger.so -s -fPIC ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/_ext/1386528437/socket_logger.o: ../../../src/socket_logger.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/1386528437
	${RM} $@.d
	$(COMPILE.c) -O2 -Wall -s -I../../../inc -I../../../src/platform_layer/inc -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/1386528437/socket_logger.o ../../../src/socket_logger.c

${OBJECTDIR}/_ext/1967181203/tPLMutex.o: ../../../src/platform_layer/posix/tPLMutex.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/1967181203
	${RM} $@.d
	$(COMPILE.c) -O2 -Wall -s -I../../../inc -I../../../src/platform_layer/inc -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/1967181203/tPLMutex.o ../../../src/platform_layer/posix/tPLMutex.c

${OBJECTDIR}/_ext/1967181203/tPLSocket.o: ../../../src/platform_layer/posix/tPLSocket.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/1967181203
	${RM} $@.d
	$(COMPILE.c) -O2 -Wall -s -I../../../inc -I../../../src/platform_layer/inc -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/1967181203/tPLSocket.o ../../../src/platform_layer/posix/tPLSocket.c

${OBJECTDIR}/_ext/1386528437/liblogger.o: ../../../src/liblogger.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/1386528437
	${RM} $@.d
	$(COMPILE.c) -O2 -Wall -s -I../../../inc -I../../../src/platform_layer/inc -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/1386528437/liblogger.o ../../../src/liblogger.c

${OBJECTDIR}/_ext/1967181203/tPLTime.o: ../../../src/platform_layer/posix/tPLTime.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/1967181203
	${RM} $@.d
	$(COMPILE.c) -O2 -Wall -s -I../../../inc -I../../../src/platform_layer/inc -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/1967181203/tPLTime.o ../../../src/platform_layer/posix/tPLTime.c

${OBJECTDIR}/_ext/1386528437/log_util.o: ../../../src/log_util.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/1386528437
	${RM} $@.d
	$(COMPILE.c) -O2 -Wall -s -I../../../inc -I../../../src/platform_layer/inc -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/1386528437/log_util.o ../../../src/log_util.c

${OBJECTDIR}/_ext/1386528437/file_logger.o: ../../../src/file_logger.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/1386528437
	${RM} $@.d
	$(COMPILE.c) -O2 -Wall -s -I../../../inc -I../../../src/platform_layer/inc -fPIC  -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/1386528437/file_logger.o ../../../src/file_logger.c

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r ${CND_BUILDDIR}/${CND_CONF}
	${RM} ${CND_DISTDIR}/${CND_CONF}/liblogger.so

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
