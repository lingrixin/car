package com.haier.controler.serive;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.haier.controler.api.ProxyObject;
import com.haier.controler.process.BeatsHandler;
import com.haier.controler.process.DoorHandler;
import com.haier.controler.process.ProxyHandler;
import com.haier.controler.process.RealTimeCheckHandler;
import com.haier.controler.utils.ControlerLogUtil;

/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/8.
 *     @desc  : ControlerService 温控灯光通信服务
 * </pre>
 */
public class ControlerService extends Service {
    BeatsHandler beatsHandler;
    ProxyObject proxyObject;
    RealTimeCheckHandler realTimeCheckHandler;
    PowerManager.WakeLock mWakeLock;
    DoorHandler doorHandler;
    MyBind myBind;

    @Override
    public void onCreate() {
        super.onCreate();
        keepCPURunning();
        //创建心跳线程
        //打开串口
        //创建解析类
        beatsHandler = new BeatsHandler();
        //创建代理类
        proxyObject = new ProxyHandler(beatsHandler, getBaseContext());
        doorHandler = new DoorHandler(proxyObject,getBaseContext());
        myBind = new MyBind();
        //创建校验线程
        realTimeCheckHandler = new RealTimeCheckHandler(proxyObject, getBaseContext());
    }

    private void keepCPURunning() {
        mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ControlerServiceKeep");
        mWakeLock.acquire();
    }

    private void unKeepCPURunning() {
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBind;
    }

    public class MyBind extends Binder {
        public ProxyObject getProxy() {
            return proxyObject;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unKeepCPURunning();
        realTimeCheckHandler.onDestroy();
        proxyObject.onDestroy();
        beatsHandler.onDestroy();
        ControlerLogUtil.i("ControlerService onDestroy...");
    }
}
