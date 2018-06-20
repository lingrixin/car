package net.codercard.car;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.codercard.car.base.BaseActivity;
import net.codercard.car.controler.api.IConnectStateChange;
import net.codercard.car.controler.api.ProxyObject;
import net.codercard.car.controler.serive.ControlerService;

public class MainActivity extends BaseActivity implements IConnectStateChange {

    private ImageView iv_connection_state;
    private TextView tv_connect;
    private FrameLayout fl_training, fl_auto;
    private SetttingsManage setttingsManage;
    private ProxyObject proxyObject;
    private boolean connected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        connectService();
        findView();
    }

    private void connectService() {
        Intent i = new Intent(this, ControlerService.class);
        bindService(i, connection, Context.BIND_AUTO_CREATE);
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
        click(tv_connect, fl_training, fl_auto);
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            connected = true;
            proxyObject = ((ControlerService.MyBind) iBinder).getProxy();
            bindOk();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            connected = false;
        }
    };

    private void bindOk() {
        proxyObject.setConnectStateChange(this);
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
                Log.i("phan", "点击了连接");
                if (proxyObject != null) {
                    Log.i("phan", "Main发送了连接");
                    proxyObject.connect();
                }
                break;
            case R.id.fl_training:
                startActivity(new Intent(this, ControllerActivity.class));
                break;
            case R.id.fl_auto:
                startActivity(new Intent(this, ControllerActivity.class));
                break;
            default:
                break;
        }
    }

    private boolean tag = false;

    @Override
    public void connectOk() {
        if (!tag) {
            tag = true;
            iv_connection_state.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    iv_connection_state.setSelected(true);
                }
            });
        }
    }

    @Override
    public void connectFailure() {
        if (tag) {
            tag = false;
            iv_connection_state.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "连接断开", Toast.LENGTH_SHORT).show();
                    iv_connection_state.setSelected(false);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proxyObject.onDestroy();
        unbindService(connection);
    }
}
