package com.haier.location.adapter;

import com.haier.location.api.HaierLocationListener;
import com.haier.location.bean.HaierLocationBean;

/**
 * Created by LRXx on 2018/1/5.
 */

public abstract class LocationListenerAdapter  implements HaierLocationListener{
    @Override
    public void onLocating() {

    }

    @Override
    public void onLocationFailed(int reason) {

    }
}
