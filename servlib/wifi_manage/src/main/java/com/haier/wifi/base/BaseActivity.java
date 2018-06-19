package com.haier.wifi.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by LRXx on 2017/11/30.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract
    @LayoutRes
    int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setup(savedInstanceState);
    }

    protected void setup(@Nullable Bundle savedInstanceState) {

    }

    protected <T extends View> T f(@IdRes int resId) {
        return ViewHelper.f(this, resId);
    }
}
