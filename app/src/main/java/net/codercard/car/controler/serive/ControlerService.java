package net.codercard.car.controler.serive;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import net.codercard.car.controler.api.ProxyObject;
import net.codercard.car.controler.process.BeatsHandler;
import net.codercard.car.controler.process.ProxyHandler;

/**
 * <pre>
 *     @author : Created by Phantom
 *     email : phantom@gradle.top‍
 *     time  : 2018/6/20.
 *     desc  : ControlerService
 * </pre>
 */
public class ControlerService extends Service {

    PowerManager.WakeLock mWakeLock;
    ProxyObject proxyObject;
    BeatsHandler beatsHandler;
    MyBind myBind;

    @Override
    public void onCreate() {
        super.onCreate();
        keepCPURunning();
        beatsHandler = new BeatsHandler(this);
        proxyObject = new ProxyHandler(beatsHandler);
        myBind = new MyBind();
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
        proxyObject.onDestroy();
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
}
