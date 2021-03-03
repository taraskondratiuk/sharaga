#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include <math.h>

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

void printString(char* str) {
    printf("%s\n", str);
}
