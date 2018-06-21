package net.codercard.car;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.Toast;

import net.codercard.car.base.BaseActivity;
import net.codercard.car.controler.api.IConnectStateChange;
import net.codercard.car.controler.api.ProxyObject;
import net.codercard.car.controler.serive.ControlerService;

/**
 * <pre>
 *     @author : Created by Phantom
 *     email : phantom@gradle.top‍
 *     time  : 2018/6/21.
 *     desc  : ControllerManager
 * </pre>
 */
public class ControllerManager<T extends BaseActivity> implements IConnectStateChange {
    private T activity;
    private ImageView view;
    private boolean tag = false;

    private ProxyObject proxyObject;
    private boolean connected = false;

    public ControllerManager(T activity, ImageView view) {
        this.activity = activity;
        this.view = view;
    }

    public void connect() {
        proxyObject.connect();
    }

    public void onStart() {
        activity.bindService(new Intent(activity, ControlerService.class), connection, Context.BIND_AUTO_CREATE);
    }

    public void onStop() {
        activity.unbindService(connection);
    }

    public void onDestory() {
//        proxyObject.onDestroy();
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
    public void connectOk() {
        if (!tag) {
            tag = true;
            view.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "连接成功", Toast.LENGTH_SHORT).show();
                    view.setSelected(true);
                }
            });
        }
    }

    @Override
    public void connectFailure() {
        if (tag) {
            tag = false;
            view.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "连接断开", Toast.LENGTH_SHORT).show();
                    view.setSelected(false);
                }
            });
        }
    }


}
