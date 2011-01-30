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
FC=
AS=as

# Macros
CND_PLATFORM=GNU-Linux-x86
CND_CONF=Debug
CND_DISTDIR=dist

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/_ext/2407264/src_nofilename.o \
	${OBJECTDIR}/_ext/2407264/src_warn.o \
	${OBJECTDIR}/_ext/2407264/src_debug.o \
	${OBJECTDIR}/_ext/2407264/src_fatal.o \
	${OBJECTDIR}/_ext/2407264/src_min.o \
	${OBJECTDIR}/_ext/2407264/main.o \
	${OBJECTDIR}/_ext/2407264/src_nologs.o


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
LDLIBSOPTIONS=-Wl,-rpath ../logger/dist/Debug -L../logger/dist/Debug -llogger

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-Debug.mk dist/Debug/testapp

dist/Debug/testapp: ../logger/dist/Debug/liblogger.so

dist/Debug/testapp: ${OBJECTFILES}
	${MKDIR} -p dist/Debug
	${LINK.c} -o ${CND_DISTDIR}/${CND_CONF}/testapp ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/_ext/2407264/src_nofilename.o: ../../../testapp/src_nofilename.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/2407264
	${RM} $@.d
	$(COMPILE.c) -g -Wall -I../../../inc -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/2407264/src_nofilename.o ../../../testapp/src_nofilename.c

${OBJECTDIR}/_ext/2407264/src_warn.o: ../../../testapp/src_warn.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/2407264
	${RM} $@.d
	$(COMPILE.c) -g -Wall -I../../../inc -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/2407264/src_warn.o ../../../testapp/src_warn.c

${OBJECTDIR}/_ext/2407264/src_debug.o: ../../../testapp/src_debug.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/2407264
	${RM} $@.d
	$(COMPILE.c) -g -Wall -I../../../inc -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/2407264/src_debug.o ../../../testapp/src_debug.c

${OBJECTDIR}/_ext/2407264/src_fatal.o: ../../../testapp/src_fatal.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/2407264
	${RM} $@.d
	$(COMPILE.c) -g -Wall -I../../../inc -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/2407264/src_fatal.o ../../../testapp/src_fatal.c

${OBJECTDIR}/_ext/2407264/src_min.o: ../../../testapp/src_min.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/2407264
	${RM} $@.d
	$(COMPILE.c) -g -Wall -I../../../inc -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/2407264/src_min.o ../../../testapp/src_min.c

${OBJECTDIR}/_ext/2407264/main.o: ../../../testapp/main.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/2407264
	${RM} $@.d
	$(COMPILE.c) -g -Wall -I../../../inc -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/2407264/main.o ../../../testapp/main.c

${OBJECTDIR}/_ext/2407264/src_nologs.o: ../../../testapp/src_nologs.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/2407264
	${RM} $@.d
	$(COMPILE.c) -g -Wall -I../../../inc -MMD -MP -MF $@.d -o ${OBJECTDIR}/_ext/2407264/src_nologs.o ../../../testapp/src_nologs.c

# Subprojects
.build-subprojects:
	cd ../logger && ${MAKE}  -f Makefile CONF=Debug

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r build/Debug
	${RM} dist/Debug/testapp

# Subprojects
.clean-subprojects:
	cd ../logger && ${MAKE}  -f Makefile CONF=Debug clean

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
