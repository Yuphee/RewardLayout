package com.zhangyf.reward;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangyf.gift.RewardLayout;
import com.zhangyf.gift.bean.GiftIdentify;
import com.zhangyf.reward.anim.AnimUtils;
import com.zhangyf.reward.anim.NumAnim;
import com.zhangyf.reward.bean.SendGiftBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvUpdate;
    private TextView tvSendzero;
    private TextView tvSendone;
    private TextView tvSendtwo;
    private TextView tvSendthree;
    private TextView tvSendfor;
    private TextView tvSendanother;
    private RewardLayout rewardLayout;
    private SendGiftBean bean0;
    private SendGiftBean bean1;
    private SendGiftBean bean2;
    private SendGiftBean bean3;
    private SendGiftBean bean4;
    private SendGiftBean bean5;
    private List<SendGiftBean> giftList = new ArrayList<>();
    private Button btnAutoSend;
    private Button btnAutoCancel;
    private Timer timer;
    private int count = 0;
    private boolean isStart;


    private void initViews() {
        rewardLayout = findViewById(R.id.llgiftcontent);
        tvUpdate = findViewById(R.id.btn_update_tg_m);
        tvSendzero = findViewById(R.id.tvSendzero);
        tvSendone = findViewById(R.id.tvSendone);
        tvSendtwo = findViewById(R.id.tvSendtwo);
        tvSendthree = findViewById(R.id.tvSendthree);
        tvSendfor = findViewById(R.id.tvSendfor);
        tvSendanother = findViewById(R.id.tvSendanother);
        btnAutoSend = findViewById(R.id.btn_auto_send);
        btnAutoCancel = findViewById(R.id.btn_auto_cancel);
        tvUpdate.setOnClickListener(this);
        tvSendzero.setOnClickListener(this);
        tvSendone.setOnClickListener(this);
        tvSendtwo.setOnClickListener(this);
        tvSendthree.setOnClickListener(this);
        tvSendfor.setOnClickListener(this);
        tvSendanother.setOnClickListener(this);
    }

    private void initData() {
        bean0 = new SendGiftBean(1, 1, "林喵喵", "糖果", R.mipmap.tg, 2700);
        bean0.setTheSendGiftSize(2);
        bean1 = new SendGiftBean(1, 1, "林喵喵", "糖果", R.mipmap.tg, 2700);
        bean2 = new SendGiftBean(2, 2, "马甲", "666", R.mipmap.good, 3000);
        bean3 = new SendGiftBean(3, 3, "小梦梦", "小香蕉", R.mipmap.banana, 2500);
        bean4 = new SendGiftBean(4, 4, "大枫哥", "鱼丸", R.mipmap.yw, 2000);
        bean5 = new SendGiftBean(4, 1, "大枫哥", "糖果", R.mipmap.tg, 2700);
        giftList.add(bean1);
        giftList.add(bean2);
        giftList.add(bean3);
        giftList.add(bean4);
        giftList.add(bean5);
        timer = new Timer();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViews();

        rewardLayout.setGiftAdapter(new RewardLayout.GiftAdapter<SendGiftBean>() {
            @Override
            public View onInit(View view, SendGiftBean bean) {
                ImageView giftImage = (ImageView) view.findViewById(R.id.iv_gift_img);
                final TextView giftNum = (TextView) view.findViewById(R.id.tv_gift_amount);
                TextView userName = (TextView) view.findViewById(R.id.tv_user_name);
                TextView giftName = (TextView) view.findViewById(R.id.tv_gift_name);

                // 初始化数据
                giftNum.setText("x" + bean.getTheSendGiftSize());
                bean.setTheGiftCount(bean.getTheSendGiftSize());
                giftImage.setImageResource(bean.getGiftImg());
                userName.setText(bean.getUserName());
                giftName.setText("送出 " + bean.getGiftName());
                return view;
            }

            @Override
            public View onUpdate(View view, SendGiftBean o, SendGiftBean t) {
                ImageView giftImage = (ImageView) view.findViewById(R.id.iv_gift_img);
                TextView giftNum = (TextView) view.findViewById(R.id.tv_gift_amount);

                int showNum = (Integer) o.getTheGiftCount() + o.getTheSendGiftSize();
                // 刷新已存在的giftview界面数据
                giftNum.setText("x" + showNum);
                giftImage.setImageResource(o.getGiftImg());
                // 数字刷新动画
                new NumAnim().start(giftNum);
                // 更新累计礼物数量
                o.setTheGiftCount(showNum);
                // 更新其它自定义字段
//              o.setUserName(t.getUserName());
                return view;
            }

            @Override
            public void onKickEnd(SendGiftBean bean) {
                Log.e("zyfff", "onKickEnd:" + bean.getTheGiftId() + "," + bean.getGiftName() + "," + bean.getUserName() + "," + bean.getTheGiftCount());
            }

            @Override
            public void onComboEnd(SendGiftBean bean) {
//                Log.e("zyfff","onComboEnd:"+bean.getTheGiftId()+","+bean.getGiftName()+","+bean.getUserName()+","+bean.getTheGiftCount());
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
            public boolean checkUnique(SendGiftBean o, SendGiftBean t) {
                return o.getTheGiftId() == t.getTheGiftId() && o.getTheUserId() == t.getTheUserId();
            }


            @Override
            public SendGiftBean generateBean(SendGiftBean bean) {
                try {
                    return (SendGiftBean) bean.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        btnAutoSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isStart) {
                    if (timer == null) {
                        timer = new Timer();
                    }
                    isStart = true;
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            rewardLayout.put(giftList.get(new Random().nextInt(giftList.size())));
                            Log.e("zyfff", "send count:" + count++);
                        }
                    }, 0, 100);
                }
            }
        });
        btnAutoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer != null) {
                    timer.cancel();
                    Toast.makeText(MainActivity.this,"队列任有数据",Toast.LENGTH_SHORT).show();
                }
                timer = null;
                isStart = false;
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (rewardLayout != null) {
            rewardLayout.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rewardLayout != null) {
            rewardLayout.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rewardLayout != null) {
            rewardLayout.onDestroy();
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*礼物1 林喵喵x2*/
            case R.id.btn_update_tg_m:
                rewardLayout.updateRefreshTime(bean1,2000);
                break;
            case R.id.tvSendzero:
                rewardLayout.put(bean0);
                break;
            /*礼物1 林喵喵*/
            case R.id.tvSendone:
                rewardLayout.put(bean1);
                break;
            /*礼物2 马甲*/
            case R.id.tvSendtwo:
                rewardLayout.put(bean2);
                break;
            /*礼物3 小梦梦*/
            case R.id.tvSendthree:
                rewardLayout.put(bean3);
                break;
            /*礼物4 枫哥*/
            case R.id.tvSendfor:
                rewardLayout.put(bean4);
                break;
            /*礼物1 枫哥*/
            case R.id.tvSendanother:
                rewardLayout.put(bean5);
                break;
            default:
                break;
        }
    }

}
