package com.haier.controler.utils;

import android.text.TextUtils;

import com.haier.controler.api.CommandIndex;

/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/8.
 *     @desc  : PrintUtil
 * </pre>
 */
public class PrintUtil {
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static final char S = ' ';

    public static String BytesToString(byte[] bytes, int length) {
        if (bytes == null) {
            return "not connected main board! ";
        }
        char[] hexChars = new char[length * 3];
        for (int j = 0; j < length; j++) {
            hexChars[j * 3] = hexArray[(bytes[j] & 0xFF) >>> 4];
            hexChars[j * 3 + 1] = hexArray[bytes[j] & 0x0F];
            hexChars[j * 3 + 2] = S;
        }
        return new String(hexChars);
    }

    public static String BytesToString(byte[] bytes) {
        return BytesToString(bytes, bytes.length);
    }

    public static String getStr(boolean tag) {
        return tag ? CommandIndex.OPEN_STR : CommandIndex.CLOSE_STR;
    }

    public static String calcTemp(int i) {
        return addTempTag(i + 5);
    }

    public static String addTempTag(int i) {
        return i + CommandIndex.TEMP_TAG;
    }
}
