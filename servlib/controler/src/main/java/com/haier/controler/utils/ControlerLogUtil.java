package com.haier.controler.utils;

import android.util.Log;

import com.haier.controler.api.CommandIndex;

/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/13.
 *     @desc  : ControlerLogUtil
 * </pre>
 */
public class ControlerLogUtil {
    public static final boolean on = true;

    public static void i(String str) {
        if (on) {
            Log.i(CommandIndex.TAG, str);
        }
    }

    public static void e(String str) {
        if (on) {
            Log.e(CommandIndex.TAG, str);
        }
    }
}
