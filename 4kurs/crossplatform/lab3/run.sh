swig -python lib.i
gcc -c lib.c lib_wrap.c -I/usr/include/python2.7 -I/usr/include/x86_64-linux-gnu/python2.7  -fno-strict-aliasing -Wdate-time -D_FORTIFY_SOURCE=2 -g -fdebug-prefix-map=/build/python2.7-U5f0ID/python2.7-2.7.18=. -fstack-protector-strong -Wformat -Werror=format-security  -DNDEBUG -g -fwrapv -O2 -Wall -Wstrict-prototypes -fPIC
gcc -shared lib.o lib_wrap.o -o _lib.so
python testscript.py
