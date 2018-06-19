package com.haier.controler.utils;

import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import static com.haier.controler.api.CommandIndex.*;

/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/8.
 *     @desc  : CommandUtil
 * </pre>
 */
public class CommandUtil {

    private static List<byte[]> commandList = new ArrayList<>();

    static {
        commandList.add(REQUEST, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x01, (byte) 0x00}); //查询命令
        commandList.add(DEVICE_LIGHT_ON, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x04, (byte) 0x00}); //照明灯开
        commandList.add(DEVICE_LIGHT_OFF, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x05, (byte) 0x00}); //照明灯关
        commandList.add(DEVICE_CELSIUS, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x06, (byte) 0x00}); //转换为摄氏度
        commandList.add(DEVICE_FAHRENHEIT, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x07, (byte) 0x00}); //转换为华氏度
        commandList.add(DEVICE_CHU_LU_ON, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x08, (byte) 0x00}); //打开手动除露
        commandList.add(DEVICE_CHU_LU_OFF, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x09, (byte) 0x00}); //关闭手动除露
        commandList.add(DEVICE_FAN_ON, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x0A, (byte) 0x00}); //强制风机开启
        commandList.add(DEVICE_FAN_OFF, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x0B, (byte) 0x00}); //强制风机关闭
        commandList.add(DEVICE_SHOP_ON, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x0C, (byte) 0x00}); //商场演示模式开启
        commandList.add(DEVICE_SHOP_OFF, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x0D, (byte) 0x00}); //商场演示模式关闭
        commandList.add(DEVICE_SABBATH_ON, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x10, (byte) 0x00}); //安息模式开启
        commandList.add(DEVICE_SABBATH_OFF, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x11, (byte) 0x00}); //安息模式关闭
        commandList.add(DEVICE_POWER_ON, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x12, (byte) 0x00}); //电源开启
        commandList.add(DEVICE_POWER_OFF, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x13, (byte) 0x00}); //电源关闭
        commandList.add(DEVICE_CELSIUS_SET_FREEZE, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x06, (byte) 0x01, (byte) 0x5d, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00}); //摄氏度状态下冷藏
        commandList.add(DEVICE_FAHRENHEIT_SET_FREEZE, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x06, (byte) 0x01, (byte) 0x5d, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00}); //华氏度状态
        commandList.add(DEVICE_CODE_REQUEST, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x02, (byte) 0x70, (byte) 0x72}); //识别码查询 识别码长度为32
        commandList.add(REQUES_REAL_STATE, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0xFF, (byte) 0x8d, (byte) 0x01, (byte) 0x00}); //查询实时温度
        commandList.add(DEVICE_HARDWARE_TEST, new byte[]{(byte) 0xAA, (byte) 0x55, (byte) 0x04, (byte) 0x01, (byte) 0x4d, (byte) 0x14, (byte) 0x00}); //硬件检测开启
    }

    private static byte[] addSum(byte[] data) {
        byte mSum = 0;
        for (int i = 2; i < data.length - 1; i++) {
            mSum += data[i];
        }
        data[data.length - 1] = mSum;
        return data;
    }

    public static byte[] getCommend(Message msg) {
        if (msg.what != REQUEST) { //查询命令使用频率高
            //摄氏度状态下箱内设定温度范围【5,20】00对应着 +5℃档
            if (msg.what == DEVICE_CELSIUS_SET_FREEZE) {
                commandList.get(msg.what)[7] = (byte) (checkCenTemp(msg.arg1) - 5);
            } else
                //华氏度状态下箱内设定温度范围【41,68】  00对应着 41℉档
                if (msg.what == DEVICE_FAHRENHEIT_SET_FREEZE) {
                    commandList.get(msg.what)[7] = (byte) (checkFahTemp(msg.arg2) - 41);
                }
        }
        return addSum(commandList.get(msg.what));
    }

    public static int checkCenTemp(int setData) {
        return setData < 5 ? 5 : setData > 20 ? 20 : setData;
    }

    private static int checkFahTemp(int setData) {
        return setData < 41 ? 41 : setData > 68 ? 68 : setData;
    }

}
