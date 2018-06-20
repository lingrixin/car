package net.codercard.car;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.codercard.car.utils.Constant;
import net.codercard.car.utils.SPUtils;

/**
 * <pre>
 *     @author : Created by Phantom
 *     email : phantom@gradle.top‍
 *     time  : 2018/6/19.
 *     desc  : SetttingsManage
 * </pre>
 */
public class SetttingsManage {

    private Context context;
    private TextInputLayout til_socket_address, til_socket_port, til_turn_angle, til_speed, til_sampling_interval;
    private EditText et_socket_address, et_socket_port, et_turn_angle, et_speed, et_sampling_interval;
    private TextView tv_socket_address, tv_socket_port, tv_turn_angle, tv_speed, tv_sampling_interval, tv_setting;
    private String str_socket_address, str_socket_port, str_turn_angle, str_speed, str_sampling_interval;

    public SetttingsManage(Context context,
                           TextInputLayout til_socket_address,
                           TextInputLayout til_socket_port,
                           TextInputLayout til_turn_angle,
                           TextInputLayout til_speed,
                           TextInputLayout til_sampling_interval,
                           EditText et_socket_address,
                           EditText et_socket_port,
                           EditText et_turn_angle,
                           EditText et_speed,
                           EditText et_sampling_interval,
                           TextView tv_setting,
                           TextView tv_socket_address,
                           TextView tv_socket_port,
                           TextView tv_turn_angle,
                           TextView tv_speed,
                           TextView tv_sampling_interval
    ) {
        this.context = context;
        this.til_socket_address = til_socket_address;
        this.til_socket_port = til_socket_port;
        this.til_turn_angle = til_turn_angle;
        this.til_speed = til_speed;
        this.til_sampling_interval = til_sampling_interval;
        this.et_socket_address = et_socket_address;
        this.et_socket_port = et_socket_port;
        this.et_turn_angle = et_turn_angle;
        this.et_speed = et_speed;
        this.et_sampling_interval = et_sampling_interval;
        this.tv_setting = tv_setting;
        this.tv_socket_address = tv_socket_address;
        this.tv_socket_port = tv_socket_port;
        this.tv_turn_angle = tv_turn_angle;
        this.tv_speed = tv_speed;
        this.tv_sampling_interval = tv_sampling_interval;
        init();
    }

    private void init() {
        initTxt();
        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveConfig()) {
                    refreshConfigUi();
                    Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initTxt() {
        str_socket_address = tv_socket_address.getText().toString();
        str_socket_port = tv_socket_port.getText().toString();
        str_turn_angle = tv_turn_angle.getText().toString();
        str_speed = tv_speed.getText().toString();
        str_sampling_interval = tv_sampling_interval.getText().toString();
    }

    private void refreshConfigUi() {
        setConfigTxt(str_socket_address, tv_socket_address, Constant.SOCKET_ADDRESS);
        setConfigTxt(str_socket_port, tv_socket_port, Constant.SOCKET_PORT);
        setConfigTxt(str_turn_angle, tv_turn_angle, Constant.TURN_ANGLE);
        setConfigTxt(str_speed, tv_speed, Constant.SPEED);
        setConfigTxt(str_sampling_interval, tv_sampling_interval, Constant.SAMPLING_INTERVAL);
    }

    private void setConfigTxt(String sstr, TextView tv, String key) {
        tv.setText(sstr + (String) SPUtils.get(context, key, ""));
    }

    public void onStart() {
        refreshConfigUi();
    }

    private boolean saveConfig() {
        if (!checkoutEdittext()) {
            return false;
        }
        SPUtils.put(context, Constant.SOCKET_ADDRESS, et_socket_address.getText());
        SPUtils.put(context, Constant.SOCKET_PORT, et_socket_port.getText());
        SPUtils.put(context, Constant.TURN_ANGLE, et_turn_angle.getText());
        SPUtils.put(context, Constant.SPEED, et_speed.getText());
        SPUtils.put(context, Constant.SAMPLING_INTERVAL, et_sampling_interval.getText());
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
    private void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }
}
