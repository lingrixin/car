package net.codercard.car.controler.api;

/**
 * <pre>
 *     @author : Created by Phantom
 *     email : phantom@gradle.top‍
 *     time  : 2018/6/20.
 *     desc  : IConnectStateChange
 * </pre>
 */
public interface IConnectStateChange {
    /**
     * 连接成功
     */
    public void connectOk();

    /**
     * 连接中断
     */
    public void connectFailure();
}
