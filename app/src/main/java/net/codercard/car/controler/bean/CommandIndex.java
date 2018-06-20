package net.codercard.car.controler.bean;

/**
 * <pre>
 *     @author : Created by Phantom
 *     email : phantom@gradle.top‍
 *     time  : 2018/6/20.
 *     desc  : CommandIndex
 * </pre>
 */
public interface CommandIndex {
    //去连接
    public static final int CONNECT = 0;
    //断开连接
    public static final int DISCONNECT = 1;
    //查询连接状态
    public static final int IS_CONNECTED = 2;
    //发送命令
    public static final int SENT_CMD = 3;
    //连接查询间隔
    public static final long REQUEST_SPEED = 1000;
}
