package com.joker.permissions4m.other;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by joker on 2017/7/27.
 */

public class App extends Application {
    private static App instance;

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
