package com.zhangyf.reward;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhangyf.reward.anim.AnimUtils;
import com.zhangyf.reward.anim.NumAnim;
import com.zhangyf.reward.bean.BaseGiftBean;
import com.zhangyf.reward.bean.GiftIdentify;
import com.zhangyf.reward.bean.SendGiftBean;
import com.zhangyf.reward.config.GiftConfig;
import com.zhangyf.reward.view.RewardLayout;

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
        bean1 = new SendGiftBean(1,1,"林喵喵","糖果",R.mipmap.tg,2700);
        bean2 = new SendGiftBean(2,2,"马甲","666",R.mipmap.good,3000);
        bean3 = new SendGiftBean(3,3,"小梦梦","小香蕉",R.mipmap.banana,2500);
        bean4 = new SendGiftBean(4,4,"大枫哥","鱼丸",R.mipmap.yw,2000);
        bean5 = new SendGiftBean(4,1,"大枫哥","糖果",R.mipmap.tg,2700);
        rewardLayout.setGiftItemRes(R.layout.gift_animation_item);
        rewardLayout.setGiftAdapter(new RewardLayout.GiftAdapter<SendGiftBean>() {
            @Override
            public View onInit(View view, SendGiftBean bean) {
                ImageView giftImage = (ImageView) view.findViewById(R.id.iv_gift_img);
                final TextView giftNum = (TextView) view.findViewById(R.id.tv_gift_amount);
                TextView userName = (TextView) view.findViewById(R.id.tv_user_name);
                TextView giftName = (TextView) view.findViewById(R.id.tv_gift_name);

                // 初始化数据
                giftNum.setText("x1");
                bean.setTheGiftCount(1);
                giftImage.setImageResource(bean.getGiftImg());
                userName.setText(bean.getUserName());
                giftName.setText("送出 "+bean.getGiftName());
                return view;
            }

            @Override
            public View onUpdate(View view, SendGiftBean bean) {
                ImageView giftImage = (ImageView) view.findViewById(R.id.iv_gift_img);
                TextView giftNum = (TextView) view.findViewById(R.id.tv_gift_amount);

                int showNum = (Integer) bean.getTheGiftCount() + 1;
                // 刷新已存在的giftview界面数据
                giftNum.setText("x" + showNum);
                giftImage.setImageResource(bean.getGiftImg());
                // 数字刷新动画
                new NumAnim().start(giftNum);
                // 更新tag
                bean.setTheGiftCount(showNum);
                bean.setTheLatestRefreshTime(System.currentTimeMillis());
                view.setTag(bean);
                return view;
            }

            @Override
            public void addAnim(final View view) {
                final TextView textView = (TextView) view.findViewById(R.id.tv_gift_amount);
                ImageView img = (ImageView) view.findViewById(R.id.iv_gift_img);
                // 整个giftview动画
                Animation giftInAnim = AnimUtils.getInAnimation(MainActivity.this);
                // 礼物图像动画
                Animation imgInAnim = AnimUtils.getInAnimation(MainActivity.this);
                // 首次连击动画
                final NumAnim comboAnim = new NumAnim();
                imgInAnim.setStartTime(500);
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
            public AnimationSet outAnim() {
                return AnimUtils.getOutAnimation(MainActivity.this);
            }

            @Override
            public SendGiftBean generateBean(SendGiftBean bean) {
                try {
                    return (SendGiftBean) bean.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                return null;
//                SendGiftBean newBean = new SendGiftBean();
//                newBean.setTheGiftId(bean.getTheGiftId());
//                newBean.setTheUserId(bean.getTheUserId());
//                newBean.setTheGiftStay(bean.getTheGiftStay());
//                newBean.setGiftName(bean.getGiftName());
//                newBean.setGiftImg(bean.getGiftImg());
//                newBean.setUserName(bean.getUserName());
//                return newBean;
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        rewardLayout.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        rewardLayout.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rewardLayout.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*礼物1 林喵喵*/
            case R.id.tvSendone:
                rewardLayout.showGift(bean1);
                break;
            /*礼物2 马甲*/
            case R.id.tvSendtwo:
                rewardLayout.showGift(bean2);
                break;
            /*礼物3 小梦梦*/
            case R.id.tvSendthree:
                rewardLayout.showGift(bean3);
                break;
            /*礼物4 枫哥*/
            case R.id.tvSendfor:
                rewardLayout.showGift(bean4);
                break;
            /*礼物1 枫哥*/
            case R.id.tvSendanother:
                rewardLayout.showGift(bean5);
                break;
        }
    }

}
