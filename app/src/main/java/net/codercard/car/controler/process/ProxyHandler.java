package net.codercard.car.controler.process;

import android.content.Context;

import net.codercard.car.controler.api.IConnectStateChange;
import net.codercard.car.controler.api.ProxyObject;

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
    private Context context;

    public ProxyHandler(BeatsHandler beatsHandler, Context context){
        this.context = context;
        this.beatsHandler = beatsHandler;
        this.beatsHandler.setRealListener(this);
    }

    @Override
    public void connect() {
        beatsHandler.connect();
    }

    @Override
    public void disconnect() {
        beatsHandler.disconnect();
    }

    @Override
    public void setCommond(String commond) {

    }

    @Override
    public void notice() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setConnectStateChange(IConnectStateChange connectStateChange) {

    }
}
