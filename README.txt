c-logger : A logging framework for C / C++ based on http://liblogger.sourceforge.net/
---------------------------------------------------------
Copyright : Jefty E. Negapatan - jeftyneg _at_ gmail _dot_ com
License : GNU Lesser General Public License(see files COPYING and COPYING.LESSER)
Website : http://code.google.com/p/c-logger/
---------------------------------------------------------

BUILDING - UNIX
----------------
c-logger uses NetBeans as IDE but can be built without using the IDE.
The sources are available under the folder src.
To build run "make" from the folder src/build/linux/logserver.
ex : To build in release mode, use "make CONF=Release"
ex : To cross compile for arm, use "make CROSS_COMPILE=arm-linux-"

This generates :
* The shared library : liblogger.so

c-logger includes testapp to test c-logger.
To build run "make" from the folder src/build/linux/testapp.
ex : To build in release mode, use "make CONF=Release"
ex : To cross compile for arm, use "make CROSS_COMPILE=arm-linux-"
* To run the test app :
  $export LD_LIBRARY_PATH=.:$LD_LIBRARY_PATH
  $./testapp

BUILDING - WINDOWS
----------------
For windows, the Visual Studio solution is provided under folder src/build/liblogger_win32/.

TEST APP
----------
The best way to understand the usage of c-logger is to go through the test apps :
* src/testapp

DOCUMENTATION
----------------
Documentation is provided at the website : http://code.google.com/p/c-logger/,
alternatively you can build the documentation  if you have doxygen installed.
Under the folder docs/ run command "gendoc.sh" and the html docs are generated in ./docs/docs/html

BUGS / SUGGESTIONS
----------------------------
Pls file the bug reports here: http://code.google.com/p/c-logger/issues/list

You can also mail the developer(s) at jeftyneg _A_T_ gmail _D_O_T_ com

-------------
Thanks for your interest in c-logger, hope it will be helpful...
