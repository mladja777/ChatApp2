//
// Created by Mladen on 6/7/2018.
//

#include "matic_mladen_chatapplication_JNIxor.h"
#include "stdlib.h"
#include "string.h"

extern "C" {
    JNIEXPORT jstring JNICALL Java_matic_mladen_chatapplication_JNIxor_encryption(JNIEnv *env, jobject obj, jstring message_java) {
        const char *message = env->GetStringUTFChars(message_java, JNI_FALSE);
        int i = 0;
        int messageLen = 0;
        while(message[i] != '\0') {
            messageLen++;
            i++;
        }
        char encryptedMessage[messageLen+1];
        char key[] = "This is the key\0";
        int keyLen = 15;
        for(i = 0; i < messageLen; i++) {
            encryptedMessage[i] = message[i]^key[i%keyLen];
        }
        encryptedMessage[messageLen] = '\0';
        env->ReleaseStringUTFChars(message_java, message);
        jstring exportString = env->NewStringUTF(encryptedMessage);
        return exportString;
    }

    JNIEXPORT jstring JNICALL Java_matic_mladen_chatapplication_JNIxor_decryption(JNIEnv *env, jobject obj, jstring message_java) {
        const char *message = env->GetStringUTFChars(message_java, JNI_FALSE);
        int i = 0;
        int messageLen = 0;
        while(message[i] != '\0') {
            messageLen++;
            i++;
        }
        char decryptedMessage[messageLen+1];
        char key[] = "This is the key\0";
        int keyLen = 15;
        for(i = 0; i < messageLen; i++) {
            decryptedMessage[i] = message[i]^key[i%keyLen];
        }
        decryptedMessage[messageLen] = '\0';
        env->ReleaseStringUTFChars(message_java, message);
        jstring exportString = env->NewStringUTF(decryptedMessage);
        return exportString;
    }
}