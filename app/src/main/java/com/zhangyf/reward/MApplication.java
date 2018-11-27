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
//        GiftConfig.getInstance()
//                .setGiftCount(4)
//                .setGiftIds(new int[] {1,2,3,4})
//                .setGiftNames(new String[] {"糖果","666","小香蕉","大鱼丸"})
//                .setGiftRes(new int[] {R.mipmap.tg,R.mipmap.good,R.mipmap.banana,R.mipmap.yw})
//                .setStayTimes(new long[] {2000,2500,5200,2700});
    }
}
