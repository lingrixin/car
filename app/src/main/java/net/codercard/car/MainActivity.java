package net.codercard.car;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.codercard.car.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private ImageView iv_connection_state;
    private TextView tv_connect;
    private FrameLayout fl_training, fl_auto;
    private SetttingsManage setttingsManage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        findView();
    }

    private void findView() {
        iv_connection_state = f(R.id.iv_connection_state);
        setttingsManage = new SetttingsManage(this,
                (TextInputLayout) findViewById(R.id.til_socket_address),
                (TextInputLayout) findViewById(R.id.til_socket_port),
                (TextInputLayout) findViewById(R.id.til_turn_angle),
                (TextInputLayout) findViewById(R.id.til_speed),
                (TextInputLayout) findViewById(R.id.til_sampling_interval),
                (EditText) findViewById(R.id.et_socket_address),
                (EditText) findViewById(R.id.et_socket_port),
                (EditText) findViewById(R.id.et_turn_angle),
                (EditText) findViewById(R.id.et_speed),
                (EditText) findViewById(R.id.et_sampling_interval),


                (TextView) findViewById(R.id.tv_setting),

                (TextView) findViewById(R.id.tv_socket_address),
                (TextView) findViewById(R.id.tv_socket_port),
                (TextView) findViewById(R.id.tv_turn_angle),
                (TextView) findViewById(R.id.tv_speed),
                (TextView) findViewById(R.id.tv_sampling_interval));
        tv_connect = f(R.id.tv_connect);
        fl_training = f(R.id.fl_training);
        fl_auto = f(R.id.fl_auto);
        click( tv_connect, fl_training, fl_auto);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setttingsManage.onStart();
    }

    @Override
    protected void c(View v) {
        switch (v.getId()) {
            case R.id.tv_connect:
                Toast.makeText(this, "点击了连接", Toast.LENGTH_SHORT).show();
                iv_connection_state.setSelected(true);
                break;
            case R.id.fl_training:
                Toast.makeText(this, "点击了训练", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,ControllerActivity.class));
                break;
            case R.id.fl_auto:
                Toast.makeText(this, "点击了自动驾驶", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,ControllerActivity.class));
                break;
            default:
                break;
        }
    }
}
