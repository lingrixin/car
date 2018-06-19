package com.haier.wifi.api;

/**
 * <p class="note"></p>
 * created by LRXx at 2017-8-2
 */
public interface IConnectRealTime {
    /**
     * ASSOCIATED
     * ASSOCIATING
     * AUTHENTICATING
     * COMPLETED
     * DISCONNECTED
     * DORMANT
     * FOUR_WAY_HANDSHAKE
     * GROUP_HANDSHAKE
     * INACTIVE
     * INTERFACE_DISABLED
     * INVALID
     * SCANNING
     * UNINITIALIZED
     */

    void onDisconnected();//断开连接
    void onScanning();//扫描中
    void onAssociating();//关联中
    void onAuthenticating();//认证中
    void WrongPassword();//密码错误
    void onFourWayHandshake();//四次握手中
    void onGroupHandshake();//分组握手
    void onDhcping();//正在获取Ip地址
    void onCompleted();//连接成功
}
