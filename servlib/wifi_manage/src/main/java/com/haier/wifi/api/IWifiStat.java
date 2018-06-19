package com.haier.wifi.api;


import com.haier.wifi.api.bean.WifiStatEnum;

/**
 * <p class="note"></p>
 * created by LRXx at 2017-8-2
 */
public interface IWifiStat  {
    void WifiStatChange(WifiStatEnum statEnum);
}
