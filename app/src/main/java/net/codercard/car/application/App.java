package net.codercard.car.application;

import android.app.Application;
import android.content.Intent;

import net.codercard.car.controler.serive.ControlerService;

/**
 * <pre>
 *     @author : Created by Phantom
 *     email : phantom@gradle.top‍
 *     time  : 2018/6/21.
 *     desc  : App
 * </pre>
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, ControlerService.class));
    }
}
