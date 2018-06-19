package com.haier.wifi.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.haier.wifi.R;


/**
 * wifi信息弹窗
 */
@SuppressLint("NewApi")
public class ShowWifiInfoDialog extends DialogFragment {
    private FragmentActivity mActivity;
    private IRemoveWifi mIRemoveWifi = null;
    private WifiInfo mConnectedInfo;
    private String encrypt;

    public interface IRemoveWifi {
        void onRemoveClick(int networkId);
    }


    public static ShowWifiInfoDialog newInstance(IRemoveWifi mIRemoveWifi, WifiInfo mConnectedInfo, String encrypt) {
        ShowWifiInfoDialog mFragment = new ShowWifiInfoDialog();
        mFragment.mConnectedInfo = mConnectedInfo;
        mFragment.encrypt = encrypt;
        mFragment.mIRemoveWifi = mIRemoveWifi;
        return mFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActivity = (FragmentActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.new_show_connected_wifi_page, null);

        Button btCancel = (Button) view.findViewById(R.id.button12);
        Button btSure = (Button) view.findViewById(R.id.button13);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowWifiInfoDialog.this.dismiss();
            }
        });
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIRemoveWifi != null) {
                    mIRemoveWifi.onRemoveClick(mConnectedInfo.getNetworkId());
                }
                ShowWifiInfoDialog.this.dismiss();
            }
        });
        TextView mStateTv = (TextView) view.findViewById(R.id.state_tv);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int Ip = mConnectedInfo.getIpAddress();
//        Log.d(TAG, "ip = " + Ip);
        String strIp = "" + (Ip & 0xFF) + "." + ((Ip >> 8) & 0xFF) + "." + ((Ip >> 16) & 0xFF) + "." + ((Ip >> 24) & 0xFF);

        if (mConnectedInfo.getSSID() != null && mConnectedInfo.getBSSID() != null && !strIp.equals("0.0.0.0")) {

            mStateTv.setText("已连接");
        } else {
            mStateTv.setText("正在连接...");
        }

        TextView mSafetyTv = (TextView) view.findViewById(R.id.safety_tv);
        TextView ssidName = (TextView) view.findViewById(R.id.textView60);

        mSafetyTv.setText(encrypt);
        ssidName.setText(mConnectedInfo.getSSID().replace("\"", ""));
        TextView mLevelTv = (TextView) view.findViewById(R.id.level_tv);

        mLevelTv.setText(String.valueOf(mConnectedInfo.getRssi()));

        TextView mSpeedTv = (TextView) view.findViewById(R.id.speed_tv);

        mSpeedTv.setText(mConnectedInfo.getLinkSpeed() + " Mbps");

        TextView mIpTv = (TextView) view.findViewById(R.id.ip_tv);

        mIpTv.setText(WifiUtil.long2ip(mConnectedInfo.getIpAddress()));
        return view;
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.button12://button cancel
//                dismiss();
//                break;
//            case R.id.button13://button 忽略网络
//                if (mIRemoveWifi != null) {
//                    mIRemoveWifi.onRemoveClick(mConnectedInfo.getNetworkId());
//                }
//                dismiss();
//                break;
//        }
//    }

    public static void show(FragmentActivity mActivity, IRemoveWifi mIRemoveWifi, WifiInfo mWifiInfo, String encrypt) {

        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();

        Fragment mBefore = mActivity.getSupportFragmentManager().findFragmentByTag(ShowWifiInfoDialog.class.getSimpleName());

        if (mBefore != null) {

            ((DialogFragment) mBefore).dismiss();

            ft.remove(mBefore);
        }
//        ft.addToBackStack(null);

        DialogFragment mNow = ShowWifiInfoDialog.newInstance(mIRemoveWifi, mWifiInfo, encrypt);

        mNow.show(ft, ShowWifiInfoDialog.class.getSimpleName());
    }
}
