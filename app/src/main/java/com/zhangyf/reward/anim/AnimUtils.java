package com.zhangyf.reward.anim;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.zhangyf.reward.R;

/**
 * Created by zhangyf on 2017/5/26.
 */

public class AnimUtils {

    /**
     * 获取礼物入场动画
     *
     * @return
     */
    public static Animation getInAnimation(Context context) {
        return (TranslateAnimation) AnimationUtils.loadAnimation(context, R.anim.gift_in);
    }

    /**
     * 获取礼物出场动画
     *
     * @return
     */
    public static AnimationSet getOutAnimation(Context context) {
        return (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.gift_out);
    }

}
