package com.haier.controler.process;

import android.content.Context;
import android.os.Message;

import com.example.shining.libutils.utilslib.data.SpUtils;
import com.haier.controler.api.CommandIndex;
import com.haier.controler.api.IConnectStateChange;
import com.haier.controler.api.ProxyObject;
import com.haier.controler.bean.ProcessDataBean;
import com.haier.controler.utils.CommandUtil;

/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/9.
 *     @desc  : ProxyHandler 控制代理类
 *
 *
 * </pre>
 */
public class ProxyHandler extends ProxyObject {
    private BeatsHandler beatsHandler;
    private Context context;

    public ProxyHandler(BeatsHandler beatsHandler, Context context) {
        this.context = context;
        this.beatsHandler = beatsHandler;
        this.beatsHandler.setRealTimeCallback(this);
    }

    @Override
    public ProcessDataBean getWineState() {
        return beatsHandler.getProcessDataBean();
    }

    //照明灯开
    @Override
    public void openLight() {
        beatsHandler.getHandler().sendEmptyMessage(CommandIndex.DEVICE_LIGHT_ON);
        SpUtils.getInstance(context).put(CommandIndex.KEY_LIGHT_STATE, true);
    }

    //照明灯关
    @Override
    public void closeLight() {
        beatsHandler.getHandler().sendEmptyMessage(CommandIndex.DEVICE_LIGHT_OFF);
        SpUtils.getInstance(context).put(CommandIndex.KEY_LIGHT_STATE, false);
    }

    //转换为摄氏度
    @Override
    public void changeCelsius() {
        beatsHandler.getHandler().sendEmptyMessage(CommandIndex.DEVICE_CELSIUS);
    }

    //打开手动除露
    @Override
    public void openChuLu() {
        beatsHandler.getHandler().sendEmptyMessage(CommandIndex.DEVICE_CHU_LU_ON);
    }

    //关闭手动除露
    @Override
    public void closeChuLu() {
        beatsHandler.getHandler().sendEmptyMessage(CommandIndex.DEVICE_CHU_LU_OFF);
    }

    //商场演示模式开启
    @Override
    public void openShop() {
        beatsHandler.getHandler().sendEmptyMessage(CommandIndex.DEVICE_SHOP_ON);
    }

    //商场演示模式关闭
    @Override
    public void closeShop() {
        beatsHandler.getHandler().sendEmptyMessage(CommandIndex.DEVICE_SHOP_OFF);
    }

    //安息模式开启
    @Override
    public void openSabbath() {
        beatsHandler.getHandler().sendEmptyMessage(CommandIndex.DEVICE_SABBATH_ON);
    }

    //安息模式关闭
    @Override
    public void closeSabbath() {
        beatsHandler.getHandler().sendEmptyMessage(CommandIndex.DEVICE_SABBATH_OFF);
    }

    //电源开启
    @Override
    public void openPower() {
        beatsHandler.getHandler().sendEmptyMessage(CommandIndex.DEVICE_POWER_ON);
    }

    //电源关闭
    @Override
    public void closePower() {
        beatsHandler.getHandler().sendEmptyMessage(CommandIndex.DEVICE_POWER_OFF);
    }

    //摄氏度状态下冷藏设置温度
    @Override
    public void celsiusSetFreeze(int temp) {
        Message msg = beatsHandler.getHandler().obtainMessage();
        msg.what = CommandIndex.DEVICE_CELSIUS_SET_FREEZE;
        msg.arg1 = CommandUtil.checkCenTemp(temp);
        SpUtils.getInstance(context).put(CommandIndex.KEY_BOX_TEMP, msg.arg1);
        beatsHandler.getHandler().sendMessage(msg);
    }

    //查询实时温度
    @Override
    public void requesRealState() {
        beatsHandler.getHandler().sendEmptyMessage(CommandIndex.REQUES_REAL_STATE);
    }


    @Override
    public void notice() {
        if (countObservers() > 0) {
            //及时通知
            setChanged();
            notifyObservers();
        }
    }

    @Override
    public void onDestroy(){
        beatsHandler = null;
    }

    @Override
    public void setConnectStateChange(IConnectStateChange connectStateChange) {
        beatsHandler.setConnectStateChange(connectStateChange);
    }

}
