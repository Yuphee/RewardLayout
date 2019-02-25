package com.zhangyf.reward;

import android.app.Application;

//import com.squareup.leakcanary.LeakCanary;


/**
 * Created by zhangyf on 2017/5/26.
 */

public class MApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
    }
}
