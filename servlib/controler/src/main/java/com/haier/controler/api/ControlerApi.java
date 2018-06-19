package com.haier.controler.api;

import com.haier.controler.bean.ProcessDataBean;

/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/13.
 *     @desc  : ControlerApi
 * </pre>
 */
public interface ControlerApi {
    //获取当前状态集
    public ProcessDataBean getWineState();

    //照明灯开
    public void openLight();

    //照明灯关
    public void closeLight();

    //转换为摄氏度
    public void changeCelsius();

    //打开手动除露
    public void openChuLu();

    //关闭手动除露
    public void closeChuLu();

    //商场演示模式开启
    public void openShop();

    //商场演示模式关闭
    public void closeShop();

    //安息模式开启
    public void openSabbath();

    //安息模式关闭
    public void closeSabbath();

    //电源开启
    public void openPower();

    //电源关闭
    public void closePower();

    //摄氏度状态下冷藏设置温度
    public void celsiusSetFreeze(int temp);

    //查询实时温度
    public void requesRealState();
}
