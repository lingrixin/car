package com.haier.controler.process;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.example.shining.libutils.utilslib.data.SpUtils;
import com.haier.controler.api.CommandIndex;
import com.haier.controler.api.ProxyObject;
import com.haier.controler.utils.ControlerLogUtil;

import static com.haier.controler.api.CommandIndex.SET_BOX_TEMP_DEFAULT;


/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/9.
 *     @desc  : RealTimeCheckHandler 检验线程
 * </pre>
 */
public class RealTimeCheckHandler {
    private ProxyObject proxyObject;
    private Context context;
    private HandlerThread kernelThread;
    private static Handler mHandler;
    private int spTemp, realTemp;
    private boolean spLight, realLight;


    public RealTimeCheckHandler(ProxyObject proxyObject, Context context) {
        this.proxyObject = proxyObject;
        this.context = context;
        init();
    }

    private void init() {
        kernelThread = new HandlerThread("BonresumeeatsThread");
        kernelThread.start();
        mHandler = new Handler(kernelThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                ControlerLogUtil.i( "CheckThread...");
                //校验模式是否正确
                if (!proxyObject.getWineState().isDewOn()) {//校验手动除露
                    ControlerLogUtil.i( "正在打开除露模式中...");
                    proxyObject.openChuLu();
                }
                if (proxyObject.getWineState().isSabbathOn()) {//校验安息模式
                    ControlerLogUtil.i( "正在关闭安息模式中...");
                    proxyObject.closeSabbath();
                }
                if (!proxyObject.getWineState().isPowerOn()) {//校验电源开关
                    ControlerLogUtil.i( "正在打开电源中...");
                    proxyObject.openPower();
                }
                if (!proxyObject.getWineState().isDegreeeOn()) {//校验摄氏度模式
                    ControlerLogUtil.i( "正在转换为摄氏度模式中...");
                    proxyObject.changeCelsius();
                }
                //拿sp
                spTemp = (int) SpUtils.getInstance(context).get(CommandIndex.KEY_BOX_TEMP, SET_BOX_TEMP_DEFAULT);
                spLight = (boolean) SpUtils.getInstance(context).get(CommandIndex.KEY_LIGHT_STATE, false);
                //拿当前状态
                realTemp = proxyObject.getWineState().getCenColdSetTemp();
                realLight = proxyObject.getWineState().isLightOn();
                //比较
                if (spTemp != realTemp) {
                    ControlerLogUtil.i( "温度校验中...Sp温度为：" + spTemp + "...查询温度为：" + realTemp);
                    proxyObject.celsiusSetFreeze(spTemp);
                }
                if (spLight != realLight) {
                    ControlerLogUtil.i( "灯光校验中...sp灯光为：" + spLight + "...查询灯光为：" + realLight);
                    if (spLight) {
                        proxyObject.openLight();
                    } else {
                        proxyObject.closeLight();
                    }
                }
                sendEmptyMessageDelayed(CommandIndex.REQUEST, CommandIndex.CHECK_DELAY);
            }
        };
        mHandler.sendEmptyMessage(CommandIndex.REQUEST);
    }

    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        kernelThread.quit();
        ControlerLogUtil.i("RealTimeCheckHandler onDestroy...");
    }
}
