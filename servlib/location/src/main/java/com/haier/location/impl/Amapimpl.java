package com.haier.location.impl;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.haier.location.api.HaierLocationHandler;
import com.haier.location.api.HaierLocationListener;
import com.haier.location.bean.HaierLocationBean;
import com.haier.location.bean.HaierLocationErrorCode;

/**
 * Created by LRXx on 2017/9/26.
 */

public class Amapimpl implements HaierLocationHandler {

    private AMapLocationClient mClient;
    private HaierLocationListener clientListener;
    private LocationServer server;

    private static volatile Amapimpl instance;

    public static Amapimpl getInstance() {
        if (instance == null) {
            synchronized (Amapimpl.class) {
                instance = instance == null ? new Amapimpl() : instance;
            }
        }
        return instance;
    }

    @Override
    public void init(Context context) {
        server = new LocationServer(context, getOption(), listener);
    }

    private AMapLocationClientOption getOption() {
        return new AMapLocationClientOption().setOnceLocation(true);
    }

    private AMapLocationListener listener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (null != aMapLocation && aMapLocation.getErrorCode() == 0
                    && aMapLocation.getLatitude() != 0.0 && aMapLocation.getLongitude() != 0.0) {
                if (clientListener != null) {
                    HaierLocationBean bean = new HaierLocationBean();
                    bean.setProvince(aMapLocation.getProvince());
                    bean.setAreaInterestName(aMapLocation.getAoiName());
                    bean.setPointInterestName(aMapLocation.getPoiName());
                    bean.setCity(aMapLocation.getCity());
                    bean.setAdCode(aMapLocation.getAdCode());
                    bean.setCityCode(aMapLocation.getCityCode());
                    bean.setDistrict(aMapLocation.getDistrict());
                    bean.setLatitude(aMapLocation.getLatitude());
                    bean.setLongitude(aMapLocation.getLongitude());
                    bean.setStreet(aMapLocation.getStreet());
                    bean.setAddress(aMapLocation.getAddress());
                    clientListener.onLocationSuccess(bean);
                }
            } else {
                if (clientListener != null) {
                    clientListener.onLocationFailed(HaierLocationErrorCode.ERROR_UNKNOWN);
                }
            }
            onDestroy();
        }
    };

    @Override
    public void setListener(HaierLocationListener listener) {
        clientListener = listener;
    }

    @Override
    public void onStart() {
        if (clientListener != null) {
            clientListener.onLocating();
        }
        server.start();
    }

    @Override
    public boolean isStart() {
        return server.isStart();
    }


    @Override
    public void onStop() {
        mClient.stopLocation();
        onDestroy();
    }

    @Override
    public void onDestroy() {
        mClient.onDestroy();
        clientListener = null;
    }
}
