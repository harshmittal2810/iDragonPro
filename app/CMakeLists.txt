cmake_minimum_required(VERSION 3.10)
#......For logs
find_library(log-lib log)

add_library(frida-check SHARED src/main/cpp/fridacheck.cpp)
target_link_libraries(
        frida-check
        ${log-lib})
