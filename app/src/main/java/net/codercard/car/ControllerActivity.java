package net.codercard.car;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import net.codercard.car.base.BaseActivity;

/**
 * Created by LRXx on 2018-6-18.
 */

public class ControllerActivity extends BaseActivity{

    private ControllerManager controllerManager;
    private ImageView iv_connection_state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controler);
        iv_connection_state = f(R.id.iv_connection_state);
        controllerManager = new ControllerManager(this, iv_connection_state);
    }

    @Override
    protected void onStart() {
        super.onStart();
        controllerManager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        controllerManager.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controllerManager.onDestory();
    }

    @Override
    protected void c(View v) {

    }
}
