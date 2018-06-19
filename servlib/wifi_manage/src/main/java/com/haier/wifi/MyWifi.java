package com.haier.wifi;

import android.content.Context;

/**
 * Created by LRXx on 2017/11/30.
 */

public class MyWifi {

    @SuppressWarnings("all")
    public static Context context;

    public static void init(Context context){
        MyWifi.context = context.getApplicationContext();
    }
}
