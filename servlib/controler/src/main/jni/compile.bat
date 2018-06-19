:: Please set %NDK_PATH% as your NDK path
set NDK_PATH=F:\Developer\android-NDK


set PATH=%NDK_PATH%;%PATH%
call ndk-build.cmd APP_ABI=x86,armeabi-v7a APP_BUILD_SCRIPT=./Android.mk NDK_LIBS_OUT=./prebuilt

set CURRENT_PATH_X86=.\prebuilt\x86\*.so
set CURRENT_PATH_ARM=.\prebuilt\armeabi-v7a\*.so
set TARGET_X86=.\..\libs\x86\
set TARGET_ARM=.\..\libs\armeabi-v7a\
copy %CURRENT_PATH_X86% %TARGET_X86%
copy %CURRENT_PATH_ARM% %TARGET_ARM%

@echo Finish compilation, please enter any key to exit...
@pause > nul
