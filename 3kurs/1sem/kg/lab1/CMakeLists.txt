cmake_minimum_required(VERSION 3.14)
project(lab1)

set(OpenGlLinkers -lglut -lGLU -lGL)


add_executable(lab1 main.cpp)
target_link_libraries(lab1 ${OpenGlLinkers})