cmake_minimum_required(VERSION 3.14)
project(lab2)

set(OpenGlLinkers -lglut -lGLU -lGL)


add_executable(lab2 main.cpp FigureTemplate.cpp SolidCube.cpp WireCube.cpp SolidTorus.cpp WireTorus.cpp)
target_link_libraries(lab2 ${OpenGlLinkers})