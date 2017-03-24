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
    private RewardLayout rewardLayout;
    private BaseGiftBean bean1;
    private BaseGiftBean bean2;
    private BaseGiftBean bean3;
    private BaseGiftBean bean4;
    private List<BaseGiftBean> beans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rewardLayout = (RewardLayout)findViewById(R.id.llgiftcontent);
        tvSendone = (TextView)findViewById(R.id.tvSendone);
        tvSendtwo = (TextView)findViewById(R.id.tvSendtwo);
        tvSendthree = (TextView)findViewById(R.id.tvSendthree);
        tvSendfor = (TextView)findViewById(R.id.tvSendfor);
        tvSendone.setOnClickListener(this);
        tvSendtwo.setOnClickListener(this);
        tvSendthree.setOnClickListener(this);
        tvSendfor.setOnClickListener(this);
        beans = new ArrayList<>();
        bean1 = new BaseGiftBean();
        bean1.setGiftId(1);
        bean1.setGiftName("礼物1");
        bean1.setGiftImg(R.mipmap.img_chat_gift_1);
        bean2 = new BaseGiftBean();
        bean2.setGiftId(2);
        bean2.setGiftName("礼物2");
        bean2.setGiftImg(R.mipmap.img_chat_gift_2);
        bean3 = new BaseGiftBean();
        bean3.setGiftId(3);
        bean3.setGiftName("礼物3");
        bean3.setGiftImg(R.mipmap.img_chat_gift_3);
        bean4 = new BaseGiftBean();
        bean4.setGiftId(4);
        bean4.setGiftName("礼物4");
        bean4.setGiftImg(R.mipmap.img_chat_gift_4);
        beans.add(bean1);
        beans.add(bean2);
        beans.add(bean3);
        beans.add(bean4);
        rewardLayout.setGiftItemRes(R.layout.gift_animation_item);
        rewardLayout.setGiftBeans(beans);
        rewardLayout.setInitListener(new RewardLayout.GiftListener() {
            @Override
            public View onInit(View view, BaseGiftBean bean) {
                ImageView giftImage = (ImageView) view.findViewById(R.id.iv_gift_img);
                final TextView giftNum = (TextView) view.findViewById(R.id.tv_gift_amount);

                // 初始化数据
                giftNum.setText("x1");
                giftImage.setImageResource(bean.getGiftImg());
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

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        comboAnim.start((TextView) view.findViewById(R.id.tv_gift_amount));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(giftInAnim);
                ((ImageView) view.findViewById(R.id.iv_gift_img)).startAnimation(imgInAnim);
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
            case R.id.tvSendone:/*礼物1*/
                rewardLayout.showGift(1);
                break;
            case R.id.tvSendtwo:/*礼物2*/
                rewardLayout.showGift(2);
                break;
            case R.id.tvSendthree:/*礼物3*/
                rewardLayout.showGift(3);
                break;
            case R.id.tvSendfor:/*礼物4*/
                rewardLayout.showGift(4);
                break;
        }
    }

}
