package net.codercard.car;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.codercard.car.utils.Constant;
import net.codercard.car.utils.SPUtils;

/**
 * Created by LRXx on 2018-6-17.
 */

public class SettingsActivity extends AppCompatActivity {

    private TextInputLayout til_socket_address;
    private TextInputLayout til_socket_port;
    private TextInputLayout til_turn_angle;
    private TextInputLayout til_speed;
    private TextInputLayout til_sampling_interval;

    private EditText et_socket_address;
    private EditText et_socket_port;
    private EditText et_turn_angle;
    private EditText et_speed;
    private EditText et_sampling_interval;

    private Button bt_save;

    private TextView tv_socket_address;
    private TextView tv_socket_port;
    private TextView tv_turn_angle;
    private TextView tv_speed;
    private TextView tv_sampling_interval;

    private String str_socket_address;
    private String str_socket_port;
    private String str_turn_angle;
    private String str_speed;
    private String str_sampling_interval;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findView();
        doBusiness();
    }

    private void findView() {
        til_socket_address = (TextInputLayout) findViewById(R.id.til_socket_address);
        til_socket_port = (TextInputLayout) findViewById(R.id.til_socket_port);
        til_turn_angle = (TextInputLayout) findViewById(R.id.til_turn_angle);
        til_speed = (TextInputLayout) findViewById(R.id.til_speed);
        til_sampling_interval = (TextInputLayout) findViewById(R.id.til_sampling_interval);

        et_socket_address = (EditText) findViewById(R.id.et_socket_address);
        et_socket_port = (EditText) findViewById(R.id.et_socket_port);
        et_turn_angle = (EditText) findViewById(R.id.et_turn_angle);
        et_speed = (EditText) findViewById(R.id.et_speed);
        et_sampling_interval = (EditText) findViewById(R.id.et_sampling_interval);

        bt_save = (Button) findViewById(R.id.bt_save);

        tv_socket_address = (TextView) findViewById(R.id.tv_socket_address);
        tv_socket_port = (TextView) findViewById(R.id.tv_socket_port);
        tv_turn_angle = (TextView) findViewById(R.id.tv_turn_angle);
        tv_speed = (TextView) findViewById(R.id.tv_speed);
        tv_sampling_interval = (TextView) findViewById(R.id.tv_sampling_interval);
    }

    private void doBusiness() {
        initTxt();
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveConfig()) {
                    refreshConfigUi();
                    Toast.makeText(SettingsActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshConfigUi();
    }

    private boolean saveConfig() {
        if (!checkoutEdittext()) {
            return false;
        }
        SPUtils.put(this, Constant.SOCKET_ADDRESS, et_socket_address.getText());
        SPUtils.put(this, Constant.SOCKET_PORT, et_socket_port.getText());
        SPUtils.put(this, Constant.TURN_ANGLE, et_turn_angle.getText());
        SPUtils.put(this, Constant.SPEED, et_speed.getText());
        SPUtils.put(this, Constant.SAMPLING_INTERVAL, et_sampling_interval.getText());
        return true;
    }

    private boolean checkoutEdittext() {
        if (TextUtils.isEmpty(et_socket_address.getText())) {
            showError(til_socket_address, "不可为空");
            return false;
        }
        if (TextUtils.isEmpty(et_socket_port.getText())) {
            showError(til_socket_port, "不可为空");
            return false;
        }
        if (TextUtils.isEmpty(et_turn_angle.getText())) {
            showError(til_turn_angle, "不可为空");
            return false;
        }
        if (TextUtils.isEmpty(et_speed.getText())) {
            showError(til_speed, "不可为空");
            return false;
        }
        if (TextUtils.isEmpty(et_sampling_interval.getText())) {
            showError(til_sampling_interval, "不可为空");
            return false;
        }

        return true;
    }

    private void refreshConfigUi() {
        setConfigTxt(str_socket_address, tv_socket_address, Constant.SOCKET_ADDRESS);
        setConfigTxt(str_socket_port, tv_socket_port, Constant.SOCKET_PORT);
        setConfigTxt(str_turn_angle, tv_turn_angle, Constant.TURN_ANGLE);
        setConfigTxt(str_speed, tv_speed, Constant.SPEED);
        setConfigTxt(str_sampling_interval, tv_sampling_interval, Constant.SAMPLING_INTERVAL);
    }

    private void setConfigTxt(String sstr, TextView tv, String key) {
        tv.setText(sstr + (String) SPUtils.get(this, key, ""));
    }

    private void initTxt() {
        str_socket_address = tv_socket_address.getText().toString();
        str_socket_port = tv_socket_port.getText().toString();
        str_turn_angle = tv_turn_angle.getText().toString();
        str_speed = tv_speed.getText().toString();
        str_sampling_interval = tv_sampling_interval.getText().toString();
    }

    /**
     * 显示错误提示，并获取焦点
     *
     * @param textInputLayout
     * @param error
     */
    private void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }

}
