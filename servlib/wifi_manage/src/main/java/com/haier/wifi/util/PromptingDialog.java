package com.haier.wifi.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.haier.wifi.R;

/**
 * Created by LRXx on 2017/12/7.
 */

public class PromptingDialog extends DialogFragment {

    private String msg, ok, cancel;
    private IOk listener;

    private FragmentActivity mActivity;

    public PromptingDialog() {
    }

    public interface IOk {
        void ok();
        void cancel();
    }

    public static PromptingDialog newInstance(IOk listener, String msg, String ok, String cancel) {
        PromptingDialog dialog = new PromptingDialog();
        dialog.listener = listener;
        dialog.msg = msg;
        dialog.ok = ok;
        dialog.cancel = cancel;
        return dialog;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.from(mActivity).inflate(R.layout.dialog_prompting, null);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        Button bt_ok = (Button) view.findViewById(R.id.bt_ok);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        tv_msg.setText(msg);
        bt_ok.setText(ok);
        bt_cancel.setText(cancel);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.ok();
                close();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cancel();
                close();
            }
        });
        return view;
    }

    private void close(){
        hideKeyBoard();
        dismiss();
    }

    public void hideKeyBoard() {
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void show(FragmentActivity mActivity,IOk listener, String msg, String ok, String cancel){
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        Fragment mBefore = mActivity.getSupportFragmentManager().findFragmentByTag(PromptingDialog.class.getSimpleName());
        if (mBefore != null) {
            ((DialogFragment) mBefore).dismiss();
            ft.remove(mBefore);
        }
//        ft.addToBackStack(null);
        DialogFragment mNow = PromptingDialog.newInstance(listener, msg, ok, cancel);
        mNow.show(ft, PromptingDialog.class.getSimpleName());
    }
}
