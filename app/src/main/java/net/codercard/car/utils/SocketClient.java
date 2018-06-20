/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package net.codercard.car.utils;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by qibin01 on 2018/6/20.
 */
public class SocketClient {

    private static SocketClient instance;

    private Socket mSocket;
    private PrintStream mPrintStream;

    public static SocketClient get() {
        if (instance == null) {
            synchronized(SocketClient.class) {
                if (instance == null) {
                    instance = new SocketClient();
                }
            }
        }

        return instance;
    }

    public boolean connect(String addr, int port) {
        try {
            mSocket = new Socket(addr, port);
            mPrintStream = new PrintStream(mSocket.getOutputStream(), true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void close() {
        try {
            mSocket.close();
            mPrintStream = null;
            mSocket = null;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean send(String msg) {
        if (mSocket != null && mPrintStream != null && mSocket.isConnected()) {
            mPrintStream.println(msg);
        }

        return false;
    }
}
