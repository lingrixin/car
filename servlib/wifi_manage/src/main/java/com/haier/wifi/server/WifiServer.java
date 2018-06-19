package com.haier.wifi.server;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.util.Log;


import com.haier.wifi.api.IScanResult;
import com.haier.wifi.util.WifiUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p class="note"></p>
 * created by LRXx at 2017-8-1
 */
public class WifiServer extends WifiStatBaseServer {

    private final MyBinder binder = new MyBinder();

    public WifiServer() {
    }

    public KernelHandler handler;
    private boolean onScanning = false;
    private IScanResult iScanResult;
    private List<ScanResult> resultList = new ArrayList<>();
    public static final int SCANNER_DELAYED = 6000;

    public static final int SCAN = 0x00;
    public static final int OPEN_WIFI = 0x01;
    public static final int CLOSE_WIFI = 0x02;
    public static final int CONNECT_POINT = 0x03;
    public static final int FORGET_NETWORK = 0x04;
    public static final int CONNECT_HAS_PASSWORD = 0x05;

//    private IServerHandlerCreateListener iServerHandlerCreateListener = null;
//
//    public void setiServerHandlerCreateListener(IServerHandlerCreateListener listener){
//        iServerHandlerCreateListener = listener;
//    }


    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(runnable).start();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (!isRegister) {
            registerStat();
        }
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (handler != null) {
//            iServerHandlerCreateListener = null;
            handler.onPause();
        }
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {
        public WifiServer getService() {
            return WifiServer.this;
        }
    }

    public class KernelHandler extends Handler {

        public void onPause() {
            removeMessages(SCAN);
            onScanning = false;
        }

        public void onResume() {
            if (hasMessages(SCAN)) {
                return;
            }
            sendEmptyMessage(SCAN);
            onScanning = true;
        }

        private void again() {
            if (hasMessages(SCAN)) {
                return;
            }
            sendEmptyMessageDelayed(SCAN, SCANNER_DELAYED);
            onScanning = true;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCAN:
                    if (iScanResult != null) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_WIFI_STATE)
                                != PackageManager.PERMISSION_GRANTED) {
                            return ;
                        }
                        wifiManager.startScan();
                        resultList = wifiManager.getScanResults();
                        boolean has_connecting = false;
                        //先判断是否有正在连接的SSID,有的话提出来,原list中remove掉
                        Pair<Integer,ScanResult> currentPair = WifiUtil.isConnecting(wifiManager, resultList);
                        //排序list
                        Collections.sort(resultList, WifiUtil.comparator);

                        //把正在连接的放到第0个,给一个boolean值 (客户判断值,将第0位变色)
                        if (currentPair !=null && currentPair.second != null) {
                            resultList.add(0, currentPair.second);
                            has_connecting = true;
                        }

                        //返回一个带是否保存密码的List
                        List<Pair<Integer, ScanResult>> onFinalList = WifiUtil.toCheckWifiList(wifiManager, resultList);

                        //把处理好的list回调给客户
                        if (iScanResult != null) {
                            iScanResult.toResult(onFinalList, has_connecting,currentPair==null?-1:currentPair.first);
//                            Log.d("LRX", "iScanResult : " + has_connecting);
                        }
                        //下一次扫描
                        again();
                    }
                    break;
                case OPEN_WIFI:
                    if (!wifiManager.isWifiEnabled()) {
                        wifiManager.setWifiEnabled(true);
                    }
                    break;
                case CLOSE_WIFI:
                    if (wifiManager.isWifiEnabled()) {
                        onPause();
                        wifiManager.setWifiEnabled(false);
                    }
                    break;
                case CONNECT_POINT:
                    /**
                     * 接收一个pair对象里面有一个密码和一个ScanResult
                     * 去连接网络实现一个状态接口
                     * 当成功时把wificonfig保存
                     * 失败时把wificonfig删除
                     */
                    Pair<String, ScanResult> pair = (Pair<String, ScanResult>) msg.obj;
                    if (pair != null && wifiManager.isWifiEnabled()) {
                        WifiConfiguration wm = WifiUtil.autoCreateConfig(wifiManager, pair);
                        if (wm == null) {
                            return;
                        }
                        connect(wm);
                    }
                    break;
                case FORGET_NETWORK:
                    if (WifiUtil.mForgetNetwork(wifiManager, msg.arg1)) {
                        onResume();
                    }
                    break;
                case CONNECT_HAS_PASSWORD:
                    connectNetWordId(msg.arg1);
                    onResume();
                    break;
                default:
                    break;
            }
        }
    }

    public void setiScanResult(IScanResult iScanResult) {
        this.iScanResult = iScanResult;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            handler = new KernelHandler();
            /*if(iServerHandlerCreateListener != null){
                iServerHandlerCreateListener.onCreate();
            }*/
            Looper.loop();
        }
    };

//    public interface IServerHandlerCreateListener{
//        void onCreate();
//    }
    //wifi控制
    //开关
    //扫描
    //连接
    //保存密码
    //忘记密码

    //wifi当前状态
    //开关状态
    //wifi连接状态
    //扫描状态
    //切换状态

}
