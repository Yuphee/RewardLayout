# RewardLayout
仿斗鱼送礼物效果
> 关于我，欢迎关注  
  邮箱：437220638@qq.com
  如果对你有点帮助的话，点个star哦~
 
## Screenshots
![image](/screenshots/photo.gif)

## Statement
界面和礼物item布局以及动画都可自定义传入，定义好自己的BaseGiftBean及SendGiftBean可轻松实现自定义的效果，最大礼物数和礼物停留时间都可在xml上或者代码直接定义，具体接入请参考demo

## 快速预览
Activity
 ```java
    bean1 = new SendGiftBean(1,1,"林喵喵");// 构建发送礼物的bean包含礼物id，name和userid,username
    bean2 = new SendGiftBean(2,2,"马甲");
    bean3 = new SendGiftBean(3,3,"大P神");
    bean4 = new SendGiftBean(4,4,"大枫哥");
    bean5 = new SendGiftBean(4,1,"大枫哥");
    rewardLayout.setGiftItemRes(R.layout.gift_animation_item);//设置礼物item布局
    rewardLayout.setInitListener(new RewardLayout.GiftListener() {// 初始化，更新，载入动画，
    //以及数字动画的回调，可以自定义
        @Override
        public View onInit(View view, BaseGiftBean bean) {
            ...//参考 demo
            return view;
        }

        @Override
        public View onUpdate(View view, BaseGiftBean bean) {
            ...//参考 demo
            return view;
        }

        @Override
        public void addAnim(final View view) {
           ...//参考 demo
        }

        @Override
        public void numAnim(View view) {
           ...//参考 demo
        }
    });
        
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
```
XML
 ```java
<com.zhangyf.reward.RewardLayout
        android:id="@+id/llgiftcontent"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="100dp"
        android:animateLayoutChanges="true"
        app:max_gift="3"
        app:gift_dur="3000"
        android:orientation="vertical" />
```
Config
 ```java
 public class GiftConfig {
    public static final int giftCount = 4;
    public static final int[] giftIds = new int[] {1,2,3,4};
    public static final int[] giftRes = new int[] {R.mipmap.tg,R.mipmap.good,R.mipmap.banana,R.mipmap.yw};
    public static final String[] giftNames = new String[] {"糖果","666","小香蕉","鱼丸"};
    public static final long[] stayTime = new long[] {2000,2500,2700,3000};
}
 ```
## Todo
由于时间匆忙，界面，动画，数据的抽离，配置还比较生硬，接入并不是非常简洁，每个动画还没来得及写不同的停留时间逻辑，礼物自动消失，没有严格按照停留时间执行，采用的是timer每秒循环检查，这些问题下几个版本会慢慢提升，另外如果又发现任何Bug或者改进的意见欢迎提issue或者邮件#，#

## Thanks
感谢许同学提供的切图
