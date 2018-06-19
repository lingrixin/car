package com.haier.controler.api;


import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class SerialPort {

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    private final String[] SerialPortDevice = {"/dev/ttyMFD1", "/dev/ttyMT2", "/dev/ttyS3"};//串口地址
    private final int baudrate = 9600;//波特率

    public SerialPort() throws IOException {
       open();
    }

    private void open() throws IOException {
        for (String str : SerialPortDevice){
            mFd = SerialOpen(str, baudrate);
            if(mFd != null){
                break;
            }
        }
        if (mFd == null) {
            throw new IOException("serial open error , not find match model");
        }else {
            mFileInputStream = new FileInputStream(mFd);
            mFileOutputStream = new FileOutputStream(mFd);
        }
    }

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    // JNI
    private native FileDescriptor SerialOpen(String path, int baudrate);//打开串口

    private native void SerialClose();

    static {
        System.loadLibrary("native-serial-jni");//
    }

    public void SerialPortClose() {
        SerialClose();
    }

    public void SerialPortReOpen() throws IOException {
        open();
    }
}