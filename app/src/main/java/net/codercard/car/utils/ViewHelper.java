package net.codercard.car.utils;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;

import net.codercard.car.R;

/**
 * <pre>
 *     @author : Created by Phantom
 *     email : phantom@gradle.top‍
 *     time  : 2018/6/19.
 *     desc  : ViewHelper
 * </pre>
 */
public class ViewHelper {
    public static <V extends View, T extends Activity> V f(T contect, @IdRes int id) {
        return contect.findViewById(id);
    }

    public static <V extends View> void click(View.OnClickListener listener, V... views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }
}
