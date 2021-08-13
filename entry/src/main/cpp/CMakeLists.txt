# the minimum version of CMake.
cmake_minimum_required(VERSION 3.4.1)
project(MyApplication)

add_library(etc1 SHARED etc1_util.cpp)
target_link_libraries(etc1)
