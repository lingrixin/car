package com.haier.controler.process;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.haier.controler.api.CommandIndex;
import com.haier.controler.api.ProxyObject;
import com.haier.controler.utils.ControlerLogUtil;

import java.util.Observable;
import java.util.Observer;

import static com.haier.controler.api.CommandIndex.BROADCAST_ACTION_DOOR_CLOSE;
import static com.haier.controler.api.CommandIndex.BROADCAST_ACTION_DOOR_OPEN;
import static com.haier.controler.api.CommandIndex.BROADCAST_ACTION_DOOR_OPEN_ALARM;
import static com.haier.controler.api.CommandIndex.OPEN_DOOR_ALARM;

/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/13.
 *     @desc  : DoorHandler 开关门逻辑处理
 * </pre>
 */
public class DoorHandler implements Observer {
    private ProxyObject proxyObject;
    private HandlerThread kernelThread;
    private static Handler mHandler;
    private boolean doorState;
    private Context context;

    public DoorHandler(ProxyObject proxyObject, Context context) {
        this.proxyObject = proxyObject;
        this.context = context;
        init();
    }

    private void init() {
        kernelThread = new HandlerThread("DoorHandlerThread");
        kernelThread.start();
        mHandler = new Handler(kernelThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case CommandIndex.REQUEST:
                        if (doorState != proxyObject.getWineState().isColdOn()) {
                            doorState = proxyObject.getWineState().isColdOn();
                            if (doorState) {
                                //开门状态
                                sendLocalOpenDoor();
                                sendEmptyMessageDelayed(OPEN_DOOR_ALARM, CommandIndex.OPEN_DOOR_DELAY);
                            } else {
                                //关门状态
                                removeMessages(OPEN_DOOR_ALARM);
                                sendLocalCloseDoor();
                            }
                        }
                        break;
                    case OPEN_DOOR_ALARM:
                        sendLocalOpenDoorAlarm();
                        break;
                    default:
                        break;
                }
            }
        };
        proxyObject.addObserver(this);
    }

    private void sendLocalOpenDoor() {
        ControlerLogUtil.i(BROADCAST_ACTION_DOOR_OPEN);
        context.sendBroadcast(new Intent(BROADCAST_ACTION_DOOR_OPEN));
    }

    private void sendLocalOpenDoorAlarm() {
        ControlerLogUtil.i(BROADCAST_ACTION_DOOR_OPEN_ALARM);
        context.sendBroadcast(new Intent(BROADCAST_ACTION_DOOR_OPEN_ALARM));
    }

    private void sendLocalCloseDoor() {
        ControlerLogUtil.i(BROADCAST_ACTION_DOOR_CLOSE);
        context.sendBroadcast(new Intent(BROADCAST_ACTION_DOOR_CLOSE));
    }

    @Override
    public void update(Observable o, Object arg) {
        mHandler.sendEmptyMessage(CommandIndex.REQUEST);
    }
}
