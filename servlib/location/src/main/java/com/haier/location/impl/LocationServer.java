package com.haier.location.impl;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by LRXx on 2017-2-27.
 */

class LocationServer {

    private Context context;
    private AMapLocationListener listener;
    private AMapLocationClientOption option;
    private AMapLocationClient locationClient;

    public LocationServer(Context context, AMapLocationClientOption option, AMapLocationListener listener) {
        this.context = context;
        this.option = option;
        this.listener = listener;
        init();
    }

    private void init() {
        locationClient = new AMapLocationClient(context);
        locationClient.setLocationOption(option);
        locationClient.setLocationListener(listener);
    }

    public void start() {
        if (locationClient != null) {
            locationClient.startLocation();
        }
    }

    public boolean isStart() {
        return locationClient != null && locationClient.isStarted();
    }

    public void stop() {
        if (locationClient != null) {
            locationClient.onDestroy();
        }
    }

    public AMapLocation getLast() {
        return locationClient.getLastKnownLocation();
    }
}
