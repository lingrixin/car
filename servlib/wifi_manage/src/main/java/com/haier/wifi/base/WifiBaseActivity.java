package com.haier.wifi.base;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.haier.wifi.R;
import com.haier.wifi.adapter.WifiListAdapter;
import com.haier.wifi.api.IConnectRealTime;
import com.haier.wifi.api.IScanResult;
import com.haier.wifi.api.IWifiStat;
import com.haier.wifi.server.WifiServer;

/**
 * <p class="note"> 给Wifi列表使用的基类</p>
 * created by LRXx at 2017-8-2
 */
public abstract class WifiBaseActivity
        extends BaseActivity
        implements WifiListAdapter.WifiItemListener {

    protected WifiServer server;
    protected boolean mBound;
    protected IConnectRealTime connectStat;
    protected IWifiStat wifiStat;
    protected IScanResult scanResult;
    protected WifiListAdapter wifiListAdapter;
    protected RecyclerView list;

    protected abstract void bindOk();

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            server = ((WifiServer.MyBinder) service).getService();
            mBound = true;
            bindOk();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            server = null;
            mBound = false;
        }
    };

    @Override
    protected void setup(@Nullable Bundle savedInstanceState) {
        list = f(R.id.list);
        bindService(new Intent(this, WifiServer.class), connection, BIND_AUTO_CREATE);
        wifiListAdapter = new WifiListAdapter();
        wifiListAdapter.setClickListener(this);
        list.setLayoutManager(generateLayoutManager());
        list.setAdapter(wifiListAdapter);
        super.setup(savedInstanceState);
    }

    public LinearLayoutManager generateLayoutManager() {
        list.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }
}
