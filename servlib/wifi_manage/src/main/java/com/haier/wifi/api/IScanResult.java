package com.haier.wifi.api;

import android.net.wifi.ScanResult;
import android.support.v4.util.Pair;

import java.util.List;

/**
 * <p class="note"></p>
 * created by LRXx at 2017-8-1
 */
public interface IScanResult {
    void toResult(List<Pair<Integer, ScanResult>> results, boolean isConnecting,int currentId);
}
