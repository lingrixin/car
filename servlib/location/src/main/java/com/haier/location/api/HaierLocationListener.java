package com.haier.location.api;

import com.haier.location.bean.HaierLocationBean;

/**
 * Created by LRXx on 2017/9/26.
 */

public interface HaierLocationListener {
    public void onLocating();
    public void onLocationSuccess(HaierLocationBean rxLocationBean);
    public void onLocationFailed(int reason);
}
