package com.haier.wifi.base;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;


/**
 * Created by LRXx on 2017/11/30.
 */

public class ViewHelper {
    @SuppressWarnings("unchecked")
    public static <V extends View> V f(Activity activity,@IdRes int resId){
        return (V) activity.findViewById(resId);
    }
}
