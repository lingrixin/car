package net.codercard.car.controler.bean;

/**
 * <pre>
 *     @author : Created by Phantom
 *     email : phantom@gradle.top‍
 *     time  : 2018/6/20.
 *     desc  : SocketsConfigInfo
 * </pre>
 */
public class SocketsConfigInfo {
    /**
     * socketIp地址
     */
    private String socketIp;
    /**
     * socket端口号
     */
    private String socketPort;
    /**
     * 转向角
     */
    private String turnAngle;
    /**
     * 单位速度
     */
    private String speed;
    /**
     * 采样时间
     */
    private String samplingInterval;

    public SocketsConfigInfo() {
    }

    public SocketsConfigInfo(String socketIp, String socketPort, String turnAngle, String speed, String samplingInterval) {
        this.socketIp = socketIp;
        this.socketPort = socketPort;
        this.turnAngle = turnAngle;
        this.speed = speed;
        this.samplingInterval = samplingInterval;
    }

    public String getSocketIp() {
        return socketIp;
    }

    public void setSocketIp(String socketIp) {
        this.socketIp = socketIp;
    }

    public String getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(String socketPort) {
        this.socketPort = socketPort;
    }

    public String getTurnAngle() {
        return turnAngle;
    }

    public void setTurnAngle(String turnAngle) {
        this.turnAngle = turnAngle;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getSamplingInterval() {
        return samplingInterval;
    }

    public void setSamplingInterval(String samplingInterval) {
        this.samplingInterval = samplingInterval;
    }
}
