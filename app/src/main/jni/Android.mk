LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := native-lib
LOCAL_SRC_FILES := native-lib.cpp

LOCAL_C_INCLUDES += src/main/jni

include $(BUILD_SHARED_LIBRARY)