package net.codercard.car.controler.api;

/**
 * <pre>
 *     @author : Created by Phantom
 *     email : phantom@gradle.top‍
 *     time  : 2018/6/20.
 *     desc  : ControlerApi
 * </pre>
 */
public interface ControlerApi {
    /**
     * 去连接
     */
    public void connect();

    /**
     * 断开连接
     */
    public void disconnect();

    /**
     * 更新指令
     * @param commond
     */
    public void setCommond(String commond);
}
