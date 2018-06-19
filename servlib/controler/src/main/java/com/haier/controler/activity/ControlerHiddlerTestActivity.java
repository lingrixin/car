package com.haier.controler.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.shining.libutils.utilslib.data.SpUtils;
import com.haier.controler.R;
import com.haier.controler.api.CommandIndex;
import com.haier.controler.api.ProxyObject;
import com.haier.controler.serive.ControlerService;
import com.haier.controler.utils.ControlerLogUtil;
import com.haier.controler.utils.PrintUtil;
import com.haier.controler.view.CircularSeekBar;

import java.util.Observable;
import java.util.Observer;

/**
 * <pre>
 *     @author: Created by Phantom
 *     @eamil : phantom@gradle.top‍
 *     @time  : 2018/3/9.
 *     @desc  : ControlerHiddlerTestActivity
 * </pre>
 */
public class ControlerHiddlerTestActivity extends Activity implements Observer, CompoundButton.OnCheckedChangeListener {

    ProxyObject proxyObject;
    TextView tv_wine_type,
            tv_response,
            tv_cool_show,
            tv_env_show,
            tv_cool_set,
            tv_cool_real,
            tv_env_real,
            tv_temp_mode,
            tv_light,
            tv_chulu,
            tv_power,
            tv_cold_state,
            tv_anxi,
            tv_shop,
            tv_temp_set;
    CircularSeekBar circularSeekBar;
    CheckBox cb_light;
    int spTemp;
    boolean spLight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controler_test);
        findViewById(R.id.bu_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_wine_type = (TextView) findViewById(R.id.tv_wine_type);
        tv_response = (TextView) findViewById(R.id.tv_response);

        tv_cool_show = (TextView) findViewById(R.id.tv_cool_show);
        tv_env_show = (TextView) findViewById(R.id.tv_env_show);
        tv_cool_set = (TextView) findViewById(R.id.tv_cool_set);
        tv_cool_real = (TextView) findViewById(R.id.tv_cool_real);
        tv_env_real = (TextView) findViewById(R.id.tv_env_real);

        tv_temp_mode = (TextView) findViewById(R.id.tv_temp_mode);
        tv_light = (TextView) findViewById(R.id.tv_light);
        tv_chulu = (TextView) findViewById(R.id.tv_chulu);
        tv_power = (TextView) findViewById(R.id.tv_power);
        tv_cold_state = (TextView) findViewById(R.id.tv_cold_state);
        tv_anxi = (TextView) findViewById(R.id.tv_anxi);
        tv_shop = (TextView) findViewById(R.id.tv_shop);
        tv_temp_set = (TextView) findViewById(R.id.tv_temp_set);

        circularSeekBar = (CircularSeekBar) findViewById(R.id.circularSeekBar);

        cb_light = (CheckBox) findViewById(R.id.cb_light);

        tv_wine_type.setText(Build.DISPLAY);
        spTemp = (int) SpUtils.getInstance(this).get(CommandIndex.KEY_BOX_TEMP, CommandIndex.SET_BOX_TEMP_DEFAULT);
        spLight = (boolean) SpUtils.getInstance(this).get(CommandIndex.KEY_LIGHT_STATE, false);
        tv_temp_set.setText(PrintUtil.addTempTag(spTemp));
        circularSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        cb_light.setOnCheckedChangeListener(this);

        bindService(new Intent(this, ControlerService.class), connection, BIND_AUTO_CREATE);
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            proxyObject = ((ControlerService.MyBind) service).getProxy();
            bindOk();
            Log.i(CommandIndex.TAG, "bind ok");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(CommandIndex.TAG, "bind error");
        }
    };

    private void bindOk() {
        proxyObject.addObserver(this);
        cb_light.setChecked(spLight);
        circularSeekBar.setProgress(spTemp - 5);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proxyObject.deleteObserver(this);
        unbindService(connection);
    }

    @Override
    public void update(Observable o, Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_response.setText(proxyObject.getWineState().getResponse_code());

                tv_cool_show.setText(PrintUtil.addTempTag(proxyObject.getWineState().getCenColdShowTemp()));
                tv_env_show.setText(PrintUtil.addTempTag(proxyObject.getWineState().getCenEnvShowTemp()));
                tv_cool_set.setText(PrintUtil.addTempTag(proxyObject.getWineState().getCenColdSetTemp()));
                tv_cool_real.setText(PrintUtil.addTempTag(proxyObject.getWineState().getCenColdTrueTemp()));
                tv_env_real.setText(PrintUtil.addTempTag(proxyObject.getWineState().getCenEnvTrueTemp()));

                tv_temp_mode.setText(proxyObject.getWineState().isDegreeeOn() ? "摄氏度模式" : "华氏度模式");
                tv_light.setText(PrintUtil.getStr(proxyObject.getWineState().isLightOn()));
                tv_chulu.setText(PrintUtil.getStr(proxyObject.getWineState().isDewOn()));
                tv_power.setText(PrintUtil.getStr(proxyObject.getWineState().isPowerOn()));
                tv_cold_state.setText(PrintUtil.getStr(proxyObject.getWineState().isColdOn()));
                tv_anxi.setText(PrintUtil.getStr(proxyObject.getWineState().isSabbathOn()));
                tv_shop.setText(PrintUtil.getStr(proxyObject.getWineState().isShopOn()));


            }
        });
    }

    CircularSeekBar.OnCircularSeekBarChangeListener seekBarChangeListener = new CircularSeekBar.OnCircularSeekBarChangeListener() {

        @Override
        public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
            tv_temp_set.setText(PrintUtil.calcTemp(progress));
        }

        @Override
        public void onStopTrackingTouch(CircularSeekBar seekBar) {
            if (proxyObject != null) {
                proxyObject.celsiusSetFreeze(seekBar.getProgress() + 5);
            }
        }

        @Override
        public void onStartTrackingTouch(CircularSeekBar seekBar) {
        }
    };


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (proxyObject != null) {
            if (isChecked) {
                proxyObject.openLight();
            } else {
                proxyObject.closeLight();
            }
        }
    }
}
