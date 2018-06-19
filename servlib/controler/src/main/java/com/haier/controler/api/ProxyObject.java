package com.haier.controler.api;

import java.util.Observable;

/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/13.
 *     @desc  : ProxyObject
 * </pre>
 */
public abstract class ProxyObject extends Observable implements RealTimeCallBack, ControlerApi {
    public abstract void onDestroy();
    public abstract void setConnectStateChange(IConnectStateChange connectStateChange);
}
