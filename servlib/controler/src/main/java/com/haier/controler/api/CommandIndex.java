package com.haier.controler.api;

/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/8.
 *     @desc  : CommandIndex
 * </pre>
 */
public interface CommandIndex {

    public static final String TAG = "controler"; // Log固定前缀

    public static final String KEY_BOX_TEMP = "set_box_temp"; //设置酒柜温度值
    public static final String KEY_LIGHT_STATE = "set_light_state";//设置灯光开关值

    public static final long BEATS_DELAY = 5_00L;//心跳延迟时间
    public static final int CHECK_DELAY = 10_000; //10秒

    public static final int SET_BOX_TEMP_DEFAULT = 10;

    public static final String OPEN_STR = "开";
    public static final String CLOSE_STR = "关";

    public static final String TEMP_TAG = "℃";

    public static final int REQUEST = 0;//查询命令
    public static final int DEVICE_LIGHT_ON = 1;//照明灯开
    public static final int DEVICE_LIGHT_OFF = 2;//照明灯关
    public static final int DEVICE_CELSIUS = 3;//转换为摄氏度
    public static final int DEVICE_FAHRENHEIT = 4;//转换为华氏度
    public static final int DEVICE_CHU_LU_ON = 5;//打开手动除露
    public static final int DEVICE_CHU_LU_OFF = 6;//关闭手动除露
    public static final int DEVICE_FAN_ON = 7;//强制风机开启
    public static final int DEVICE_FAN_OFF = 8;//强制风机关闭
    public static final int DEVICE_SHOP_ON = 9;//商场演示模式开启
    public static final int DEVICE_SHOP_OFF = 10;//商场演示模式关闭
    public static final int DEVICE_SABBATH_ON = 11;//安息模式开启
    public static final int DEVICE_SABBATH_OFF = 12;//安息模式关闭
    public static final int DEVICE_POWER_ON = 13;//电源开启
    public static final int DEVICE_POWER_OFF = 14;//电源关闭
    public static final int DEVICE_CELSIUS_SET_FREEZE = 15;//摄氏度状态下冷藏设置温度
    public static final int DEVICE_FAHRENHEIT_SET_FREEZE = 16;//华氏度状态下冷藏设置温度
    public static final int DEVICE_CODE_REQUEST = 17;//识别码查询 识别码长度为32个Byte
    public static final int REQUES_REAL_STATE = 18;//查询实时温度
    public static final int DEVICE_HARDWARE_TEST = 19;//硬件检测开启

    public static final int OPEN_DOOR_ALARM = 20;//开门告警

    public static final int OPEN_DOOR_DELAY = 60_000; //告警延迟

    public static final String BROADCAST_ACTION_DOOR_OPEN_ALARM = "com.haiersmart.dooropenalarm";//开门报警
    public static final String BROADCAST_ACTION_DOOR_OPEN = "com.haiersmart.dooropen";//开门
    public static final String BROADCAST_ACTION_DOOR_CLOSE = "com.haiersmart.doorclose";//关门

}
