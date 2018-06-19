package com.haier.wifi.adapter;

import android.net.wifi.ScanResult;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haier.wifi.R;
import com.haier.wifi.base.RecyclerAdapter;
import com.haier.wifi.util.WifiUtil;


/**
 * <p class="note"></p>
 * created by LRXx at 2017-8-3
 */
public class WifiListAdapter extends RecyclerAdapter<Pair<Integer, ScanResult>> {

    public WifiListAdapter() {
        super(R.layout.item);
    }

    private boolean hasConnecting;
    private String currentStat;

    public void setHasConnecting(boolean hasConnecting) {
        this.hasConnecting = hasConnecting;
    }

    public void setCurrentStat(String currentStat) {
        this.currentStat = currentStat;
    }

    public void setData(String currentStat, boolean hasConnecting) {
        this.currentStat = currentStat;
        this.hasConnecting = hasConnecting;
    }

    public void deletePassword(int position, Pair<Integer, ScanResult> pair) {
        Pair<Integer, ScanResult> temp = Pair.create(-1, pair.second);
        mDatas.set(position, temp);
        notifyItemChanged(position);
    }

    @Override
    public void onBind(CommHolder holder, final int position, int viewType, final Pair<Integer, ScanResult> data) {
        RelativeLayout rl_item = holder.getView(R.id.rl_item);
        TextView mSsidTv = holder.getView(R.id.wifi_name);
//        TextView tvSave = holder.getView(R.id.tv_save);
        ImageView tvSave = holder.getView(R.id.iv_save);
        ImageView img_level = holder.getView(R.id.signal);
        ImageView mEncryptTv = holder.getView(R.id.secret_key);
        ProgressBar load = holder.getView(R.id.load);
        ImageView conected = holder.getView(R.id.conected);

        WifiClickStat wifiClickStat = WifiClickStat.INPUT_PASSWORD;

        ScanResult mResult = data.second;
        mSsidTv.setText(mResult.SSID);

        if (mResult.level > -55) {
            img_level.setBackgroundResource(R.mipmap.wifi_leavel11);
        } else if (mResult.level > -70 && mResult.level <= -50) {
            img_level.setBackgroundResource(R.mipmap.wifi_leavel22);
        } else if (mResult.level > -85 && mResult.level <= -70) {
            img_level.setBackgroundResource(R.mipmap.wifi_leavel33);
        } else if (mResult.level > -100 && mResult.level <= -80) {
            img_level.setBackgroundResource(R.mipmap.wifi_leavel44);
        } else if (mResult.level <= -100) {
            img_level.setBackgroundResource(R.mipmap.wifi_leavel55);
        }
        load.setVisibility(View.INVISIBLE);
        tvSave.setVisibility(View.INVISIBLE);
        if (hasConnecting && position == 0) {
//            tvSave.setVisibility(View.INVISIBLE);
            if ("已连接".equals(currentStat)) {
                wifiClickStat = WifiClickStat.SHOW_WIFI_INFO;
                conected.setVisibility(View.VISIBLE);
                if(updateButton!=null){
                    updateButton.update();
                }
            } else {
                wifiClickStat = WifiClickStat.CONNECTING;
                conected.setVisibility(View.INVISIBLE);
                load.setVisibility(View.VISIBLE);
            }
        } else {
            conected.setVisibility(View.INVISIBLE);
            if (-1 != data.first) {
                tvSave.setVisibility(View.VISIBLE);
                wifiClickStat = WifiClickStat.HAS_SAVE_PASSWORD_CONNECT;
            } else {
                tvSave.setVisibility(View.INVISIBLE);
                if ("OPEN".equals(WifiUtil.getEncryptString(data.second.capabilities))) {
//                    mEncryptTv.setVisibility(View.INVISIBLE);
                    wifiClickStat = WifiClickStat.NO_PASSWORD_CONNECT;
                } else {
//                    mEncryptTv.setVisibility(View.VISIBLE);
                    wifiClickStat = WifiClickStat.INPUT_PASSWORD;
                }
            }
        }
        if ("OPEN".equals(WifiUtil.getEncryptString(data.second.capabilities))) {
            mEncryptTv.setVisibility(View.INVISIBLE);
        } else {
            mEncryptTv.setVisibility(View.VISIBLE);
        }

        final WifiClickStat finalWifiClickStat = wifiClickStat;
        rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onWifiItemClick(data, position, finalWifiClickStat);
                    //是为正在连接的item
                    //当前是为已连接状态
                    //当前是正在连接状态
                    //否为正在连接的item
                    //是无密码直接连接
                    //有密码
                    //已保存
                    //未保存
                }
            }
        });
    }

    private WifiItemListener clickListener;

    public void setClickListener(WifiItemListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface WifiItemListener {
        void onWifiItemClick(Pair<Integer, ScanResult> pair, int position, WifiClickStat wifiClickStat);
    }

    public enum WifiClickStat {
        INPUT_PASSWORD, NO_PASSWORD_CONNECT, HAS_SAVE_PASSWORD_CONNECT, SHOW_WIFI_INFO, CONNECTING
    }

    private UpdateButton updateButton;

    public void setUpdateButton(UpdateButton updateButton) {
        this.updateButton = updateButton;
    }

    public interface UpdateButton{
        void update();
    }

}
