package com.haier.location.manage;

import android.content.Context;

import com.haier.location.api.HaierLocationHandler;
import com.haier.location.api.HaierLocationListener;
import com.haier.location.impl.Amapimpl;


/**
 * Created by LHaierx on 2017/9/26.
 * 定位逻辑内部管理类
 * 负责控制整体定位线程
 * 1、一次请求只有一次回应
 * 2、上次请求未结束不进行下一次回应
 * 3、
 */

public class InsideManage {

    private static volatile InsideManage instance;
    private HaierLocationHandler handler;
    private Context context;

    private InsideManage(Context context) {
        this.context = context;
    }

    public static InsideManage init(Context context) {
        if (instance == null) {
            synchronized (InsideManage.class) {
                instance = instance == null ? new InsideManage(context.getApplicationContext()) : instance;
            }
        }
        return instance;
    }

    public void getLocation(HaierLocationListener listener) {
        handler = useAMap();
        handler.init(context);
        handler.setListener(listener);
        if (!handler.isStart()) {
            handler.onStart();
        }
    }

    /*
     * 使用高德地图
     * @param listener
     */
    private HaierLocationHandler useAMap() {
        return Amapimpl.getInstance();
    }

    /*
     * 使用百度地图
     * @param listener
     */
    private HaierLocationHandler useBaidu() {
        return null;
    }

}
