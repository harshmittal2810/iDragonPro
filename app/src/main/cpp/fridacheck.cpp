//
// Created by Harsh Mittal on 03/04/23
//
#include <jni.h>
#include <string.h>
#include <unistd.h>
#include <stdbool.h>
#include <filesystem>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <android/log.h>
#include<thread>

void internalFridaCheck() {
    struct sockaddr_in sa;
    bool isFridaDetected = false;
    memset(&sa, 0, sizeof(sa));
    sa.sin_family = AF_INET;
    sa.sin_port = htons(27042);
    inet_aton("127.0.0.1", &(sa.sin_addr));

    int sock = socket(AF_INET, SOCK_STREAM, 0);

    if (connect(sock, (struct sockaddr *) &sa, sizeof sa) != -1) {
//        __android_log_print(ANDROID_LOG_INFO, "fridacheck ", "==>detected, closing socket");
        isFridaDetected = true;
    } else {
//        __android_log_print(ANDROID_LOG_INFO, "fridacheck ", "==>socket not connected");
    }
    close(sock);
    if (isFridaDetected)
        std::terminate();
};

__attribute__((constructor))
void detectfrida() {
    std::thread thread_object(internalFridaCheck);
    thread_object.detach();
}

extern "C" JNIEXPORT jboolean
JNICALL Java_com_idragonpro_andmagnus_MyApp_isFridaServerListening(JNIEnv *env, jobject thiz) {
    struct sockaddr_in sa;
    bool isFridaDetected = false;
    memset(&sa, 0, sizeof(sa));
    sa.sin_family = AF_INET;
    sa.sin_port = htons(27042);
    inet_aton("127.0.0.1", &(sa.sin_addr));

    int sock = socket(AF_INET, SOCK_STREAM, 0);

    if (connect(sock, (struct sockaddr *) &sa, sizeof sa) != -1) {
//        __android_log_print(ANDROID_LOG_INFO, "fridacheck ", "detected, closing socket");
        isFridaDetected = true;
    }
//    else{
//        __android_log_print(ANDROID_LOG_INFO, "fridacheck ", "socket not connected");
//    }
    close(sock);
    return static_cast<jboolean>(isFridaDetected);

}