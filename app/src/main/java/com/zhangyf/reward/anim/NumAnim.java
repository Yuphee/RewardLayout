package com.zhangyf.reward.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by zhangyf on 2017/5/26.
 * 连击数字放大动画
 */

public class NumAnim {

    private Animator lastAnimator = null;

    public void start(View view) {
        if (lastAnimator != null) {
            lastAnimator.removeAllListeners();
            lastAnimator.end();
            lastAnimator.cancel();
        }
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "scaleX", 1.6f, 1.0f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "scaleY", 1.6f, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        lastAnimator = animSet;
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animX, animY);
        animSet.start();
    }

}
