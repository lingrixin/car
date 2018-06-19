package com.haier.location.api;

import android.content.Context;

/**
 * Created by LRXx on 2017/9/26.
 */

public interface HaierLocationHandler {
    public void init(Context context);
    public void setListener(HaierLocationListener listener);
    public void onStart();
    public boolean isStart();
    public void onStop();
    public void onDestroy();
}
