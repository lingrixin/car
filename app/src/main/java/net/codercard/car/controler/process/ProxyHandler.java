package net.codercard.car.controler.process;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import net.codercard.car.controler.api.IConnectStateChange;
import net.codercard.car.controler.api.ProxyObject;
import net.codercard.car.controler.bean.CommandIndex;


/**
 * <pre>
 *     @author : Created by Phantom
 *     email : phantom@gradle.top‍
 *     time  : 2018/6/20.
 *     desc  : ProxyHandler
 * </pre>
 */
public class ProxyHandler extends ProxyObject {
    private BeatsHandler beatsHandler;
    private Handler handler;

    public ProxyHandler(BeatsHandler beatsHandler) {
        this.beatsHandler = beatsHandler;
        this.beatsHandler.setRealListener(this);
        handler = beatsHandler.getmHandler();
    }

    @Override
    public void connect() {
        Log.i("phan", "发送了连接");
        handler.sendEmptyMessage(CommandIndex.CONNECT);
    }

    @Override
    public void disconnect() {
        handler.sendEmptyMessage(CommandIndex.DISCONNECT);
    }

    @Override
    public void setCommond(String commond) {
        Message msg = Message.obtain(handler);
        msg.what = CommandIndex.SENT_CMD;
        msg.obj = commond;
        handler.sendMessage(msg);
    }

    @Override
    public void notice() {
        if (countObservers() > 0) {
            //及时通知
            setChanged();
            notifyObservers();
        }
    }

    @Override
    public void onDestroy() {
        beatsHandler = null;
    }

    @Override
    public void setConnectStateChange(IConnectStateChange connectStateChange) {
        beatsHandler.setConnectStateChange(connectStateChange);
    }
}
