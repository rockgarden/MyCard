LOCAL_PATH := $(call my-dir)  #指定源文件目录

#sign operations module
include $(CLEAR_VARS) #清空变量

LOCAL_MODULE    := sign  #模块名称，对应编译出libsign.so
LOCAL_SRC_FILES := sign.c md5c.c#指定要编译的源文件
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY) #指定编译动态链接库
APP_OPTIM := debug
LOCAL_CFLAGS := -g
