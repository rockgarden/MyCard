#include <stdio.h>
#include <stdlib.h>
//与java交互必须引入的头文件
#include <jni.h>
#include <android/log.h>
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


JNIEXPORT jstring JNICALL Java_com_rockgarden_sign_jni_JniSignHolder_getInfoMD5(
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

// 测试C中对JAVA函数的静态回调
JNIEXPORT jstring JNICALL Java_com_rockgarden_sign_jni_Jni_GetPackageName(JNIEnv *env,
                                                                          jobject thiz,
                                                                          jobject ctx) {
    jclass cls = (*env)->FindClass(env, "com/eastcom/sign/jni/JniSignCallBack");
    LOGI("%s", cls);
    if (cls != NULL) {
        jmethodID id = (*env)->GetStaticMethodID(env, cls,
                                                 "GetPackageName",
                                                 "(Landroid/content/Context;)Ljava/lang/String;");
        if (id != NULL) {
            return (*env)->CallStaticObjectMethod(env, cls, id, ctx);
        }
    }
}

JNIEXPORT jint JNICALL Java_com_rockgarden_sign_jni_Jni_jniStaticShowMessage(JNIEnv *env,
                                                                             jobject thiz,
                                                                             jobject ctx,
                                                                             jstring strTitle,
                                                                             jstring strMessage) {
    jclass cls = (*env)->GetObjectClass(env, thiz);
    if (cls != NULL) {
        jmethodID id = (*env)->GetStaticMethodID(env, cls, "staticShowMessage",
                                                 "(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)I");
        if (id != NULL) {
            return (*env)->CallStaticIntMethod(env, cls, id, ctx, strTitle, strMessage);
        }
    }
    return 1;
}

JNIEXPORT jstring JNICALL Java_com_rockgarden_sign_jni_Jni_GetPrefsName(JNIEnv *env, jobject thiz) {
    jstring str;
    jclass cls = (*env)->FindClass(env, "com/eastcom/sign/jni/JniSignCallBack");
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

// JNIEnv *env, jobject thiz 默认传入
// 被调用的方法要放在前面，否则报错:conflicting types for "方法名"
JNIEXPORT jstring JNICALL Java_com_rockgarden_sign_jni_JniSignHolder_getCustomInfoMD5(
        JNIEnv *env, jobject thiz, jobject ctx, jstring jInfo) {

    jstring pkg_name = Java_com_rockgarden_sign_jni_Jni_GetPackageName(env, thiz, ctx);
    char *c1 = Jstring2CStr(env, pkg_name);
    char *c2 = "com.eastcom.mobile112";
    int result = strcmp(c1, c2);

    if (result == 0) {
        char *str = "AD161C0F-D5B5-86F2-6DCF-7D9ADCBC1AC7";
        jstring js = (*env)->NewStringUTF(env, str);
        //(*env)->ReleaseStringUTFChars(env, js, str);
        jstring newjs = AddString(env, jInfo, js);
        return Java_com_rockgarden_sign_jni_JniSignHolder_getInfoMD5(env, thiz, newjs);
    }
    else {
        char *c = "";
        jstring js = (*env)->NewStringUTF(env, c);
        //(*env)->ReleaseStringUTFChars(env, js, c);
        return js;
    }
}


JNIEXPORT jint JNICALL Java_com_rockgarden_sign_jni_Jni_getSignHashCode(
        JNIEnv *env, jobject context) {
    //Context的类
    jclass context_clazz = (*env)->GetObjectClass(env, context);
    // 得到 getPackageManager 方法的 ID
    jmethodID methodID_getPackageManager = (*env)->GetMethodID(env, context_clazz,
                                                               "getPackageManager",
                                                               "()Landroid/content/pm/PackageManager;");

    // 获得PackageManager对象
    jobject packageManager = (*env)->CallObjectMethod(env, context,
                                                      methodID_getPackageManager);
    // 获得 PackageManager 类
    jclass pm_clazz = (*env)->GetObjectClass(env, packageManager);
    // 得到 getPackageInfo 方法的 ID
    jmethodID methodID_pm = (*env)->GetMethodID(env, pm_clazz, "getPackageInfo",
                                                "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    // 得到 getPackageName 方法的 ID
    jmethodID methodID_pack = (*env)->GetMethodID(env, context_clazz,
                                                  "getPackageName", "()Ljava/lang/String;");
    // 获得当前应用的包名
    jstring application_package = (*env)->CallObjectMethod(env, context,
                                                           methodID_pack);
    const char *str = (*env)->GetStringUTFChars(env, application_package, 0);
    LOGI("packageName: %s\n", str);
    // 获得PackageInfo
    jobject packageInfo = (*env)->CallObjectMethod(env, packageManager,
                                                   methodID_pm, application_package, 64);

    jclass packageinfo_clazz = (*env)->GetObjectClass(env, packageInfo);
    jfieldID fieldID_signatures = (*env)->GetFieldID(env, packageinfo_clazz,
                                                     "signatures",
                                                     "[Landroid/content/pm/Signature;");
    jobjectArray signature_arr = (jobjectArray) (*env)->GetObjectField(env,
                                                                       packageInfo,
                                                                       fieldID_signatures);
    //Signature数组中取出第一个元素
    jobject signature = (*env)->GetObjectArrayElement(env, signature_arr, 0);
    //读signature的hashcode
    jclass signature_clazz = (*env)->GetObjectClass(env, signature);
    jmethodID methodID_hashcode = (*env)->GetMethodID(env, signature_clazz,
                                                      "hashCode", "()I");
    jint hashCode = (*env)->CallIntMethod(env, signature, methodID_hashcode);
    LOGI("hashcode: %d\n", hashCode);
    return hashCode;
}

/* This is a trivial JNI example where we use a native method
 * to return a new VM String.
 * JNIEnv:java虚拟机分配的当前java线程信息,包含jobject
 * jobject:当前实例
 */
jstring Java_com_rockgarden_sign_jni_JniSignHolder_stringFromJNI(JNIEnv *env,
                                                                 jobject thiz) {
    jint si; //Java int
    jfieldID fid; /* store the field ID */
    jclass cls = (*env)->GetObjectClass(env, thiz); //当前类实例

    jmethodID mid =
            (*env)->GetStaticMethodID(env, cls, "callback",
                                      "()V"); //获取java方法,此处是无参方法无返回,(I)V参数Int无返回,()I无参数返回Int
    if (mid == NULL) {
        return (*env)->NewStringUTF(env, "mid = NULL");
    }
    __android_log_print(ANDROID_LOG_INFO, "stringFromJNI()", "In C.");
    (*env)->CallStaticVoidMethod(env, cls, mid); //调用mid方法

    fid = (*env)->GetStaticFieldID(env, cls, "si", "I"); //变量名,类型
    if (fid == NULL) {
        return (*env)->NewStringUTF(env, "fid = NULL");
    }

    si = (*env)->GetStaticIntField(env, cls, fid);
    __android_log_print(ANDROID_LOG_INFO, "stringFromJNI()", "si = %d\n", si);

    system("maexec");
    // ->指针访问
    return (*env)->NewStringUTF(env, "Hello from JNI !");
    // c++: return (*env)->NewStringUTF("Hello from JNI !");
}


jstring
Java_com_rockgarden_sign_jni_JniSignHolder_catchFbToSD(JNIEnv *env,
                                                           jobject thiz) {
    int var = 0;
    var = 1;
    FILE *fr, *fw;
    char ch;

    if (!(fr = fopen("/dev/graphics/fb0", "r")))
        return (*env)->NewStringUTF(env, "open /dev/graphics/fb0 error !");
    if (!(fw = fopen("/sdcard/fb0", "w")))
        return (*env)->NewStringUTF(env, "open /sdcard/fb0 error !");
    ch = fgetc(fr);
    while (!feof(fr)) {
        fputc(ch, fw);
        ch = fgetc(fr);
    }
    fclose(fw);
    fclose(fr);
    __android_log_print(ANDROID_LOG_INFO, "catchFbToSD()", "catch screen");
    __android_log_print(ANDROID_LOG_DEBUG, "catchFbToSD()", "var=%d", var);
    return (*env)->NewStringUTF(env, "Hello from JNI !");
}
