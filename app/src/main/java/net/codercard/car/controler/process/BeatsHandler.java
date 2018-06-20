package net.codercard.car.controler.process;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import net.codercard.car.controler.api.IConnectStateChange;
import net.codercard.car.controler.api.RealTimeCallBack;
import net.codercard.car.controler.bean.CommandIndex;
import net.codercard.car.controler.bean.ProcessDataBean;
import net.codercard.car.controler.bean.SocketsConfigInfo;
import net.codercard.car.utils.Constant;
import net.codercard.car.utils.SPUtils;

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
    private boolean connected;

    private HandlerThread kernelThread;
    private static Handler mHandler;
    /**
     * 配置信息
     */
    private SocketsConfigInfo socketsConfigInfo;

    public BeatsHandler(Context context) {
        this.context = context;
        socketsConfigInfo = new SocketsConfigInfo();
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
                    case CommandIndex.REQUEST:
                        //todo 检查连接状态
                        if(true){
                            connected = true;
                            if(connectStateChange!=null){
                                connectStateChange.connectOk();
                            }
                        }else {
                            connected = false;
                            stop();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }
    /**
     * 尝试连接
     */
    public void connect() {
        processSp();
        /**
         * todo 用socket去连接
         * 连接完，开启连接检查线程
         */
        start();
    }

    /**
     * 断开连接状态
     */
    public void disconnect(){
        //怎么中断这个连接
        connected = false;
        if(connectStateChange!=null){
            connectStateChange.connectFailure();
        }
    }

    public void start() {
        mHandler.sendEmptyMessage(CommandIndex.REQUEST);
    }

    public void stop(){
        mHandler.removeMessages(CommandIndex.REQUEST);
    }

    public void setConnectStateChange(IConnectStateChange connectStateChange) {
        this.connectStateChange = connectStateChange;
    }

    public void setRealListener(RealTimeCallBack realListener) {
        this.realListener = realListener;
    }

    public void processSp() {
        socketsConfigInfo.setSocketIp((String) SPUtils.get(context, Constant.SOCKET_ADDRESS, ""));
        socketsConfigInfo.setSocketPort((String) SPUtils.get(context, Constant.SOCKET_PORT, ""));
        socketsConfigInfo.setTurnAngle((String) SPUtils.get(context, Constant.TURN_ANGLE, ""));
        socketsConfigInfo.setSpeed((String) SPUtils.get(context, Constant.SPEED, ""));
        socketsConfigInfo.setSamplingInterval((String) SPUtils.get(context, Constant.SAMPLING_INTERVAL, ""));
    }
}
