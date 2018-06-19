package com.haier.wifi.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.haier.wifi.R;
import com.haier.wifi.adapter.WifiListAdapter;
import com.haier.wifi.api.IConnectRealTime;
import com.haier.wifi.api.IScanResult;
import com.haier.wifi.api.IWifiStat;
import com.haier.wifi.api.bean.WifiStatEnum;
import com.haier.wifi.base.BaseActivity;
import com.haier.wifi.server.WifiServer;
import com.haier.wifi.util.FactoryLogicManager;
import com.haier.wifi.util.PromptingDialog;
import com.haier.wifi.util.SetApPwdDialog;
import com.haier.wifi.util.ShowWifiInfoDialog;
import com.haier.wifi.util.WifiUtil;

import java.util.Calendar;
import java.util.List;

import static com.haier.wifi.api.bean.WifiStatEnum.WIFI_STATE_ENABLED;
import static com.haier.wifi.server.WifiServer.CONNECT_HAS_PASSWORD;
import static com.haier.wifi.server.WifiServer.CONNECT_POINT;
import static com.haier.wifi.server.WifiServer.FORGET_NETWORK;

/**
 * Created by LRXx on 2017/12/1.
 */

public class WifiActivity extends BaseActivity implements IConnectRealTime
        , IScanResult, IWifiStat, WifiListAdapter.WifiItemListener, ShowWifiInfoDialog.IRemoveWifi {

    private ProgressBar wifiLoad;//刷新
    private Button nextBtn;//下一步
    protected WifiListAdapter wifiListAdapter;
    protected RecyclerView list;
    protected WifiServer server;
    protected boolean mBound;
    private TextView tv_loading;
    private TextView btn_lock_open;
    private TextView btn_lock_close;

    protected void next() {
        Toast.makeText(WifiActivity.this, "下一步", Toast.LENGTH_SHORT).show();
    }
    protected void nearClick(){Toast.makeText(WifiActivity.this, "附近wifi", Toast.LENGTH_SHORT).show();}

    @Override
    protected int getLayoutId() {
        return R.layout.list;
    }


    @Override
    protected void setup(@Nullable Bundle savedInstanceState) {
        super.setup(savedInstanceState);
        initView();
        initDate();
        setListener();
    }

    private void initView() {
        wifiLoad = (ProgressBar) findViewById(R.id.wifi_load);
        nextBtn = (Button) findViewById(R.id.next_btn);
        list = (RecyclerView) findViewById(R.id.list);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
        btn_lock_open = (TextView) findViewById(R.id.btn_lock_open);
        btn_lock_close = (TextView) findViewById(R.id.btn_lock_close);
        //四代智能电磁锁
        if ("haier_zn_346_rk1".equals(Build.DEVICE)){
            btn_lock_open.setVisibility(View.VISIBLE);
            btn_lock_close.setVisibility(View.VISIBLE);
            //打开智能电磁锁
            btn_lock_open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent openIntent = new Intent();
                    openIntent.setAction("android.intent.action.ELECTROMAGNETIC_LOCK");
                    openIntent.putExtra("state", true);
                    sendBroadcast(openIntent);
                }
            });
            //关闭智能电磁锁
            btn_lock_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent closeIntent = new Intent();
                    closeIntent.setAction("android.intent.action.ELECTROMAGNETIC_LOCK");
                    closeIntent.putExtra("state", false);
                    sendBroadcast(closeIntent);
                }
            });
        }

    }

    private void initDate() {
        tv_loading.setVisibility(View.VISIBLE);
        //工厂逻辑
        FactoryLogicManager.init(this,
                findViewById(R.id.goto_launcher_view),
                findViewById(R.id.goto_factory_test),
                findViewById(R.id.goto_serial_factory_test),
                findViewById(R.id.near),
                nearClickListener
        );
        if (WifiUtil.isWifiOpen(this)) {
            wifiStat = WIFI_STATE_ENABLED;
        } else {
            WifiUtil.openWifi(WifiActivity.this, true);
            wifiStat = WIFI_STATE_ENABLED;
        }
        wifiLoad.setIndeterminateDrawable(getResources().getDrawable(R.drawable.wifi_refresh));
        wifiListAdapter = new WifiListAdapter();
        wifiListAdapter.setClickListener(this);
        wifiListAdapter.setUpdateButton(updateButton);
        list.setLayoutManager(generateLayoutManager());
        list.setAdapter(wifiListAdapter);
        setNextBtnNoClickable();
        updateBtu();
    }

    private void setListener() {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
        wifiLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WifiActivity.this, "刷新", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, WifiServer.class), connection, BIND_AUTO_CREATE);
    }

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

    private void bindOk() {
        server.setConnectStat(this);
        server.setiScanResult(this);
        server.setWifiStat(this);
        server.handler.onResume();
        updateBtu();
    }

    private WifiListAdapter.UpdateButton updateButton = new WifiListAdapter.UpdateButton() {
        @Override
        public void update() {
//            updateBtu();
            updateTrain();
        }
    };

    private void updateBtu() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (WifiUtil.isNetworkConnected(WifiActivity.this)) {
                    setNextBtnClickable();
                } else {
                    setNextBtnNoClickable();
                }
            }
        });
    }

    private void updateTrain() {
        setNextBtnClickable();
    }

    boolean aaTag = false;

    /**
     * 下一步,可以点击
     */
    private void setNextBtnClickable() {
        if (!aaTag) {
            aaTag = true;
            Toast.makeText(WifiActivity.this, "连接成功", Toast.LENGTH_LONG).show();
            nextBtn.setClickable(true);
            nextBtn.setBackgroundResource(R.drawable.gold_shape);
            nextBtn.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    /**
     * 下一步,不可点击
     */
    private void setNextBtnNoClickable() {
        aaTag = false;
        nextBtn.setClickable(false);
        nextBtn.setBackgroundResource(R.drawable.gray_shape);
        nextBtn.setTextColor(Color.parseColor("#c3c3c3"));
    }

    public LinearLayoutManager generateLayoutManager() {
        list.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }


    private WifiStatEnum wifiStat;
    protected Handler mHandler = new Handler();
    private String connectTag = "已连接";

    @Override
    public void WifiStatChange(WifiStatEnum statEnum) {
        wifiStat = statEnum;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
//                mRefreshWifiButton();
            }
        });
    }

    @Override
    public void onDisconnected() {
        connectTag = "断开连接";
        if (WifiUtil.onDisconnected()) {
            remoteNetWorkIdList(WifiUtil.clearConnectedId());
        }
        refreshCurrentWifiInfo(connectTag);
    }

    @Override
    public void onScanning() {

    }

    @Override
    public void onAssociating() {
        connectTag = "正在连接...";
        refreshCurrentWifiInfo(connectTag);
    }

    @Override
    public void onAuthenticating() {
        connectTag = "正在连接...";
        refreshCurrentWifiInfo(connectTag);
    }

    @Override
    public void WrongPassword() {
        connectTag = "密码错误";
        if (currentConnectingNetWorkId != -1) {
            remoteNetWorkId(currentConnectingNetWorkId);
        }
        refreshCurrentWifiInfo(connectTag);
    }

    private int currentConnectingNetWorkId = -1;

    private void remoteNetWorkIdList(List<Integer> list) {
        for (Integer i : list) {
            remoteNetWorkId(i);
        }
    }

    private void remoteNetWorkId(final int NetWorkId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WifiActivity.this, "密码错误，请重新输入密码或连接其它热点", Toast.LENGTH_LONG).show();
                deleteNetWork(NetWorkId);
            }
        });
    }

    private void deleteNetWork(int NetWorkId) {
        WifiUtil.deleteNetword(NetWorkId);
        Message msg = mHandler.obtainMessage();
        msg.what = FORGET_NETWORK;
        msg.arg1 = NetWorkId;
        server.handler.sendMessage(msg);
    }

    @Override
    public void onFourWayHandshake() {
        connectTag = "正在连接...";
        refreshCurrentWifiInfo(connectTag);
    }

    @Override
    public void onGroupHandshake() {
        connectTag = "正在连接...";
        refreshCurrentWifiInfo(connectTag);
    }

    @Override
    public void onDhcping() {
        connectTag = "正在连接...";
        refreshCurrentWifiInfo(connectTag);
    }

    @Override
    public void onCompleted() {
        connectTag = "已连接";
        refreshCurrentWifiInfo(connectTag);
        WifiUtil.onConnected();
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                //已连接
////                Intent intent = new Intent(ConstantUtil.BROADCAST_NETWORK_CONNECTED);
////                intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
////                sendBroadcast(new Intent(intent));
//
//                updateBtu();
//            }
//        });
    }


    private void refreshCurrentWifiInfo(final String s) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                wifiListAdapter.setCurrentStat(s);
                wifiListAdapter.notifyItemChanged(0);
            }
        });
    }

    @Override
    public void toResult(final List<Pair<Integer, ScanResult>> results,
                         final boolean isConnecting, int currentConnectingNetWorkId) {
        updateBtu();
        this.currentConnectingNetWorkId = isConnecting ? currentConnectingNetWorkId : -1;
        if (wifiListAdapter != null) {
            if (wifiStat == WIFI_STATE_ENABLED) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        openWifi();
//                        network_loading2.setVisibility(View.GONE);
                        tv_loading.setVisibility(View.INVISIBLE);
                        wifiListAdapter.setHasConnecting(isConnecting);
                        wifiListAdapter.setCurrentStat(connectTag);
                        wifiListAdapter.setData(results);

                    }
                });
            }
        }
    }

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onWifiItemClick(final Pair<Integer, ScanResult> pair, final int position, WifiListAdapter.
            WifiClickStat wifiClickStat) {
        Message msg = mHandler.obtainMessage();
        Pair<String, ScanResult> mPair = Pair.create("", pair.second);
        /**
         * 是为正在连接的item
         *      当前是为已连接状态
         *      当前是正在连接状态
         * 否为正在连接的item
         *      是无密码直接连接
         *      有密码
         *          已保存
         *          未保存
         */
        switch (wifiClickStat) {
            case SHOW_WIFI_INFO:
                ShowWifiInfoDialog.show(this, this, WifiUtil.getConnectedWifiInfo(),
                        WifiUtil.getEncryptString(pair.second.capabilities));
                break;
            case CONNECTING:
                //正在连接
                ShowWifiInfoDialog.show(this, this, WifiUtil.getConnectedWifiInfo(),
                        WifiUtil.getEncryptString(pair.second.capabilities));
                break;
            case NO_PASSWORD_CONNECT:
                PromptingDialog.show(this, new PromptingDialog.IOk() {
                    @Override
                    public void ok() {
                        setNextBtnNoClickable();
                        Message msg = mHandler.obtainMessage();
                        msg.what = CONNECT_POINT;
                        String password = "";
                        Pair<String, ScanResult> mPair = Pair.create(password, pair.second);
                        msg.obj = mPair;
                        server.handler.sendMessage(msg);
                    }

                    @Override
                    public void cancel() {

                    }
                }, "不安全的网络，是否仍要连接？", "连接", "取消");
                break;
            case HAS_SAVE_PASSWORD_CONNECT:
                PromptingDialog.show(this, new PromptingDialog.IOk() {
                    @Override
                    public void ok() {
                        setNextBtnNoClickable();
                        Message msg = mHandler.obtainMessage();
                        msg.what = CONNECT_HAS_PASSWORD;
                        msg.arg1 = pair.first;
                        server.handler.sendMessage(msg);
                        list.scrollToPosition(0);
                    }

                    @Override
                    public void cancel() {
                        Message msg = mHandler.obtainMessage();
                        msg.what = FORGET_NETWORK;
                        msg.arg1 = pair.first;
                        server.handler.sendMessage(msg);
                        wifiListAdapter.deletePassword(position, pair);
                    }
                }, "此密码已经保存，是否直接连接！", "连接", "删除");
                break;
            case INPUT_PASSWORD:
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    SetApPwdDialog.show(WifiActivity.this, new SetApPwdDialog.IConnectWifi() {
                        @Override
                        public void onConnectClick(ScanResult scanResult, String pwd, WifiUtil.WifiCipherType mType) {
                            setNextBtnNoClickable();
                            Message msg = mHandler.obtainMessage();
                            msg.what = CONNECT_POINT;
                            Pair<String, ScanResult> mPair = Pair.create(pwd, scanResult);
                            msg.obj = mPair;
                            server.handler.sendMessage(msg);
                            list.scrollToPosition(0);
                        }
                    }, pair.second, WifiUtil.getWifiCipher(pair.second.capabilities), pair.second.capabilities);
                }
                //去连接
            default:
                break;
        }
    }

    @Override
    public void onRemoveClick(int networkId) {
        setNextBtnNoClickable();
        Message msg = mHandler.obtainMessage();
        msg.what = FORGET_NETWORK;
        msg.arg1 = networkId;
        server.handler.sendMessage(msg);
        updateBtu();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return ev.getPointerCount() > MotionEvent.ACTION_UP ||
                super.dispatchTouchEvent(ev);
    }

    FactoryLogicManager.NearClickListener nearClickListener = new FactoryLogicManager.NearClickListener() {
        @Override
        public void onNearClick() {
            nearClick();
        }
    };
}
