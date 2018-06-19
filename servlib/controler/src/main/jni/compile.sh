#!/bin/bash
source /home/shilin/workspace/ndk/set.sh
ndk-build APP_ABI="x86 armeabi-v7a" APP_BUILD_SCRIPT=./Android.mk NDK_LIBS_OUT=../libs


#TARGET_X86 := $(shell pwd)/../libs/x86/
#TARGET_ARM := $(shell pwd)/../libs/armeabi-v7a/

#CURRENT_PATH_X86 := $(shell pwd)/prebuilt/x86/*.so
#CURRENT_PATH_ARM := $(shell pwd)/prebuilt/armeabi-v7a/*.so
#$(warning "the value of TARGET_X86 is $(TARGET_X86)")
#$(warning "the value of CURRENT_PATH_X86 is $(CURRENT_PATH_X86)")

#PRODUCT_COPY_FILES += $(TARGET_X86):$(CURRENT_PATH_X86)/*.so
#$(shell cp $(CURRENT_PATH_X86) $(TARGET_X86) -rf)


CURRENT_PATH_X86="./prebuilt/x86/*.so"
CURRENT_PATH_ARM="./prebuilt/armeabi-v7a/*.so"

TARGET_X86="./../libs/x86/"
TARGET_ARM="./../libs/armeabi-v7a/"

cp $CURRENT_PATH_X86 $TARGET_X86 -rf
cp $CURRENT_PATH_ARM $TARGET_ARM -rf


