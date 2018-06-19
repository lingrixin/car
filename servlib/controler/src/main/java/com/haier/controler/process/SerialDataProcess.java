package com.haier.controler.process;

import android.util.Log;

import com.haier.controler.api.CommandIndex;
import com.haier.controler.api.RealTimeCallBack;
import com.haier.controler.bean.ProcessDataBean;
import com.haier.controler.utils.ControlerLogUtil;

/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/8.
 *     @desc  : SerialDataProcess
 * </pre>
 */
public class SerialDataProcess {
    private byte[] receiveBuff;
    private int length;
    private byte sum;
    private ProcessDataBean processDataBean;

    private static final byte PACKAGE_HEAD_0 = (byte) 0xAA;
    private static final byte PACKAGE_HEAD_1 = (byte) 0x55;
    private RealTimeCallBack realListener;

    public void setRealListener(RealTimeCallBack realListener) {
        this.realListener = realListener;
    }

    SerialDataProcess(ProcessDataBean processDataBean) {
        this.processDataBean = processDataBean;
    }

    public void processData(byte[] receiveBuff, int length, String response_code) {
        this.length = length;
        this.receiveBuff = receiveBuff;
        processDataBean.setResponse_code(response_code);
        //先判断数据有效性
        //1包头
        if (!isFrameHeader()) {
            processDataBean.addHeaderErrorCounts();
            return;
        }
        //2、第三位长度的大小，是否和length一致
        if (!isFrameLenth()) {
            processDataBean.addLengthErrorCounts();
            return;
        }
        //3、校验和
        if (!isSumCheck()) {
            processDataBean.addSumErrorCounts();
            return;
        }
        //解析
        parseData();
    }

    private void parseData() {
        switch (receiveBuff[3]) {
            case (byte) 0x02: //用户状态帧
                parseUser();
                break;
            case (byte) 0x03: //无效帧
                parseInvalid();
                break;
            case (byte) 0x71: //设备识别码查询应答帧
                parseTypeId();
                break;
            case (byte) 0xff: //设备调试信息
                parseDebug();
                break;
            default:
                ControlerLogUtil.e( "parse type unknown!!");
                break;
        }
    }

    private void parseUser() {
        processDataBean.setCenColdShowTemp(receiveBuff[4 + 2]);//摄氏度状态下冷藏显示温度 (1byte)
        processDataBean.setCenEnvShowTemp(receiveBuff[5 + 2] - 9);  //摄氏度状态下环境显示温度 (1byte)
        processDataBean.setCenColdSetTemp(receiveBuff[6 + 2] + 5);  //摄氏度状态下冷藏设置温度 (1byte)
        processDataBean.setCenColdTrueTemp(receiveBuff[7 + 2] - 25);      //摄氏度状态下冷藏实际温度 (1byte)
        processDataBean.setCenEnvTrueTemp(receiveBuff[8 + 2] - 25); //摄氏度状态下环境实际温度 (1byte)

        processDataBean.setFahColdShowTemp(receiveBuff[9 + 2] + 32);   //华氏度状态下冷藏显示温度 (1byte)
        processDataBean.setFahEnvShowTemp(receiveBuff[10 + 2] + 16);    //华氏度状态下环温显示温度 (1byte)
        processDataBean.setFahColdSetTemp(receiveBuff[11 + 2] + 41);     //华氏度状态下冷藏设置温度 (1byte)
        processDataBean.setFahColdTrueTemp(receiveBuff[12 + 2] - 13);        //华氏度状态下冷藏实际温度 (1byte)
        processDataBean.setFahEnvTrueTemp(receiveBuff[13 + 2] - 13);    //华氏度状态下环境实际温度 (1byte)

        processDataBean.setLightOn((receiveBuff[17 + 2] & 0x01) == 0x01);        //灯开关
        /*为“1”时，表示“摄氏度与华氏度转换，摄氏度模式”
            为“0”时，表示“摄氏度与华氏度转换，华氏度模式”*/
        processDataBean.setDegreeeOn((receiveBuff[17 + 2] & 0x02) == 0x02);
        processDataBean.setDewOn((receiveBuff[17 + 2] & 0x04) == 0x04);          //除露模式开关
        processDataBean.setPowerOn((receiveBuff[17 + 2] & 0x08) == 0x08);        //电源开关

        processDataBean.setColdOn((receiveBuff[17 + 2] & 0x10) == 0x10);         //冷藏门开关
        processDataBean.setSabbathOn((receiveBuff[17 + 2] & 0x20) == 0x20);      //安息日模式开关
        processDataBean.setShopOn((receiveBuff[17 + 2] & 0x40) == 0x40);         //商场演示模式开关

        processDataBean.setTemperature_sensor_fault((receiveBuff[29 + 2] & 0x01) == 0x01);          //防低温温度传感器故障
        processDataBean.setControl_sensor_fault((receiveBuff[29 + 2] & 0x02) == 0x02);      //控制传感器故障
        processDataBean.setAmbient_sensor_fault((receiveBuff[29 + 2] & 0x04) == 0x04);          //环境温度传感器感故障
        processDataBean.setLow_temperature_alarm((receiveBuff[29 + 2] & 0x08) == 0x08);        //防低温报警
        processDataBean.setLow_box_alarm((receiveBuff[29 + 2] & 0x10) == 0x10);         //箱内低温报警
        processDataBean.setCold_open((receiveBuff[29 + 2] & 0x20) == 0x20);      //开门报警
        //解析完成 发送通知
        if (realListener != null) {
            realListener.notice();
        }
    }

    private void parseInvalid() {
        switch (receiveBuff[5]) {
            case (byte) 0x01:
                ControlerLogUtil.e( "档位超限");
                break;
            case (byte) 0x02:
                ControlerLogUtil.e( "摄氏度模式，不能调节华氏度冷藏档位");
                break;
            case (byte) 0x03:
                ControlerLogUtil.e( "华氏度模式，不能调节摄氏度冷藏档位");
                break;
            case (byte) 0x04:
                ControlerLogUtil.e( "电源关闭时，不可调节档位等信息");
                break;
            case (byte) 0x05:
                ControlerLogUtil.e( "安息日模式，不可调节照明灯等信息");
                break;
            default:
                ControlerLogUtil.e( "无效命令");
                break;
        }
    }

    private void parseTypeId() {
        StringBuilder stringBuffer = new StringBuilder();//by holy
        for (int counts = 0; counts < length; counts++) {//by holy
            stringBuffer.append(String.format("%02x", receiveBuff[counts]));//by holy
        }
        processDataBean.setDevicesInfo(stringBuffer.toString());
    }

    private void parseDebug() {
        processDataBean.setColdSensorTrueTemp(receiveBuff[4 + 2]);      // 冷藏传感器实际温度:华氏度(1byte) 4
        processDataBean.setLowSensorTrueTemp(receiveBuff[5 + 2]);  //防低温传感器实际温度：华氏度(1byte) 5
        processDataBean.setEnvSeneorTrueTemp(receiveBuff[6 + 2]);  //环温传感器实际温度：华氏度(1byte) 6
        processDataBean.setPressState((receiveBuff[14 + 2] & 0x01) == 0x01);  //压机状态
        processDataBean.setLowHeatingWireState((receiveBuff[15 + 2] & 0x01) == 0x01);  //低温补偿加热丝
        processDataBean.setDoorHeatingWireState((receiveBuff[15 + 2] & 0x02) == 0x02);  //门体除露加热丝
        processDataBean.setLightState((receiveBuff[16 + 2] & 0x01) == 0x01);  //照明灯
        processDataBean.setAirState((receiveBuff[17 + 2] & 0x01) == 0x01);  //蒸发风机
    }


    private boolean isFrameHeader() {
        return receiveBuff[0] == PACKAGE_HEAD_0 && receiveBuff[1] == PACKAGE_HEAD_1;
    }

    private boolean isFrameLenth() {
        return receiveBuff[2] == (length - 3);
    }

    private boolean isSumCheck() {
        sum = 0;
        for (int i = 2; i < length - 1; i++) {
            sum += receiveBuff[i];
        }
        return sum == receiveBuff[length - 1];
    }


}
