package com.haier.controler.process;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.haier.controler.api.CommandIndex;
import com.haier.controler.api.IConnectStateChange;
import com.haier.controler.api.RealTimeCallBack;
import com.haier.controler.api.SerialPort;
import com.haier.controler.bean.ProcessDataBean;
import com.haier.controler.utils.CommandUtil;
import com.haier.controler.utils.ControlerLogUtil;
import com.haier.controler.utils.PrintUtil;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.haier.controler.api.CommandIndex.BEATS_DELAY;
import static com.haier.controler.api.CommandIndex.REQUEST;

/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/8.
 *     @desc  : BeatsHandler
 * </pre>
 */
public class BeatsHandler {
    private SerialPort serialPort;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private HandlerThread kernelThread;
    private ProcessDataBean processDataBean;
    private static Handler mHandler;
    private SerialDataProcess mSerialData;
    private byte[] buffer = new byte[256];
    private int size = 0;
    private boolean timeOut;
    private String responseCodeCache;
    private IConnectStateChange connectStateChange;
    private int checkX, checkY;

    public BeatsHandler() {
        //打开串口
        try {
            serialPort = new SerialPort();
            mInputStream = serialPort.getInputStream();
            mOutputStream = serialPort.getOutputStream();
        } catch (IOException e) {
            ControlerLogUtil.e("Open SerialPort Error");
        }
        process();
    }

    public Handler getHandler() {
        return mHandler;
    }

    public ProcessDataBean getProcessDataBean() {
        return processDataBean;
    }

    //创建线程
    private void process() {
        processDataBean = new ProcessDataBean();
        mSerialData = new SerialDataProcess(processDataBean);
        kernelThread = new HandlerThread("BonresumeeatsThread");
        kernelThread.start();
        mHandler = new Handler(kernelThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (mInputStream == null || null == mOutputStream) {
                    ControlerLogUtil.e("HandlerMessage SerialPort Error");
                    return;
                }
                removeMessages(REQUEST);

                sendCommand(CommandUtil.getCommend(msg));

                sendEmptyMessageDelayed(REQUEST, BEATS_DELAY);
            }
        };
        mHandler.sendEmptyMessage(REQUEST);
    }

    public void sendCommand(byte[] mSendByte) {
        timeOut = true;
        try {
            ProcessData:
            for (int i = 0; i < 3; i++) {
                //尝试发送300
                ControlerLogUtil.i("SendByte:" + PrintUtil.BytesToString(mSendByte) + ",SendCnt:" + i);
                mOutputStream.write(mSendByte);

                for (int j = 0; j < 5; j++) {
                    //尝试500ms接收
                    checkX = mInputStream.available();
                    SystemClock.sleep(100);
                    checkY = mInputStream.available();
                    if (checkX != 0 && checkX == checkY) {
                        size = mInputStream.read(buffer);
                        responseCodeCache = PrintUtil.BytesToString(buffer, size);
                        ControlerLogUtil.i("ReceiveByte:" + responseCodeCache + ",size:" + size);
                        //处理信息
                        mSerialData.processData(buffer, size, responseCodeCache);
                        timeOut = false;
                        break ProcessData;
                    }
                }
            }
        } catch (IOException e) {
            ControlerLogUtil.e("温控读写异常");
        }

        //超时逻辑
        processDataBean.setTimeOut(timeOut);
        if (connectStateChange != null) {
            connectStateChange.connectChange();
        }
    }

    public void setRealTimeCallback(RealTimeCallBack realTimeCallback) {
        mSerialData.setRealListener(realTimeCallback);
    }

    public void setConnectStateChange(IConnectStateChange connectStateChange) {
        this.connectStateChange = connectStateChange;
    }

    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        kernelThread.quit();
        serialPort.SerialPortClose();
        ControlerLogUtil.i("BeatsHandler onDestroy...");
    }
}

