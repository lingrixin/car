package net.codercard.car;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_setting, bt_connection, bt_mode1, bt_mode2, bt_open;

    private View v_hint;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
    }

    private void findView() {
        bt_setting = (Button) findViewById(R.id.bt_setting);
        bt_connection = (Button) findViewById(R.id.bt_connection);
        bt_mode1 = (Button) findViewById(R.id.bt_mode1);
        bt_mode2 = (Button) findViewById(R.id.bt_mode2);
        bt_open = (Button) findViewById(R.id.bt_open);
        v_hint = (View) findViewById(R.id.v_hint);
        pb = (ProgressBar) findViewById(R.id.pb);
        bt_setting.setOnClickListener(this);
        bt_connection.setOnClickListener(this);
        bt_mode1.setOnClickListener(this);
        bt_mode2.setOnClickListener(this);
        bt_open.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.bt_connection:
                connectionGo();
                break;
            case R.id.bt_mode1:
                Toast.makeText(this, "打开模式1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_mode2:
                Toast.makeText(this, "打开模式2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_open:
                startActivity(new Intent(this, ControllerActivity.class));
                break;
            default:
                break;
        }
    }

    private void connectionGo() {
        v_hint.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                v_hint.post(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.INVISIBLE);
                        v_hint.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }
}
