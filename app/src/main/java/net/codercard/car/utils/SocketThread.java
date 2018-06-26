/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package net.codercard.car.utils;

import android.util.Log;

/**
 * Created by qibin01 on 2018/6/26.
 */
public class SocketThread extends Thread {

    public volatile boolean flag;

    public volatile float angle;
    public volatile float speed;

    private int sleepTime;

    public SocketThread(int time) {
        super("SocketThread");
        sleepTime = time;
    }

    public void newValue(float angle, float speed) {
        this.angle = angle;
        this.speed = speed;
    }

    @Override
    public void run() {
        while (flag) {
            try {
                Log.d("qibin", speed  + "," + angle);
                SocketClient.get().send(speed  + "," + angle);
                Thread.sleep(this.sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void start() {
        flag = true;
        super.start();
    }

    public synchronized void quit() {
        flag = false;
    }
}
