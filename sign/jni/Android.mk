#指定源文件目录
LOCAL_PATH := $(call my-dir)
#sign operations module
include $(CLEAR_VARS) #清空变量
#模块名称,对应编译出libsign.so
LOCAL_MODULE    := sign
#指定要编译的源文件
LOCAL_SRC_FILES := sign.c md5c.c
LOCAL_LDLIBS := -llog
#指定编译动态链接库
include $(BUILD_SHARED_LIBRARY)
APP_OPTIM := debug
LOCAL_CFLAGS := -g
