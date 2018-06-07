LOCAL_PATH := $(call my-dir)

include $(CLEAN_VARS)

LOCAL_MODULE := xor_encryption
LOCAL_MODULE_FILENAME := xor_encryption
LOCAL_SRC_FILES := xor_encryption.cpp
include $(BUILD_SHARED_LIBRARY)