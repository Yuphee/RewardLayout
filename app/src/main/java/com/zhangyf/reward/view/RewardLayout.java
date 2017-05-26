package com.zhangyf.reward.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zhangyf.reward.R;
import com.zhangyf.reward.bean.BaseGiftBean;
import com.zhangyf.reward.bean.SendGiftBean;
import com.zhangyf.reward.config.GiftConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by zhangyf on 2017/3/20.
 */

public class RewardLayout extends LinearLayout {

    private final int MAX_COUNT_DEFAULT = 3;
    private int MAX_GIFT_COUNT;
    private int latestIndex;
    private Context mContext;
    private Activity mActivity;
    private int childWidth;
    private int childHeight;
    private int giftItemRes;
    private List<BaseGiftBean> beans;
    private GiftListener initListener;
    private AnimationSet outAnim = null;
    private List<View> giftViewCollection = new ArrayList<View>();

    public interface GiftListener{
        View onInit(View view, BaseGiftBean bean);
        View onUpdate(View view, BaseGiftBean bean);
        void addAnim(View view);
//        void numAnim(View view);
        AnimationSet outAnim();
    }

    public GiftListener getInitListener() {
        return initListener;
    }

    public void setInitListener(GiftListener initListener) {
        this.initListener = initListener;
    }

    public RewardLayout(Context context) {
        super(context);
        init(context);
    }

    public RewardLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RewardLayout);
        MAX_GIFT_COUNT = (int) a.getInteger(R.styleable.RewardLayout_max_gift, MAX_COUNT_DEFAULT);
        init(context);
    }

    public RewardLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RewardLayout);
        MAX_GIFT_COUNT = (int) a.getDimension(R.styleable.RewardLayout_max_gift, MAX_COUNT_DEFAULT);
        init(context);
    }

    /**
     * 测量礼物view的高度和宽度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        View child = getGiftView();
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams lp = (MarginLayoutParams) child
                .getLayoutParams();
        // 当前子空间实际占据的宽度
        childWidth = child.getMeasuredWidth() + lp.leftMargin
                + lp.rightMargin;
        // 当前子空间实际占据的高度
        childHeight = child.getMeasuredHeight() + lp.topMargin
                + lp.bottomMargin;

        int totalHeight = childHeight * MAX_GIFT_COUNT;
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth
                : childWidth, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight
                : totalHeight);
    }

    private void init(Context context) {
        mContext = context;
        mActivity = (Activity) mContext;
        beans = new ArrayList<>();
//        clearTiming();
    }

    /**
     * 向rewardlayout中添加MAX_GIFT_COUNT个子framelayout
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int i = 0; i < MAX_GIFT_COUNT; i++) {
            View child = getGiftView();
            child.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            int height = child.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;
            FrameLayout linearLayout = new FrameLayout(mContext);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
            linearLayout.setLayoutParams(params);
            addView(linearLayout);
        }
    }


    /**
     * 外部调用方法，添加礼物view到rewardlayout中
     *
     * @param sBean
     */
    public void showGift(SendGiftBean sBean) {
        BaseGiftBean bean = null;
        for (BaseGiftBean baseGiftBean : beans) {
            if(baseGiftBean.getGiftId() == sBean.getGiftId() && baseGiftBean.getUserId() == sBean.getUserId()){
                bean = baseGiftBean;
            }
        }
        if(bean == null) {
            bean = generateGift(sBean);
            beans.add(bean);
        }
        BaseGiftBean mBean = null;
        View giftView = findViewWithTag(bean);
        if (giftView != null && !giftView.isEnabled()) {
            giftView = null;
        }
        if (giftView == null) {/*该用户不在礼物显示列表*/
            mBean = bean;
            if (getCurrentGiftCount() > MAX_GIFT_COUNT - 1) {/*如果正在显示的礼物的个数超过MAX_GIFT_COUNT个，那么就移除最后一次更新时间比较长的*/
                List<BaseGiftBean> list = new ArrayList();
                for (int i = 0; i < getChildCount(); i++) {
                    ViewGroup vg = (ViewGroup) getChildAt(i);
                    for(int j=0;j<vg.getChildCount();j++) {
                        if(vg.getChildAt(j).isEnabled() == true){
                            BaseGiftBean gBean = (BaseGiftBean) vg.getChildAt(j).getTag();
                            list.add(gBean.setCurrentIndex(i));
                        }
                    }
                }
                Collections.sort(list);// 根据加入时间排序所有child中giftview
                if (list.size() > 0) {
                    latestIndex = list.get(0).getCurrentIndex();
                    removeGiftViewAnim(latestIndex);
                }
                addGiftViewAnim(mBean);
            } else {
                addGiftViewAnim(mBean);
            }

        } else {
            mBean = (BaseGiftBean) giftView.getTag();
            if(initListener != null) {
                giftView = initListener.onUpdate(giftView,mBean);
            }
            // 根据GiftExistTime 准时消失，根据GiftExistTime可在配置中配置
            giftView.removeCallbacks(mBean.getClearRun());
            final View finalGiftView = giftView;
            final BaseGiftBean finalMBean = mBean;
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    removeGiftViewAnim(finalGiftView);
                }
            };
            giftView.postDelayed(run,mBean.getGiftExistTime());
            finalMBean.setClearRun(run);

            ViewGroup vg = (ViewGroup) giftView.getParent();
            vg.setTag(mBean.getLatestRefreshTime());
        }
    }

    /**
     * 从xml布局中加载礼物view
     */
    private View getGiftView() {
        View view = null;
        view = LayoutInflater.from(mContext).inflate(getGiftRes(), null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return view;

    }


    private void addGiftViewAnim(final BaseGiftBean mBean) {
        View giftView = null;

        if(initListener != null) {
            giftView = initListener.onInit(getGiftView(),mBean);
        }

        mBean.setLatestRefreshTime(System.currentTimeMillis());

        // 根据GiftExistTime 准时消失，根据GiftExistTime可在配置中配置
        final View finalGiftView = giftView;
        Runnable run = new Runnable() {
            @Override
            public void run() {
                removeGiftViewAnim(finalGiftView);
            }
        };
        finalGiftView.postDelayed(run,mBean.getGiftExistTime());
        mBean.setClearRun(run);

        giftView.setTag(mBean);
        giftView.setEnabled(true);// 标记该giftview可用

        addChildGift(giftView);// 如果送礼非常快在add之前再进来一个相同的礼物可能会出现不会叠加而跑到下面的布局中去
        invalidate();

        if(initListener != null) {
            initListener.addAnim(giftView);
        }

    }

    /**
     *
     * 删除指定framelayout下的view的礼物动画
     * @param view
     */
    private void removeGiftViewAnim(final View view) {
        if(view != null) {
            view.setEnabled(false);// 标记该giftview不可用
            if(initListener != null) {
                outAnim = initListener.outAnim();
                outAnim.setFillAfter(true);
                outAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                removeChildGift(view);
                            }
                        }, 10);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.startAnimation(outAnim);
                    }
                });
            }

        }
    }

    /**
     * 删除指定framelayout下的view的礼物动画
     * @param index
     */
    private void removeGiftViewAnim(final int index) {
        final View removeView = getChildGift(index);
        if (removeView != null) {
            removeView.setEnabled(false);// 标记该giftview不可用
            if(initListener != null) {
                outAnim = initListener.outAnim();
                outAnim.setFillAfter(true);
                outAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                removeChildGift(removeView);
                            }
                        }, 10);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        removeView.startAnimation(outAnim);
                    }
                });
            }

        }
    }


    /**
     * 定时清除礼物每秒检查一次，所以礼物的离场时间必须设置为1秒的整数倍
     */
    @Deprecated
    private void clearTiming() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    final int index = i;
                    View view = getChildAt(index);
                    if (view.getTag() != null) {
                        long nowtime = System.currentTimeMillis();
                        long upTime = (long) view.getTag();
                        if ((nowtime - upTime) >= 3000) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    removeGiftViewAnim(index);
                                }
                            });
                        }
                    }

                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 1000);
    }

    /**
     * 移除指定framelayout下面的礼物view
     * @param index
     */
    private void removeChildGift(int index) {
        if (index < getChildCount() && getChildAt(index) instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) getChildAt(index);
            if (vg.getChildCount() > 0) {
                vg.removeViewAt(0);
            }
        }
    }

    /**
     * 移除指定framelayout下面的礼物view
     * @param view
     */
    private void removeChildGift(View view) {
        for(int i=0;i<getChildCount();i++) {
            ViewGroup vg = (ViewGroup)getChildAt(i);
            final int index = vg.indexOfChild(view);
            if (index >= 0) {
                vg.removeView(view);
                BaseGiftBean bean = (BaseGiftBean)view.getTag();
                int giftId = bean.getGiftId();
                int userId = bean.getUserId();
                for (Iterator<BaseGiftBean> it = beans.iterator(); it.hasNext();) {
                    BaseGiftBean value = it.next();
                    if (value.getGiftId() == giftId && value.getUserId() == userId) {
                        it.remove();
                    }
                }
            }
        }
    }

    /**
     * 获取指定framelayout下的礼物view
     * @param index
     * @return
     */
    private View getChildGift(int index) {
        View view = null;
        ViewGroup vg = (ViewGroup) getChildAt(index);
        for (int i = 0; i < vg.getChildCount(); i++) {
            if (vg.getChildAt(i).isEnabled()) {
                view = vg.getChildAt(i);
            }
        }
        return view;
    }

    /**
     * 添加礼物到空闲的framelayout，在覆盖的时候可能存在礼物离场动画还么结束view还没有被remove的情况下
     * 根据该view的enable判断
     *
     * @param view
     */
    private void addChildGift(View view) {
        for (int i = 0; i < getChildCount(); i++) {
            if (((ViewGroup) getChildAt(i)).getChildCount() == 0) {
                ((ViewGroup) getChildAt(i)).addView(view);
                ((ViewGroup) getChildAt(i)).setTag(((BaseGiftBean) view.getTag()).getLatestRefreshTime());
                break;
            } else {
                boolean isAllCancel = true;
                for (int j = 0; j < ((ViewGroup) getChildAt(i)).getChildCount(); j++) {
                    if (((ViewGroup) getChildAt(i)).getChildAt(j).isEnabled()) {
                        isAllCancel = false;
                        break;
                    }
                }
                if (isAllCancel) {
                    ((ViewGroup) getChildAt(i)).addView(view);
                    ((ViewGroup) getChildAt(i)).setTag(((BaseGiftBean) view.getTag()).getLatestRefreshTime());
                    break;
                }
            }
        }
    }


    private int getCurrentGiftCount(){
        int count = 0;
        for(int i=0;i<getChildCount();i++){
            for(int j=0;j<((ViewGroup)getChildAt(i)).getChildCount();j++){
                if(((ViewGroup)getChildAt(i)).getChildAt(j).isEnabled() == true) {
                    count++;
                }
            }
        }
        return count;
    }

    public void setMaxGift(int max){
        MAX_GIFT_COUNT = max;
    }

    public int getMaxGiftCount(){
        return MAX_GIFT_COUNT;
    }

    public void setGiftItemRes(int res) {
        giftItemRes = res;
    }


    public BaseGiftBean generateGift(SendGiftBean sb) {
        BaseGiftBean bean = new BaseGiftBean();
        bean.setUserId(sb.getUserId());
        bean.setGiftId(sb.getGiftId());
        bean.setUserName(sb.getUserName());
        int index = 0;
        int count = GiftConfig.getInstance().getGiftCount();
        for(int i=0;i<count;i++) {
            if(GiftConfig.getInstance().getGiftIds()[i] == sb.getGiftId()){
                index = i;
            }
        }
        if(GiftConfig.getInstance().getStayTime().length != count){
            throw new IllegalArgumentException("stayTime lenth must equals giftcount");
        }
        bean.setGiftExistTime(GiftConfig.getInstance().getStayTime()[index]);

        if(GiftConfig.getInstance().getGiftNames().length != count){
            throw new IllegalArgumentException("giftname lenth must equals giftcount");
        }
        bean.setGiftName(GiftConfig.getInstance().getGiftNames()[index]);

        if(GiftConfig.getInstance().getGiftRes().length != count){
            throw new IllegalArgumentException("giftres lenth must equals giftcount");
        }
        bean.setGiftImg(GiftConfig.getInstance().getGiftRes()[index]);
        return bean;
    }

    private int getGiftRes() {
        if(giftItemRes != 0){
            return giftItemRes;
        }else {
            throw new NullPointerException("u should set gift item resource first");
        }
    }

}
