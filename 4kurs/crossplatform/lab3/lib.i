%module lib

%{

extern int squareInt(int);

extern float squareFloat(float);

extern double squareDouble(double);

extern char* newStringToUppercase(char*);

extern char* mutateStringToUppercase(char*);
%}


int squareInt(int);

float squareFloat(float);

double squareDouble(double);

char* newStringToUppercase(char*);

char* mutateStringToUppercase(char*);
