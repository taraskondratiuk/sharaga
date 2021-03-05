#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

int squareInt(int x) {
	printf("calling square for %d\n", x);
	return x * x;
}

float squareFloat(float x) {

	printf("calling square for %f\n", x);
	return x * x;
}

double squareDouble(double x) {
	printf("calling square for %lf\n", x);
	return x * x;
}

char* newStringToUppercase(char* str) {
    printf("calling new string toUppercase() for %s\n", str);
    char* tmp = (char*) malloc(strlen(str) * sizeof(char));
    char* res = tmp;
    while (*tmp) {
        *tmp = toupper((unsigned char) *str);
        tmp++;
        str++;
    }
    return res;
}

char* mutateStringToUppercase(char* str) {
    printf("calling mutating toUppercase() for %s\n", str);
    char* tmp = str;
    while (*tmp) {
        *tmp = toupper((unsigned char) *tmp);
        tmp++;
    }
    return str;
}
