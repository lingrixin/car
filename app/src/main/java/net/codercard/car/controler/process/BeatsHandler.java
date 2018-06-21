package net.codercard.car.controler.process;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import net.codercard.car.controler.api.IConnectStateChange;
import net.codercard.car.controler.api.RealTimeCallBack;
import net.codercard.car.controler.bean.CommandIndex;
import net.codercard.car.controler.bean.SocketsConfigInfo;
import net.codercard.car.utils.Constant;
import net.codercard.car.utils.SPUtils;
import net.codercard.car.utils.SocketClient;

/**
 * <pre>
 *     @author : Created by Phantom
 *     email : phantom@gradle.top‍
 *     time  : 2018/6/20.
 *     desc  : BeatsHandler socket连接类
 * </pre>
 */
public class BeatsHandler {

    private Context context;
    private IConnectStateChange connectStateChange;
    private RealTimeCallBack realListener;

    private SocketClient socketClient;

    private HandlerThread kernelThread;
    private static Handler mHandler;
    private String cmd;
    /**
     * 配置信息
     */
    private SocketsConfigInfo socketsConfigInfo;

    public Handler getmHandler() {
        return mHandler;
    }

    public BeatsHandler(Context context) {
        this.context = context;
        socketsConfigInfo = new SocketsConfigInfo();
        socketClient = SocketClient.get();
        initProcee();
    }

    /**
     * 初始化检查线程
     */
    private void initProcee() {
        kernelThread = new HandlerThread("ConnectedCheckoutThread");
        kernelThread.start();
        mHandler = new Handler(kernelThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CommandIndex.CONNECT:
                        removeMessages(CommandIndex.CONNECT);
                        //解析最新的配置信息
                        if(processSp()){
                            Log.i("phan", "尝试连接");
                            if (socketClient.connect(socketsConfigInfo.getSocketIp(), Integer.valueOf(socketsConfigInfo.getSocketPort()))) {
                                //连接成功
                                connectStateChange.connectOk();
                                Log.i("phan", "连接成功");
                                startRequest();
                            } else {
                                connectStateChange.connectFailure();
                                Log.i("phan", "连接失败");
                            }
                        }else {
                            Log.i("phan", "配置信息读取失败");
                        }
                        break;
                    case CommandIndex.DISCONNECT:
                        clearRequest();
                        connectStateChange.connectFailure();
                        //todo主动断开连接，重置命令
                        socketClient.close();
                        break;
                    case CommandIndex.IS_CONNECTED:
                        if (socketClient.isConnected()) {
                            connectStateChange.connectOk();
                            startRequest();
                        } else {
                            connectStateChange.connectFailure();
                            clearRequest();
                        }
                        break;
                    case CommandIndex.SENT_CMD:
                        socketClient.send(String.valueOf(msg.obj));
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void startRequest() {
        clearRequest();
        mHandler.sendEmptyMessageDelayed(CommandIndex.IS_CONNECTED, CommandIndex.REQUEST_SPEED);
    }

    private void clearRequest() {
        mHandler.removeMessages(CommandIndex.IS_CONNECTED);
    }

    public void setConnectStateChange(IConnectStateChange connectStateChange) {
        this.connectStateChange = connectStateChange;
    }

    public void setRealListener(RealTimeCallBack realListener) {
        this.realListener = realListener;
    }

    private boolean processSp() {
        Log.i("phan", "读取配置信息");
        socketsConfigInfo.setSocketIp((String) SPUtils.get(context, Constant.SOCKET_ADDRESS, ""));
        socketsConfigInfo.setSocketPort((String) SPUtils.get(context, Constant.SOCKET_PORT, ""));
        socketsConfigInfo.setTurnAngle((String) SPUtils.get(context, Constant.TURN_ANGLE, ""));
        socketsConfigInfo.setSpeed((String) SPUtils.get(context, Constant.SPEED, ""));
        socketsConfigInfo.setSamplingInterval((String) SPUtils.get(context, Constant.SAMPLING_INTERVAL, ""));
        return checkoutConfig();
    }

    private boolean checkoutConfig() {
        return !(TextUtils.isEmpty(socketsConfigInfo.getSocketIp())
                || TextUtils.isEmpty(socketsConfigInfo.getSocketPort())
                || TextUtils.isEmpty(socketsConfigInfo.getTurnAngle())
                || TextUtils.isEmpty(socketsConfigInfo.getSpeed())
                || TextUtils.isEmpty(socketsConfigInfo.getSamplingInterval()));
    }
}
