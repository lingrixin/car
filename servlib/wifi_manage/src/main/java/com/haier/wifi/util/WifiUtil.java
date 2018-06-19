package com.haier.wifi.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;

import com.haier.wifi.MyWifi;
import com.haier.wifi.api.bean.RssiStatEnum;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WifiUtil {

    private static long lastSwitchTime = 0L;
    private static final long SWITCH_DURATION = 10 * 60 * 1000L;//10分钟间隔时间
    private static final String TAG = WifiUtil.class.getSimpleName();
    public static final int NO_SAVE_PASSWORD = -1;

    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    public static DhcpInfo getDhcpInfo(Context mContext) {

        WifiManager wm = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        DhcpInfo di = wm.getDhcpInfo();

        return di;
    }

    public static String long2ip(long ip) {

        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf((int) (ip & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((ip >> 8) & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((ip >> 16) & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((ip >> 24) & 0xff)));
        return sb.toString();
    }

    public static WifiCipherType getWifiCipher(String capability) {

        String cipher = getEncryptString(capability);

        if (cipher.contains("WEP")) {

            return WifiCipherType.WIFICIPHER_WEP;
        } else if (cipher.contains("WPA") || cipher.contains("WPA2") || cipher.contains("WPS")) {

            return WifiCipherType.WIFICIPHER_WPA;
        } else if (cipher.contains("unknow")) {

            return WifiCipherType.WIFICIPHER_INVALID;
        } else {
            return WifiCipherType.WIFICIPHER_NOPASS;
        }
    }

    public static String getEncryptString(String capability) {


        StringBuilder sb = new StringBuilder();

        if (TextUtils.isEmpty(capability))
            return "unknow";

        if (capability.contains("WEP")) {

            sb.append("WEP");

            return sb.toString();
        }

        if (capability.contains("WPA")) {

            sb.append("WPA");

        }
        if (capability.contains("WPA2")) {

            sb.append("/");

            sb.append("WPA2");

        }

        if (capability.contains("WPS")) {

            sb.append("/");

            sb.append("WPS");

        }

        if (TextUtils.isEmpty(sb))
            return "OPEN";

        return sb.toString();
    }

    public static List<WifiConfiguration> getConfigurations(Context mContext) {

        WifiManager wm = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        List<WifiConfiguration> mList = wm.getConfiguredNetworks();

        return mList;
    }

    public static boolean removeWifi(Context mContext, int networkId) {
        if (mContext == null) {
            return false;
        }
        WifiManager wm = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return false;
        }
        return wm.removeNetwork(networkId);

    }

    public static boolean addNetWork(WifiConfiguration cfg, Context mContext) {
        WifiManager wm = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo mInfo = wm.getConnectionInfo();

        if (mInfo != null) {

            wm.disableNetwork(mInfo.getNetworkId());
//			wm.disconnect();
        }

        boolean flag = false;


        if (cfg.networkId > 0) {

//			Log.d(WifiUtil.class.getSimpleName(), "cfg networkId = " + cfg.networkId);

            flag = wm.enableNetwork(cfg.networkId, true);

            wm.updateNetwork(cfg);
        } else {

            int netId = wm.addNetwork(cfg);


//			Log.d(WifiUtil.class.getSimpleName(), "after adding netId = " + netId);

            if (netId > 0) {
                wm.saveConfiguration();
                flag = wm.enableNetwork(netId, true);
            } else {
//				ToastUtil.showToastShort("connect err");
            }
        }

        return flag;
    }

    public static void addNetWorkThread(final WifiConfiguration cfg, final Context mContext) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                WifiManager wm = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo mInfo = wm.getConnectionInfo();
                if (mInfo != null) {
                    wm.disableNetwork(mInfo.getNetworkId());
                }
                boolean flag = false;
                if (cfg.networkId > 0) {
                    flag = wm.enableNetwork(cfg.networkId, true);
                    wm.updateNetwork(cfg);
                } else {
                    int netId = wm.addNetwork(cfg);
                    if (netId > 0) {
                        wm.saveConfiguration();
                        flag = wm.enableNetwork(netId, true);
                    }
                }

            }
        }.start();
    }


    public static WifiConfiguration createWifiConfig(String SSID, String Password,

                                                     WifiCipherType Type) {

        WifiConfiguration config = new WifiConfiguration();

        config.allowedAuthAlgorithms.clear();

        config.allowedGroupCiphers.clear();

        config.allowedKeyManagement.clear();

        config.allowedPairwiseCiphers.clear();

        config.allowedProtocols.clear();

        if (!SSID.startsWith("\"")) {

            SSID = "\"" + SSID + "\"";
        }
        config.SSID = SSID;

//		Log.d(WifiUtil.class.getSimpleName(), config.SSID );

        // 无密码

        if (Type == WifiCipherType.WIFICIPHER_NOPASS) {

            config.wepKeys[0] = "\"" + "\"";

            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

            config.wepTxKeyIndex = 0;

        }

        // WEP加密

        if (Type == WifiCipherType.WIFICIPHER_WEP) {

            config.preSharedKey = "\"" + Password + "\"";

            config.hiddenSSID = true;

            config.allowedAuthAlgorithms

                    .set(WifiConfiguration.AuthAlgorithm.SHARED);

            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);

            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

            config.allowedGroupCiphers

                    .set(WifiConfiguration.GroupCipher.WEP104);

            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

            config.wepTxKeyIndex = 0;

        }

        // WPA加密

        if (Type == WifiCipherType.WIFICIPHER_WPA) {

            config.preSharedKey = "\"" + Password + "\"";

            config.hiddenSSID = true;

//			 config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

//			 config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

//			 config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

//			 config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

            config.status = WifiConfiguration.Status.ENABLED;

        }

        return config;

    }

    //-------------------------------------  LRX --- start

    /**
     * 自动创建WifiConfiguration
     *
     * @param wifiManager
     * @param pair
     * @return
     */
    public static WifiConfiguration autoCreateConfig(WifiManager wifiManager, Pair<String, ScanResult> pair) {
        if (pair == null) {
            return null;
        } else {
            WifiConfiguration config = new WifiConfiguration();
            //清除默认配置
            config.allowedAuthAlgorithms.clear();
            config.allowedGroupCiphers.clear();
            config.allowedPairwiseCiphers.clear();
            config.allowedProtocols.clear();
            config.allowedKeyManagement.clear();
            //判断是否已经存在该SSID的配置，存在就干掉
            WifiConfiguration tempConfig = isExist(wifiManager, pair.second);
            if (tempConfig != null) {
                wifiManager.removeNetwork(tempConfig.networkId);
            }
            WifiCipherType type = checkWifiCipher(pair.second.capabilities);
            //配置config
            config.SSID = "\"" + pair.second.SSID + "\"";
            switch (type) {
                case WIFICIPHER_WEP:
                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                    if (getHexKey(pair.first)) config.wepKeys[0] = pair.first;
                    else config.wepKeys[0] = "\"".concat(pair.first).concat("\"");
                    config.wepTxKeyIndex = 0;
                    break;
                case WIFICIPHER_WPA:
                    config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    config.preSharedKey = "\"".concat(pair.first).concat("\"");
                    break;
                case WIFICIPHER_NOPASS:
                case WIFICIPHER_INVALID:
                default:
                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                    config.allowedAuthAlgorithms.clear();
                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    break;
            }
            return config;
        }
    }

    /**
     * WEP has two kinds of password, a hex value that specifies the key or
     * a character string used to generate the real hex. This checks what kind of
     * password has been supplied. The checks correspond to WEP40, WEP104 & WEP232
     *
     * @param s
     * @return
     */
    private static boolean getHexKey(String s) {
        if (s == null) {
            return false;
        }
        int len = s.length();
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }
        for (int i = 0; i < len; ++i) {
            char c = s.charAt(i);
            if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
                continue;
            }
            return false;
        }
        return true;
    }

    public static WifiCipherType checkWifiCipher(String cipher) {
//        String cipher = getEncryptString(capability);
        if (TextUtils.isEmpty(cipher)) {
            return WifiCipherType.WIFICIPHER_NOPASS;
        }
        if (cipher.toUpperCase().contains("WEP".toUpperCase())) {
            return WifiCipherType.WIFICIPHER_WEP;
        } else if (cipher.toUpperCase().contains("WPA".toUpperCase()) ||
                cipher.toUpperCase().contains("WPA2".toUpperCase()) ||
                cipher.toUpperCase().contains("WPS".toUpperCase())) {
            return WifiCipherType.WIFICIPHER_WPA;
        } else if (cipher.toUpperCase().contains("UNKNOW".toUpperCase())) {
            return WifiCipherType.WIFICIPHER_INVALID;
        } else {
            return WifiCipherType.WIFICIPHER_NOPASS;
        }
    }

    public static WifiConfiguration isExist(WifiManager wifiManager, ScanResult result) {
        List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration config : configs) {
            if (config == null || config.BSSID == null || config.SSID == null) continue;
            if (config.SSID.equals("\"" + result.SSID + "\"") &&
                    config.BSSID.equals("\"" + result.BSSID + "\"")) {
                return config;
            }
        }
        return null;
    }

    public static Pair<Integer, ScanResult> isConnecting(WifiManager wifiManager, List<ScanResult> list) {
        if (wifiManager == null) {
            return null;
        }
        WifiInfo info = wifiManager.getConnectionInfo();
        if (null == info || "00:00:00:00:00:00".equals(info.getBSSID())) {
            return null;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            ScanResult scanResult = list.get(i);
            if (info.getSSID().equals("\"" + scanResult.SSID + "\"") &&
                    info.getBSSID().equals(scanResult.BSSID)) {
                list.remove(i);
                return Pair.create(info.getNetworkId(), scanResult);
            }
        }
        return null;
    }

    public static List<Pair<Integer, ScanResult>> toCheckWifiList(WifiManager wifiManager, List<ScanResult> list) {
        List<Pair<Integer, ScanResult>> resultList = new ArrayList<>();
        //判断list是否为空 为空return
        if (list == null || list.isEmpty()) {
            return null;
        }
        //获取到wifi info list，遍历它,并使用ScanResult对比每一个wifi info，如果匹配上则把networkId把上去
        List<WifiConfiguration> saveNetIdList = wifiManager.getConfiguredNetworks();
        //按优先级排序
        sortByPriority(saveNetIdList);
        //遍历判断
        if (saveNetIdList == null || saveNetIdList.isEmpty()) {
            for (ScanResult scan : list) {
                resultList.add(Pair.create(NO_SAVE_PASSWORD, scan));
            }
        } else {
            for (ScanResult scan : list) {
                int tag = -1;
                for (WifiConfiguration configuration : saveNetIdList) {
                    int netID = equalsWifiInfo(configuration, scan);
                    if (NO_SAVE_PASSWORD != netID) {
                        tag = equalsWifiInfo(configuration, scan);
                    }
                }
                resultList.add(Pair.create(tag, scan));
            }
        }
        return resultList;
    }


    public static Integer equalsWifiInfo(WifiConfiguration a, ScanResult b) {
        if (a.SSID.equals("\"" + b.SSID + "\"")) {
            return a.networkId;
        }
        return NO_SAVE_PASSWORD;
    }

    public static String convertToQuotedString(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        final int lastPos = string.length() - 1;
        if (lastPos > 0 && (string.charAt(0) == '"' && string.charAt(lastPos) == '"')) {
            return string;
        }
        return "\"" + string + "\"";
    }

    private static void sortByPriority(final List<WifiConfiguration> configurations) {
        if (configurations == null || configurations.isEmpty()) {
            return;
        }
        Collections.sort(configurations, new Comparator<WifiConfiguration>() {
            @Override
            public int compare(WifiConfiguration object1,
                               WifiConfiguration object2) {
                return object1.priority - object2.priority;
            }
        });
    }

    public static boolean mForgetNetwork(WifiManager wifiManager, int networkId) {
        return wifiManager.removeNetwork(networkId);
    }

    public static WifiInfo getCurrentWifiInfo(WifiManager wifiManager) {
        return wifiManager.getConnectionInfo();
    }

    /**
     * 通过反射出不同版本的connect方法来连接Wifi
     */
    private static Method connectWifiByReflectMethod(WifiManager mWifiManager, int netId) {
        Method connectMethod = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // 反射方法： connect(int, listener) , 4.2 <= phone‘s android version
            for (Method methodSub : mWifiManager.getClass()
                    .getDeclaredMethods()) {
                if ("connect".equalsIgnoreCase(methodSub.getName())) {
                    Class<?>[] types = methodSub.getParameterTypes();
                    if (types != null && types.length > 0) {
                        if ("int".equalsIgnoreCase(types[0].getName())) {
                            connectMethod = methodSub;
                            break;
                        }
                    }
                }
            }
            if (connectMethod != null) {
                try {
                    connectMethod.invoke(mWifiManager, netId, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
            // 反射方法: connect(Channel c, int networkId, ActionListener listener)
            // 暂时不处理4.1的情况 , 4.1 == phone‘s android version
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            // 反射方法：connectNetwork(int networkId) ,
            // 4.0 <= phone‘s android version < 4.1
            for (Method methodSub : mWifiManager.getClass()
                    .getDeclaredMethods()) {
                if ("connectNetwork".equalsIgnoreCase(methodSub.getName())) {
                    Class<?>[] types = methodSub.getParameterTypes();
                    if (types != null && types.length > 0) {
                        if ("int".equalsIgnoreCase(types[0].getName())) {
                            connectMethod = methodSub;
                        }
                    }
                }
            }
            if (connectMethod != null) {
                try {
                    connectMethod.invoke(mWifiManager, netId);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } else {
            // < android 4.0
            return null;
        }
        putKey(netId);
        clearDisTag();
        return connectMethod;
    }

    public static void connect(WifiManager wifiManager, int netWorkId) {
        Method method = connectWifiByReflectMethod(wifiManager, netWorkId);
//        if(method!=null && (!method.isAccessible())){
//            method.setAccessible(true);
//        }
        if (method == null) {
            Log.e("WIFI_ERROR", "当前系统版本的connect方法,反射不到");
        }
    }

    public static boolean isNetworkConnected(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//            //WIFI网络跳转的页面.
//        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//            //3G网络跳转的页面
//        }
        return (networkInfo != null && networkInfo.isConnected());
    }

    @SuppressWarnings("all")
    public static RssiStatEnum wifiLevelModel(WifiInfo wifiInfo) {
        int level = wifiInfo.getRssi();
        RssiStatEnum rssiStatEnum = RssiStatEnum.WIFI_DISCONNECTION;
        if (level > -55) {
            rssiStatEnum = RssiStatEnum.WIFI_LEVEL_HIGH;
        } else if (level > -70 && level <= -50) {
            rssiStatEnum = RssiStatEnum.WIFI_LEVEL_HIGH_HALF;
        } else if (level > -85 && level <= -70) {
            rssiStatEnum = RssiStatEnum.WIFI_LEVEL_MIDDLE;
        } else if (level > -100 && level <= -80) {
            rssiStatEnum = RssiStatEnum.WIFI_LEVEL_LOW_BETTER;
        } else if (level <= -100) {
            rssiStatEnum = RssiStatEnum.WIFI_LEVEL_LOW;
        }
        return rssiStatEnum;
    }


    public static boolean isDeviceOnWifi(final Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi != null && mWifi.isConnectedOrConnecting();
    }

    //-------------------------------------  LRX --- end

    public static WifiInfo getConnectedWifiInfo() {

        WifiManager wm = (WifiManager) MyWifi.context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        return wm.getConnectionInfo();

    }

    /**
     * 获取扫描结果
     *
     * @param mContext
     * @return
     */
    public static List<ScanResult> getWifiScanResult(Context mContext) {


        List<ScanResult> res = new ArrayList<ScanResult>();

        WifiManager wm = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        res = wm.getScanResults();

        if (res != null) {
//			for(ScanResult mRs : mResult){
//				Log.d(WifiUtil.class.getSimpleName(), mRs.toString());
//			}
        }

        return res;
    }

    public static boolean isWifiOpen(Context mContext) {

        WifiManager wm = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        return wm.isWifiEnabled();

    }

    public static void openWifi(final Context mContext, final IWifiOpen mCallBack) {

        new Thread(
                new Runnable() {

                    @Override
                    public void run() {

                        WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

                        wm.setWifiEnabled(true);

                        while (wm.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {

                        }

//						Log.d(WifiUtil.class.getSimpleName(), "openWifi finish... " + wm.getWifiState());

                        if (mCallBack != null) {

                            mCallBack.onWifiOpen(wm.getWifiState());
                        }
                    }


                }).start();

    }

    public static void openWifi(final Context mContext, boolean open) {
        WifiManager wm = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wm.setWifiEnabled(open);
    }

    public static void reStartWifi(final Context mContext) {
        if (isWifiOpen(mContext)) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    openWifi(mContext, false);
                    try {
                        sleep(1000 * 2);
                        openWifi(mContext, true);
                    } catch (InterruptedException e) {
                        try {
                            openWifi(mContext, true);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                }
            }.start();

        } else {
            openWifi(mContext, true);
        }

    }


    public static void closeWifi(final Context mContext) {

        new Thread(
                new Runnable() {

                    @Override
                    public void run() {

                        WifiManager wm = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                        wm.setWifiEnabled(false);

                    }


                }).start();

    }


    public interface IWifiOpen {

        public void onWifiOpen(int state);
    }


    //将搜索到的wifi根据信号强度从强到弱进行排序
    public static void sortByLevel(List<ScanResult> list) {
        for (int i = 0; i < list.size(); i++)
            for (int j = 1; j < list.size(); j++) {
                if (list.get(i).level > list.get(j).level)    //level属性即为强度
                {
                    ScanResult temp = null;
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
    }

    /**
     * 排序 信号强的放前面
     */
    public static Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
        @Override
        public int compare(ScanResult lhs, ScanResult rhs) {
            WifiInfo info = WifiUtil.getConnectedWifiInfo();

            if (info != null && info.getSSID() != null) {
                if (info.getSSID().equals(lhs.SSID)
                        || info.getSSID().equals("\"" + lhs.SSID + "\"")) {
                    return -1;
                }

                if (info.getSSID().equals(rhs.SSID)
                        || info.getSSID().equals("\"" + rhs.SSID + "\"")) {
                    return 1;
                }
            }

            return 0 - WifiManager.compareSignalLevel(lhs.level, rhs.level);
        }
    };


    /**
     * 排序 信号强的放前面
     */
    public static Comparator<ScanResult> comparatorForDelete = new Comparator<ScanResult>() {
        @Override
        public int compare(ScanResult lhs, ScanResult rhs) {
            WifiInfo info = WifiUtil.getConnectedWifiInfo();

            if (info != null && info.getSSID() != null) {
                if (info.getSSID().equals(lhs.SSID)
                        || info.getSSID().equals("\"" + lhs.SSID + "\"")) {
                    return 1;
                }

                if (info.getSSID().equals(rhs.SSID)
                        || info.getSSID().equals("\"" + rhs.SSID + "\"")) {
                    return -1;
                }
            }

            return 0 - WifiManager.compareSignalLevel(rhs.level, lhs.level);
        }
    };

    public static boolean autoSwitchWifi(Context context) {
        if (context == null || !WifiUtil.isWifiOpen(context)) {
            return false;
        }
//        long currentTime = System.currentTimeMillis();
//        if ((currentTime - lastSwitchTime) > SWITCH_DURATION) {
//            WifiUtil.reStartWifi(context);//请求数据失败，可能是没有网络，重启WIFI模块
//            lastSwitchTime = System.currentTimeMillis();
//            return true;
//        } else {
//            return false;
//        }
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiManager.reconnect();
        }
        return true;
    }

    private static HashMap<Integer, Boolean> connectMap = new HashMap<>();

    private static void putKey(Integer netWorkId) {
        connectMap.put(netWorkId, false);
    }

    private static void updateKey(Integer netWorkId, Boolean isConnected) {
        connectMap.put(netWorkId, isConnected);
    }

    public static void onConnected() {
        WifiInfo info = getConnectedWifiInfo();
        if (connectMap.containsKey(info.getNetworkId())) {
            updateKey(info.getNetworkId(), true);
        }
    }

    public static void deleteNetword(Integer netWorkId) {
        connectMap.remove(netWorkId);
    }

    public static LinkedList<Integer> clearConnectedId() {
        LinkedList<Integer> list = new LinkedList<>();
        for (Map.Entry<Integer, Boolean> item : connectMap.entrySet()) {
            if (!item.getValue()) {
                list.add(item.getKey());
            }
        }
        return list;
    }

    private static int disconnTag = 0;

    public static boolean onDisconnected() {
        disconnTag++;
        if (disconnTag >= 3) {
            clearDisTag();
            return true;
        } else {
            return false;
        }
    }

    private static void clearDisTag() {
        disconnTag = 0;
    }


    private static long lastClickTime = 0;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= 1000) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }
}

