package net.codercard.car.controler.api;

import java.util.Observable;

/**
 * <pre>
 *     @author : Created by Phantom
 *     email : phantom@gradle.top‍
 *     time  : 2018/6/20.
 *     desc  : ProxyObject
 * </pre>
 */
public abstract class ProxyObject extends Observable implements RealTimeCallBack, ControlerApi {
    public abstract void onDestroy();
    public abstract void setConnectStateChange(IConnectStateChange connectStateChange);
}
