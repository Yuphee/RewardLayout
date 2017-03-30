package com.zhangyf.reward;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSendone;
    private TextView tvSendtwo;
    private TextView tvSendthree;
    private TextView tvSendfor;
    private TextView tvSendanother;
    private RewardLayout rewardLayout;
    private SendGiftBean bean1;
    private SendGiftBean bean2;
    private SendGiftBean bean3;
    private SendGiftBean bean4;
    private SendGiftBean bean5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rewardLayout = (RewardLayout)findViewById(R.id.llgiftcontent);
        tvSendone = (TextView)findViewById(R.id.tvSendone);
        tvSendtwo = (TextView)findViewById(R.id.tvSendtwo);
        tvSendthree = (TextView)findViewById(R.id.tvSendthree);
        tvSendfor = (TextView)findViewById(R.id.tvSendfor);
        tvSendanother = (TextView)findViewById(R.id.tvSendanother);
        tvSendone.setOnClickListener(this);
        tvSendtwo.setOnClickListener(this);
        tvSendthree.setOnClickListener(this);
        tvSendfor.setOnClickListener(this);
        tvSendanother.setOnClickListener(this);
        bean1 = new SendGiftBean(1,1,"林喵喵");
        bean2 = new SendGiftBean(2,2,"马甲");
        bean3 = new SendGiftBean(3,3,"大P神");
        bean4 = new SendGiftBean(4,4,"大枫哥");
        bean5 = new SendGiftBean(4,1,"大枫哥");
        rewardLayout.setGiftItemRes(R.layout.gift_animation_item);
        rewardLayout.setInitListener(new RewardLayout.GiftListener() {
            @Override
            public View onInit(View view, BaseGiftBean bean) {
                ImageView giftImage = (ImageView) view.findViewById(R.id.iv_gift_img);
                final TextView giftNum = (TextView) view.findViewById(R.id.tv_gift_amount);
                TextView userName = (TextView) view.findViewById(R.id.tv_user_name);
                TextView giftName = (TextView) view.findViewById(R.id.tv_gift_name);

                // 初始化数据
                giftNum.setText("x1");
                giftImage.setImageResource(bean.getGiftImg());
                userName.setText(bean.getUserName());
                giftName.setText("送出 "+bean.getGiftName());
                return view;
            }

            @Override
            public View onUpdate(View view, BaseGiftBean bean) {
                ImageView giftImage = (ImageView) view.findViewById(R.id.iv_gift_img);
                TextView giftNum = (TextView) view.findViewById(R.id.tv_gift_amount);

                int showNum = (Integer) bean.getGiftCount() + 1;
                // 刷新已存在的giftview界面数据
                giftNum.setText("x" + showNum);
                giftImage.setImageResource(bean.getGiftImg());
                // 更新tag
                bean.setGiftCount(showNum);
                bean.setLatestRefreshTime(System.currentTimeMillis());
                view.setTag(bean);
                return view;
            }

            @Override
            public void addAnim(final View view) {
                final TextView textView = (TextView) view.findViewById(R.id.tv_gift_amount);
                ImageView img = (ImageView) view.findViewById(R.id.iv_gift_img);
                Animation giftInAnim = rewardLayout.getInAnimation();// 整个giftview动画
                Animation imgInAnim = rewardLayout.getInAnimation();// 礼物图像动画
                final RewardLayout.NumAnim comboAnim = new RewardLayout.NumAnim();// 首次连击动画
                imgInAnim.setStartTime(500);
                giftInAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imgInAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        textView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        textView.setVisibility(View.VISIBLE);
                        comboAnim.start(textView);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(giftInAnim);
                img.startAnimation(imgInAnim);
            }

            @Override
            public void numAnim(View view) {
                new RewardLayout.NumAnim().start((TextView) view.findViewById(R.id.tv_gift_amount));
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvSendone:/*礼物1 林喵喵*/
                rewardLayout.showGift(bean1);
                break;
            case R.id.tvSendtwo:/*礼物2 马甲*/
                rewardLayout.showGift(bean2);
                break;
            case R.id.tvSendthree:/*礼物3 P神*/
                rewardLayout.showGift(bean3);
                break;
            case R.id.tvSendfor:/*礼物4 枫哥*/
                rewardLayout.showGift(bean4);
                break;
            case R.id.tvSendanother:/*礼物1 枫哥*/
                rewardLayout.showGift(bean5);
                break;
        }
    }

}
