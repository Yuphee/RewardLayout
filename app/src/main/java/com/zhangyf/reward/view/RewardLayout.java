package com.zhangyf.reward.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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
    ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);

    public interface GiftListener {
        /**
         * 初始化
         * @param view
         * @param bean
         * @return
         */
        View onInit(View view, BaseGiftBean bean);

        /**
         * 更新
         * @param view
         * @param bean
         * @return
         */
        View onUpdate(View view, BaseGiftBean bean);

        /**
         * 添加进入动画
         * @param view
         */
        void addAnim(View view);

        /**
         * 添加退出动画
         * @return
         */
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
        startClearService();
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
            if (baseGiftBean.getGiftId() == sBean.getGiftId() && baseGiftBean.getUserId() == sBean.getUserId()) {
                bean = baseGiftBean;
            }
        }
        if (bean == null) {
            bean = generateGift(sBean);
            beans.add(bean);
        }
        BaseGiftBean mBean = null;
        View giftView = findSameUserGiftView(bean);
        /*该用户不在礼物显示列表*/
        if (giftView == null) {
            mBean = bean;
            /*如果正在显示的礼物的个数超过MAX_GIFT_COUNT个，那么就移除最后一次更新时间比较长的*/
            if (getCurrentGiftCount() > MAX_GIFT_COUNT - 1) {
                List<BaseGiftBean> list = new ArrayList();
                for (int i = 0; i < getChildCount(); i++) {
                    ViewGroup vg = (ViewGroup) getChildAt(i);
                    for (int j = 0; j < vg.getChildCount(); j++) {
                        if (vg.getChildAt(j).isEnabled()) {
                            BaseGiftBean gBean = (BaseGiftBean) vg.getChildAt(j).getTag();
                            list.add(gBean.setCurrentIndex(i));
                        }
                    }
                }
                // 根据加入时间排序所有child中giftview
                Collections.sort(list);
                if (list.size() > 0) {
//                    latestIndex = list.get(0).getCurrentIndex();
//                    removeGiftViewAnim(latestIndex);
                    removeGiftViewAnim(findSameUserGiftView(list.get(0)));
                }
                addGiftViewAnim(mBean);
            } else {
                addGiftViewAnim(mBean);
            }

        } else {
            if(giftView.isEnabled()) {
                mBean = (BaseGiftBean) giftView.getTag();
                if (initListener != null) {
                    giftView = initListener.onUpdate(giftView, mBean);
                }
                // 根据GiftExistTime 准时消失，根据GiftExistTime可在配置中配置
//            giftView.removeCallbacks(mBean.getClearRun());
//            final View finalGiftView = giftView;
//            final BaseGiftBean finalMBean = mBean;
//            Runnable run = new Runnable() {
//                @Override
//                public void run() {
//                    removeGiftViewAnim(finalGiftView);
//                }
//            };
//            giftView.postDelayed(run, mBean.getGiftExistTime());
//            finalMBean.setClearRun(run);
                mBean.setLatestRefreshTime(System.currentTimeMillis());
                giftView.setTag(mBean);
                ViewGroup vg = (ViewGroup) giftView.getParent();
                vg.setTag(mBean.getLatestRefreshTime());
            }
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

        if (initListener != null) {
            giftView = initListener.onInit(getGiftView(), mBean);
        }

        mBean.setLatestRefreshTime(System.currentTimeMillis());

        // 根据GiftExistTime 准时消失，根据GiftExistTime可在配置中配置
//        final View finalGiftView = giftView;
//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//                removeGiftViewAnim(finalGiftView);
//            }
//        };
//        finalGiftView.postDelayed(run, mBean.getGiftExistTime());
//        mBean.setClearRun(run);

        giftView.setTag(mBean);
        // 标记该giftview可用
        giftView.setEnabled(true);

        addChildGift(giftView);
        invalidate();

        if (initListener != null) {
            initListener.addAnim(giftView);
        }

    }

    /**
     * 删除指定framelayout下的view的礼物动画
     *
     * @param view
     */
    private void removeGiftViewAnim(final View view) {
        if (view != null) {
            // 标记该giftview不可用
            view.setEnabled(false);
            if (initListener != null) {
                outAnim = initListener.outAnim();
                outAnim.setFillAfter(true);
                outAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                removeChildGift(view);
                            }
                        });

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
     *
     * @param index
     */
    private void removeGiftViewAnim(final int index) {
        final View removeView = getChildGift(index);
        if (removeView != null) {
            // 标记该giftview不可用
            removeView.setEnabled(false);
            if (initListener != null) {
                outAnim = initListener.outAnim();
                outAnim.setFillAfter(true);
                outAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                removeChildGift(removeView);
                            }
                        });

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
     * 定时清除礼物
     */
    private void startClearService() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    final int index = i;
                    ViewGroup viewG = (ViewGroup) getChildAt(index);
                    for (int j = 0; j < viewG.getChildCount(); j++) {
                        View view = viewG.getChildAt(j);
                        if (view.getTag() != null && view.isEnabled()) {
                            BaseGiftBean tag = (BaseGiftBean) view.getTag();
                            long nowtime = System.currentTimeMillis();
                            long upTime = tag.getLatestRefreshTime();
                            if ((nowtime - upTime) >= tag.getGiftExistTime()) {
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
            }
        };
        ses.scheduleWithFixedDelay(task, 0, 20, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(ses != null) {
            ses.shutdownNow();
            ses = null;
        }
    }

    /**
     * 移除指定framelayout下面的礼物view
     *
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
     *
     * @param view
     */
    private void removeChildGift(View view) {
        for (int i = 0; i < getChildCount(); i++) {
            ViewGroup vg = (ViewGroup) getChildAt(i);
            final int index = vg.indexOfChild(view);
            if (index >= 0) {
                BaseGiftBean bean = (BaseGiftBean) view.getTag();
                int giftId = bean.getGiftId();
                int userId = bean.getUserId();
                for (Iterator<BaseGiftBean> it = beans.iterator(); it.hasNext(); ) {
                    BaseGiftBean value = it.next();
                    if (value.getGiftId() == giftId && value.getUserId() == userId) {
                        it.remove();
                    }
                }
                vg.removeView(view);
                view = null;
            }
        }
    }

    /**
     * 获取指定framelayout下的礼物view
     *
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
                getChildAt(i).setTag(((BaseGiftBean) view.getTag()).getLatestRefreshTime());
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
                    getChildAt(i).setTag(((BaseGiftBean) view.getTag()).getLatestRefreshTime());
                    break;
                }
            }
        }
    }

    /**
     * 找出相同人相同礼物
     * @param target
     * @return
     */
    private View findSameUserGiftView(BaseGiftBean target) {
        int targetGiftId = -1;
        int targetUserId = -1;
        if (target != null) {
            targetGiftId = target.getGiftId();
            targetUserId = target.getUserId();
        }
        for (int i = 0; i < getChildCount(); i++) {
            for (int j = 0; j < ((ViewGroup) getChildAt(i)).getChildCount(); j++) {
                BaseGiftBean rGiftBean = (BaseGiftBean) ((ViewGroup) getChildAt(i)).getChildAt(j).getTag();
                if (rGiftBean != null && rGiftBean.getGiftId() == targetGiftId && rGiftBean.getUserId() == targetUserId) {
                    return ((ViewGroup) getChildAt(i)).getChildAt(j);
                }
            }
        }
        return null;
    }

    /**
     * 获取当前在显示的礼物数量
     * @return
     */
    private int getCurrentGiftCount() {
        int count = 0;
        for (int i = 0; i < getChildCount(); i++) {
            for (int j = 0; j < ((ViewGroup) getChildAt(i)).getChildCount(); j++) {
                if (((ViewGroup) getChildAt(i)).getChildAt(j).isEnabled() == true) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 生成礼物对象
     * @param sb
     * @return
     */
    public BaseGiftBean generateGift(SendGiftBean sb) {
        BaseGiftBean bean = new BaseGiftBean();
        bean.setUserId(sb.getUserId());
        bean.setGiftId(sb.getGiftId());
        bean.setUserName(sb.getUserName());
        int index = 0;
        int count = GiftConfig.getInstance().getGiftCount();
        for (int i = 0; i < count; i++) {
            if (GiftConfig.getInstance().getGiftIds()[i] == sb.getGiftId()) {
                index = i;
            }
        }
        if (GiftConfig.getInstance().getStayTime().length != count) {
            throw new IllegalArgumentException("stayTime lenth must equals giftcount");
        }
        bean.setGiftExistTime(GiftConfig.getInstance().getStayTime()[index]);

        if (GiftConfig.getInstance().getGiftNames().length != count) {
            throw new IllegalArgumentException("giftname lenth must equals giftcount");
        }
        bean.setGiftName(GiftConfig.getInstance().getGiftNames()[index]);

        if (GiftConfig.getInstance().getGiftRes().length != count) {
            throw new IllegalArgumentException("giftres lenth must equals giftcount");
        }
        bean.setGiftImg(GiftConfig.getInstance().getGiftRes()[index]);
        return bean;
    }

    private int getGiftRes() {
        if (giftItemRes != 0) {
            return giftItemRes;
        } else {
            throw new NullPointerException("u should set gift item resource first");
        }
    }

    public void setMaxGift(int max) {
        MAX_GIFT_COUNT = max;
    }

    public int getMaxGiftCount() {
        return MAX_GIFT_COUNT;
    }

    public void setGiftItemRes(int res) {
        giftItemRes = res;
    }

}
