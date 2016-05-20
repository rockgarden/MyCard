#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <string.h>
#include "sign_jni_Jni.h"
#include <android/log.h>
#include <malloc.h>
#include "md5c.h"

#define LOG_TAG "System.out.c"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

/**
 * 工具方法
 * 返回值 char* 这个代表char数组的首地址
 *  Jstring2CStr 把java中的jstring的类型转化成一个c语言中的char 字符串
 */
char *Jstring2CStr(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;
    jclass clsstring = (*env)->FindClass(env, "java/lang/String");
    jstring strencode = (*env)->NewStringUTF(env, "utf-8");
    jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes",
                                        "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray) (*env)->CallObjectMethod(env, jstr, mid,
                                                            strencode);
    jsize alen = (*env)->GetArrayLength(env, barr);
    jbyte *ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    (*env)->ReleaseByteArrayElements(env, barr, ba, 0);
    return rtn;
}

//java字符串转C字符串
char *jstringTostr(JNIEnv *env, jstring jstr) {
    char *pStr = NULL;

    jclass jstrObj = (*env)->FindClass(env, "java/lang/String");
    jstring encode = (*env)->NewStringUTF(env, "utf-8");
    jmethodID methodId = (*env)->GetMethodID(env, jstrObj, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray byteArray = (jbyteArray) (*env)->CallObjectMethod(env, jstr, methodId, encode);
    jsize strLen = (*env)->GetArrayLength(env, byteArray);
    jbyte *jBuf = (*env)->GetByteArrayElements(env, byteArray, JNI_FALSE);

    if (jBuf > 0) {
        pStr = (char *) malloc(strLen + 1);

        if (!pStr) {
            return NULL;
        }

        memcpy(pStr, jBuf, strLen);

        pStr[strLen] = 0;
    }

    (*env)->ReleaseByteArrayElements(env, byteArray, jBuf, 0);

    return pStr;
}

//C字符串转java字符串
jstring strToJstring(JNIEnv *env, const char *pStr) {
    int strLen = strlen(pStr);
    jclass jstrObj = (*env)->FindClass(env, "java/lang/String");
    jmethodID methodId = (*env)->GetMethodID(env, jstrObj, "", "([BLjava/lang/String;)V");
    jbyteArray byteArray = (*env)->NewByteArray(env, strLen);
    jstring encode = (*env)->NewStringUTF(env, "utf-8");

    (*env)->SetByteArrayRegion(env, byteArray, 0, strLen, (jbyte *) pStr);

    return (jstring) (*env)->NewObject(env, jstrObj, methodId, byteArray, encode);
}


JNIEXPORT jstring JNICALL Java_com_eastcom_sign_jni_Jni_getInfoMD5(
        JNIEnv *env, jobject thiz, jstring jInfo) {
    char *jstr = Jstring2CStr(env, jInfo);

    MD5_CTX context = {0};
    MD5Init(&context);
    MD5Update(&context, jstr, strlen(jstr));
    unsigned char dest[16] = {0};
    MD5Final(dest, &context);

    int i;
    char destination[32] = {0};
    for (i = 0; i < 16; i++) {
        sprintf(destination, "%s%02x", destination, dest[i]);
    }
    LOGI("%s", destination);
    return (*env)->NewStringUTF(env, destination);
}


jstring AddString(JNIEnv *env, jstring a,
                  jstring b) {

    char *str_a = Jstring2CStr(env, a);
    char *str_b = Jstring2CStr(env, b);

    int len_a = strlen(str_a);
    int len_b = strlen(str_b);

    //concat string
    char *str_result = (char *) malloc(len_a + len_b + 1);
    strcpy(str_result, str_a);
    strcat(str_result, str_b);
    jstring jstr_result = (*env)->NewStringUTF(env, str_result); //创建string对象
    //delete[] str_result;

    (*env)->ReleaseStringUTFChars(env, a, str_a);
    (*env)->ReleaseStringUTFChars(env, b, str_b);
    (*env)->ReleaseStringUTFChars(env, jstr_result, str_result);
    return jstr_result; //返回ab字符串连接之后的结果
}


JNIEXPORT jstring JNICALL Java_com_eastcom_sign_jni_Jni_GetPackageName(JNIEnv *env, jobject thiz,
                                                                       jobject ctx) {
    jstring str;
    jclass cls = (*env)->FindClass(env, "com/eastcom/sign/jni/JniCallBack");
    if (cls != NULL) {
        jmethodID id = (*env)->GetStaticMethodID(env, cls,
                                                 "GetPackageName",
                                                 "(Landroid/content/Context;)Ljava/lang/String;");
        if (id != NULL) {
            return (*env)->CallStaticObjectMethod(env, cls, id, ctx);
        }
    }
}


JNIEXPORT jstring JNICALL Java_com_eastcom_sign_jni_Jni_GetPrefsName(JNIEnv *env, jobject thiz) {
    jstring str;
    jclass cls = (*env)->FindClass(env, "com/eastcom/sign/jni/JniCallBack");
    if (cls != NULL) {
        jmethodID id = (*env)->GetStaticMethodID(env, cls,
                                                 "GetPrefsName",
                                                 "()Ljava/lang/String;");
        if (id != NULL) {
            str = (*env)->CallStaticObjectMethod(env, cls, id);
        }
        else {
            char *c = "";
            str = (*env)->NewStringUTF(env, c);
            //(*env)->ReleaseStringUTFChars(env, str, c);
        }
    }
    return str;
}

//被调用的方法要放在前面，否则报错:conflicting types for "方法名"
JNIEXPORT jstring JNICALL Java_com_eastcom_sign_jni_Jni_getCustomInfoMD5(
        JNIEnv *env, jobject thiz, jobject ctx, jstring jInfo) {

    jstring pkg_name = Java_com_eastcom_sign_jni_Jni_GetPackageName(env, thiz, ctx);
    char *c1 = Jstring2CStr(env, pkg_name);
    char *c2 = "com.eastcom.sign";
    int result = strcmp(c1, c2);

    if (result == 0) {
        char *str = "AD161C0F-D5B5-86F2-6DCF-7D9ADCBC1AC7";
        jstring js = (*env)->NewStringUTF(env, str);
        //(*env)->ReleaseStringUTFChars(env, js, str);
        jstring newjs = AddString(env, jInfo, js);
        return Java_com_eastcom_sign_jni_Jni_getInfoMD5(env, thiz, newjs);
    }
    else {
        char *c = "";
        jstring js = (*env)->NewStringUTF(env, c);
        //(*env)->ReleaseStringUTFChars(env, js, c);
        return js;
    }
}

