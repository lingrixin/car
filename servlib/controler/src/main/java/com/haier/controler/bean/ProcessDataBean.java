package com.haier.controler.bean;

import android.util.Log;

import com.haier.controler.api.CommandIndex;
import com.haier.controler.utils.ControlerLogUtil;

/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/9.
 *     @desc  : ProcessDataBean
 * </pre>
 */
public class ProcessDataBean {

    /**
     * 摄氏度状态下冷藏显示温度
     */
    private int cenColdShowTemp;
    /**
     * 摄氏度状态下环境显示温度
     */
    private int cenEnvShowTemp;
    /**
     * 摄氏度状态下冷藏设置温度
     */
    private int cenColdSetTemp;
    /**
     * 摄氏度状态下冷藏实际温度
     */
    private int cenColdTrueTemp;
    /**
     * 摄氏度状态下环境实际温度
     */
    private int cenEnvTrueTemp;
    /**
     * 华氏度状态下冷藏显示温度
     */
    private int fahColdShowTemp;
    /**
     * 华氏度状态下环温显示温度
     */
    private int fahEnvShowTemp;
    /**
     * 华氏度状态下冷藏设置温度
     */
    private int fahColdSetTemp;
    /**
     * 华氏度状态下冷藏实际温度
     */
    private int fahColdTrueTemp;
    /**
     * 华氏度状态下环境实际温度
     */
    private int fahEnvTrueTemp;
    /**
     * 照明灯开
     */
    private boolean lightOn;
    /**
     * 摄氏度与华氏度转换，摄氏度模式
     */
    private boolean degreeeOn;
    /**
     * 除露开启
     */
    private boolean dewOn;
    /**
     * 开机
     */
    private boolean powerOn;
    /**
     * 冷藏门开
     */
    private boolean coldOn;
    /**
     * 安息日模式
     */
    private boolean sabbathOn;
    /**
     * 商场演示模式
     */
    private boolean shopOn;
    /**
     * 冷藏传感器实际温度
     */
    private int coldSensorTrueTemp;
    /**
     * 防低温传感器实际温度
     */
    private int lowSensorTrueTemp;
    /**
     * 环温传感器实际温度
     */
    private int envSeneorTrueTemp;
    /**
     * 压机状态
     */
    private boolean pressState;
    /**
     * 风机状态
     */
    private boolean airState;
    /**
     * 加热丝状态
     */
    private boolean lowHeatingWireState;
    /**
     * 加热丝状态
     */
    private boolean doorHeatingWireState;
    /**
     * 照明灯状态
     */
    private boolean lightState;
    /**
     * 超时标志位
     */
    private boolean timeOut;
    /**
     * 统计数据头校验失败次数
     */
    private int headerErrorCounts = 0;
    /**
     * 统计数据长度校验失败次数
     */
    private int lengthErrorCounts = 0;
    /**
     * 统计数据校验和失败次数
     */
    private int sumErrorCounts = 0;

    /**
     * 设备识别码
     */
    private String devicesInfo;


/* Error 信息 */
    /**
     * 防低温温度传感器故障
     */
    private boolean temperature_sensor_fault;
    /**
     * 控制传感器故障
     */
    private boolean control_sensor_fault;
    /**
     * 环境温度传感器感故障
     */
    private boolean ambient_sensor_fault;
    /**
     * 防低温报警
     */
    private boolean low_temperature_alarm;
    /**
     * 箱内低温报警
     */
    private boolean low_box_alarm;
    /**
     * 开门报警
     */
    private boolean cold_open;
    /**
     * 通信
     */
    private boolean communicate;


    private String response_code;

    public ProcessDataBean() {
    }


    public int getCenColdShowTemp() {
        return cenColdShowTemp;
    }

    public void setCenColdShowTemp(int cenColdShowTemp) {
        this.cenColdShowTemp = cenColdShowTemp;
    }

    public int getCenEnvShowTemp() {
        return cenEnvShowTemp;
    }

    public void setCenEnvShowTemp(int cenEnvShowTemp) {
        this.cenEnvShowTemp = cenEnvShowTemp;
    }

    public int getCenColdSetTemp() {
        return cenColdSetTemp;
    }

    public void setCenColdSetTemp(int cenColdSetTemp) {
        this.cenColdSetTemp = cenColdSetTemp;
    }

    public int getCenColdTrueTemp() {
        return cenColdTrueTemp;
    }

    public void setCenColdTrueTemp(int cenColdTrueTemp) {
        this.cenColdTrueTemp = cenColdTrueTemp;
    }

    public int getCenEnvTrueTemp() {
        return cenEnvTrueTemp;
    }

    public void setCenEnvTrueTemp(int cenEnvTrueTemp) {
        this.cenEnvTrueTemp = cenEnvTrueTemp;
    }

    public int getFahColdShowTemp() {
        return fahColdShowTemp;
    }

    public void setFahColdShowTemp(int fahColdShowTemp) {
        this.fahColdShowTemp = fahColdShowTemp;
    }

    public int getFahEnvShowTemp() {
        return fahEnvShowTemp;
    }

    public void setFahEnvShowTemp(int fahEnvShowTemp) {
        this.fahEnvShowTemp = fahEnvShowTemp;
    }

    public int getFahColdSetTemp() {
        return fahColdSetTemp;
    }

    public void setFahColdSetTemp(int fahColdSetTemp) {
        this.fahColdSetTemp = fahColdSetTemp;
    }

    public int getFahColdTrueTemp() {
        return fahColdTrueTemp;
    }

    public void setFahColdTrueTemp(int fahColdTrueTemp) {
        this.fahColdTrueTemp = fahColdTrueTemp;
    }

    public int getFahEnvTrueTemp() {
        return fahEnvTrueTemp;
    }

    public void setFahEnvTrueTemp(int fahEnvTrueTemp) {
        this.fahEnvTrueTemp = fahEnvTrueTemp;
    }

    public boolean isLightOn() {
        return lightOn;
    }

    public void setLightOn(boolean lightOn) {
        this.lightOn = lightOn;
    }

    public boolean isDegreeeOn() {
        return degreeeOn;
    }

    public void setDegreeeOn(boolean degreeeOn) {
        this.degreeeOn = degreeeOn;
    }

    public boolean isDewOn() {
        return dewOn;
    }

    public void setDewOn(boolean dewOn) {
        this.dewOn = dewOn;
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    public void setPowerOn(boolean powerOn) {
        this.powerOn = powerOn;
    }

    public boolean isColdOn() {
        return coldOn;
    }

    public void setColdOn(boolean coldOn) {
        this.coldOn = coldOn;
    }

    public boolean isSabbathOn() {
        return sabbathOn;
    }

    public void setSabbathOn(boolean sabbathOn) {
        this.sabbathOn = sabbathOn;
    }

    public boolean isShopOn() {
        return shopOn;
    }

    public void setShopOn(boolean shopOn) {
        this.shopOn = shopOn;
    }

    public int getColdSensorTrueTemp() {
        return coldSensorTrueTemp;
    }

    public void setColdSensorTrueTemp(int coldSensorTrueTemp) {
        this.coldSensorTrueTemp = coldSensorTrueTemp;
    }

    public int getLowSensorTrueTemp() {
        return lowSensorTrueTemp;
    }

    public void setLowSensorTrueTemp(int lowSensorTrueTemp) {
        this.lowSensorTrueTemp = lowSensorTrueTemp;
    }

    public int getEnvSeneorTrueTemp() {
        return envSeneorTrueTemp;
    }

    public void setEnvSeneorTrueTemp(int envSeneorTrueTemp) {
        this.envSeneorTrueTemp = envSeneorTrueTemp;
    }

    public boolean isPressState() {
        return pressState;
    }

    public void setPressState(boolean pressState) {
        this.pressState = pressState;
    }

    public boolean isAirState() {
        return airState;
    }

    public void setAirState(boolean airState) {
        this.airState = airState;
    }

    public boolean isLowHeatingWireState() {
        return lowHeatingWireState;
    }

    public void setLowHeatingWireState(boolean lowHeatingWireState) {
        this.lowHeatingWireState = lowHeatingWireState;
    }

    public boolean isDoorHeatingWireState() {
        return doorHeatingWireState;
    }

    public void setDoorHeatingWireState(boolean doorHeatingWireState) {
        this.doorHeatingWireState = doorHeatingWireState;
    }

    public boolean isLightState() {
        return lightState;
    }

    public void setLightState(boolean lightState) {
        this.lightState = lightState;
    }

    public boolean isTimeOut() {
        return timeOut;
    }

    public void setTimeOut(boolean timeOut) {
        if (timeOut) {
            ControlerLogUtil.i( "通信超时...");
        }
        this.timeOut = timeOut;
    }

    public int getHeaderErrorCounts() {
        return headerErrorCounts;
    }


    public void addHeaderErrorCounts() {
        headerErrorCounts++;
    }

    public int getLengthErrorCounts() {
        return lengthErrorCounts;
    }

    public void addLengthErrorCounts() {
        lengthErrorCounts++;
    }

    public int getSumErrorCounts() {
        return sumErrorCounts;
    }

    public void addSumErrorCounts() {
        sumErrorCounts++;
    }

    public boolean isTemperature_sensor_fault() {
        return temperature_sensor_fault;
    }

    public void setTemperature_sensor_fault(boolean temperature_sensor_fault) {
        this.temperature_sensor_fault = temperature_sensor_fault;
    }

    public boolean isControl_sensor_fault() {
        return control_sensor_fault;
    }

    public void setControl_sensor_fault(boolean control_sensor_fault) {
        this.control_sensor_fault = control_sensor_fault;
    }

    public boolean isAmbient_sensor_fault() {
        return ambient_sensor_fault;
    }

    public void setAmbient_sensor_fault(boolean ambient_sensor_fault) {
        this.ambient_sensor_fault = ambient_sensor_fault;
    }

    public boolean isLow_temperature_alarm() {
        return low_temperature_alarm;
    }

    public void setLow_temperature_alarm(boolean low_temperature_alarm) {
        this.low_temperature_alarm = low_temperature_alarm;
    }

    public boolean isLow_box_alarm() {
        return low_box_alarm;
    }

    public void setLow_box_alarm(boolean low_box_alarm) {
        this.low_box_alarm = low_box_alarm;
    }

    public boolean isCold_open() {
        return cold_open;
    }

    public void setCold_open(boolean cold_open) {
        this.cold_open = cold_open;
    }

    public boolean isCommunicate() {
        return communicate;
    }

    public void setCommunicate(boolean communicate) {
        this.communicate = communicate;
    }

    public String getDevicesInfo() {
        return devicesInfo;
    }

    public void setDevicesInfo(String devicesInfo) {
        this.devicesInfo = devicesInfo;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    @Override
    public String toString() {
        return "ProcessDataBean{" +
                "cenColdShowTemp=" + cenColdShowTemp +
                ", cenEnvShowTemp=" + cenEnvShowTemp +
                ", cenColdSetTemp=" + cenColdSetTemp +
                ", cenColdTrueTemp=" + cenColdTrueTemp +
                ", cenEnvTrueTemp=" + cenEnvTrueTemp +
                ", fahColdShowTemp=" + fahColdShowTemp +
                ", fahEnvShowTemp=" + fahEnvShowTemp +
                ", fahColdSetTemp=" + fahColdSetTemp +
                ", fahColdTrueTemp=" + fahColdTrueTemp +
                ", fahEnvTrueTemp=" + fahEnvTrueTemp +
                ", lightOn=" + lightOn +
                ", degreeeOn=" + degreeeOn +
                ", dewOn=" + dewOn +
                ", powerOn=" + powerOn +
                ", coldOn=" + coldOn +
                ", sabbathOn=" + sabbathOn +
                ", shopOn=" + shopOn +
                ", coldSensorTrueTemp=" + coldSensorTrueTemp +
                ", lowSensorTrueTemp=" + lowSensorTrueTemp +
                ", envSeneorTrueTemp=" + envSeneorTrueTemp +
                ", pressState=" + pressState +
                ", airState=" + airState +
                ", lowHeatingWireState=" + lowHeatingWireState +
                ", doorHeatingWireState=" + doorHeatingWireState +
                ", lightState=" + lightState +
                ", timeOut=" + timeOut +
                ", headerErrorCounts=" + headerErrorCounts +
                ", lengthErrorCounts=" + lengthErrorCounts +
                ", sumErrorCounts=" + sumErrorCounts +
                ", devicesInfo='" + devicesInfo + '\'' +
                ", temperature_sensor_fault=" + temperature_sensor_fault +
                ", control_sensor_fault=" + control_sensor_fault +
                ", ambient_sensor_fault=" + ambient_sensor_fault +
                ", low_temperature_alarm=" + low_temperature_alarm +
                ", low_box_alarm=" + low_box_alarm +
                ", cold_open=" + cold_open +
                ", communicate=" + communicate +
                '}';
    }
}
