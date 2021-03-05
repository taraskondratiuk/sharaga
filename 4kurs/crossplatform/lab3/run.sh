swig -python lib.i
gcc -c lib.c lib_wrap.c -fPIC $(python-config --cflags)
gcc -shared lib.o lib_wrap.o -o _lib.so
python testscript.py
