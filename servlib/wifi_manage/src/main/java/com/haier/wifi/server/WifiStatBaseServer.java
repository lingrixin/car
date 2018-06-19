package com.haier.wifi.server;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


import com.haier.wifi.api.IConnectRealTime;
import com.haier.wifi.api.IScanOver;
import com.haier.wifi.api.IWifiStat;
import com.haier.wifi.api.bean.WifiStatEnum;
import com.haier.wifi.util.WifiUtil;

import static android.net.NetworkInfo.DetailedState.OBTAINING_IPADDR;
import static android.net.wifi.WifiManager.ERROR_AUTHENTICATING;

/**
 * <p class="note"></p>
 * created by LRXx at 2017-8-2
 */
public abstract class WifiStatBaseServer extends Service {

    private WifiStatReceiver wifiStatReceiver = new WifiStatReceiver();
    private ScanReceiver scanReceiver = new ScanReceiver();
    private ConnectReceiver connectReceiver = new ConnectReceiver();
    protected WifiManager wifiManager ;

    protected IConnectRealTime connectStat;
    protected IWifiStat wifiStat;
    protected IScanOver iScanstat;

    protected boolean isRegister = false;

    @Override
    public void onCreate() {
        super.onCreate();
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (isRegister) {
            unRegisterStat();
        }
        connectStat = null;
        wifiStat = null;
        iScanstat = null;
        return super.onUnbind(intent);
    }

    protected void registerStat() {
        isRegister = true;
        registerReceiver(wifiStatReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        registerReceiver(scanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        registerReceiver(connectReceiver, new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
    }

    private void unRegisterStat() {
        isRegister = false;
        if (wifiStatReceiver != null) {
            unregisterReceiver(wifiStatReceiver);
        }
        if (scanReceiver != null) {
            unregisterReceiver(scanReceiver);
        }
        if (connectReceiver != null) {
            unregisterReceiver(connectReceiver);
        }
    }

    private interface IConnectStat {
        void ok();

        void failed();
    }

    private int mLastNetworkId = -1;

    protected void connect(WifiConfiguration wm) {
        final int networkId = wifiManager.addNetwork(wm);
        WifiUtil.connect(wifiManager,networkId);
    }

    protected void connectNetWordId(final int networdId) {
        WifiUtil.connect(wifiManager,networdId);
    }

    //开关状态
    class WifiStatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                if (wifiStat == null) {
                    return;
                }
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_ENABLING:
                        if (wifiStat != null) {
                            wifiStat.WifiStatChange(WifiStatEnum.WIFI_STATE_ENABLING);
                        }

                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        if (wifiStat != null) {
                            wifiStat.WifiStatChange(WifiStatEnum.WIFI_STATE_ENABLED);
                        }
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        if (wifiStat != null) {
                            wifiStat.WifiStatChange(WifiStatEnum.WIFI_STATE_DISABLING);
                        }
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:
                        if (wifiStat != null) {
                            wifiStat.WifiStatChange(WifiStatEnum.WIFI_STATE_DISABLED);
                        }
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                    default:
                        if (wifiStat != null) {
                            wifiStat.WifiStatChange(WifiStatEnum.WIFI_STATE_UNKNOWN);
                        }
                        break;
                }
            }
        }
    }

    //扫描状态
    class ScanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                if (iScanstat != null) {
                    iScanstat.IScanOver();
                }
            }
        }
    }

    //连接状态
    class ConnectReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                if (connectStat == null) {
                    return;
                }
                SupplicantState supplicantState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
                int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                WifiInfo mInfo = WifiUtil.getConnectedWifiInfo();
                if (mInfo.getDetailedStateOf(supplicantState) == OBTAINING_IPADDR) {
                    if (connectStat != null) {
                        connectStat.onDhcping();
                    }
                }
                if (ERROR_AUTHENTICATING == error) {
                    if (connectStat != null) {
                        connectStat.WrongPassword();
                    }
                    return;
                }
//                Log.d("LRX", supplicantState.name());
                if (supplicantState != null) {
                    switch (supplicantState) {
                        case ASSOCIATED:
                            break;
                        case ASSOCIATING:
                            if (connectStat != null) {
                                connectStat.onAssociating();
                            }
                            break;
                        case AUTHENTICATING:
                            if (connectStat != null) {
                                connectStat.onAuthenticating();
                            }
                            break;
                        case COMPLETED:
                            if (connectStat != null) {
                                connectStat.onCompleted();
                            }
                            break;
                        case DISCONNECTED:
                            if (connectStat != null) {
                                connectStat.onDisconnected();
                            }
                            break;
                        case DORMANT:
                            break;
                        case FOUR_WAY_HANDSHAKE:
                            if (connectStat != null) {
                                connectStat.onFourWayHandshake();
                            }
                            break;
                        case GROUP_HANDSHAKE:
                            if (connectStat != null) {
                                connectStat.onGroupHandshake();
                            }
                            break;
                        case INACTIVE:
                            break;
                        case INTERFACE_DISABLED:
                            break;
                        case INVALID:
                            break;
                        case SCANNING:
                            if (connectStat != null) {
                                connectStat.onScanning();
                            }
                            break;
                        case UNINITIALIZED:
                            break;
                    }
                }
            }
        }
    }

    public void setConnectStat(IConnectRealTime connectStat) {
        this.connectStat = connectStat;
    }

    public void setWifiStat(IWifiStat wifiStat) {
        this.wifiStat = wifiStat;
    }

    public void setiScanstat(IScanOver iScanstat) {
        this.iScanstat = iScanstat;
    }

    @Override
    public void onDestroy() {
        if (isRegister) {
            unRegisterStat();
        }
        super.onDestroy();
    }
}
