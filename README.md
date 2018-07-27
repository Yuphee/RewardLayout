# RewardLayout
仿斗鱼送礼物效果<br>
本项目旨在提供实现参考，交流学习。<br>
> 关于我，欢迎关注  
  邮箱：437220638@qq.com<br>
  有问题及时issue pr 或 email<br>
  如果对你有点帮助的话，点个star哦~
 
## Screenshots
效果展示：<br><br>
![image](/screenshots/photo.gif) ![image](/screenshots/photo2.gif)

## Getting started
Add it in your root build.gradle at the end of repositories:
 ```java
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
 ```
 Add the dependency
  ```java
 dependencies {
        implementation 'com.github.Yuphee:RewardLayout:1.0.1'
}
 ```

## Apk
[Demo体验](/sample.apk)

## Statement
**1.关于直播间布局**<br>
    可以采用Activity+(Verticalviewpager)+LiveFragment+viewpager(EmptyFragment+LayerFragment);<br>
如果需要上下滑动切换直播间可以在activity布局最外层套上verticalviewpager，可以参考我的另一个关于兼容上下左右滑动的Viewpager项目[Bidirectio](https://github.com/Yuphee/Bidirectio)直播页单独在LiveFragment实现,HorizontalViewpager覆盖在LiveFragment之上HorizontalViewpager中包含2个Fragment，一个是空的透明页，用于左右滑动实现沉浸式效果,另一个则是遮罩层，包含用户列表，头像，评论，礼物布局等一些悬浮在直播TextureView之上的布局<br><br>
**2.关于RewardLayout**<br>
    可自定义礼物item布局，动画，最大条数，每种礼物持续时间，继承BaseGiftBean实现自定义的SendGiftBean并实现相应接口方法，可轻松实现自定义的效果；最大礼物数可在xml上指定或者代码直接定义，其它参数可以在自定义SendGiftBean中指定，具体接入请参考demo

## Fast review
Activity
 ```java
    // 可以由服务器返回json解析得到
    bean1 = new SendGiftBean(1,1,"林喵喵","糖果",R.mipmap.tg,2700);
    bean2 = new SendGiftBean(2,2,"马甲","666",R.mipmap.good,3000);
    bean3 = new SendGiftBean(3,3,"小梦梦","小香蕉",R.mipmap.banana,2500);
    bean4 = new SendGiftBean(4,4,"大枫哥","鱼丸",R.mipmap.yw,2000);
    bean5 = new SendGiftBean(4,1,"大枫哥","糖果",R.mipmap.tg,2700);
    rewardLayout.setGiftAdapter(new RewardLayout.GiftAdapter<SendGiftBean>() {
            @Override
            public View onInit(View view, SendGiftBean bean) {
                //参考demo
                return view
            }

            @Override
            public View onUpdate(View view, SendGiftBean bean) {
                //参考demo
                return view;
            }

            @Override
            public void addAnim(final View view) {
                //参考demo
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
            }
        });
        
    @Override
    protected void onPause() {
        super.onPause();
        if(rewardLayout != null) {
            rewardLayout.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(rewardLayout != null) {
            rewardLayout.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(rewardLayout != null) {
            rewardLayout.onDestroy();
        }
    }
    
   Just call ->
   rewardLayout.put(bean1);
```
XML
 ```java
<com.zhangyf.reward.view.RewardLayout
        android:id="@+id/llgiftcontent"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="100dp"
        android:animateLayoutChanges="true"
        app:max_gift="3"
        app:gift_item_layout="@layout/gift_animation_item"
        android:orientation="vertical" />
```
Config
 ```java
  @Deprecated
  GiftConfig.getInstance()
                .setGiftCount(4)
                .setGiftIds(new int[] {1,2,3,4})
                .setGiftNames(new String[] {"糖果","666","小香蕉","大鱼丸"})
                .setGiftRes(new int[] {R.mipmap.tg,R.mipmap.good,R.mipmap.banana,R.mipmap.yw})
                .setStayTimes(new long[] {2000,2500,2700,5200});
  以上配置已去除，全部参数有数据对象返回,数据对象必须继承BaseGiftBean，并实现相应接口，具体参考demo
 ```
## Todo
**v1.4** 支持更多礼物替换规则<br>
 
## Fixed 
**v1.0** 已改进不同礼物消失机制，采用postHandler及removeCallbacks去更新和执行删除时机，可以通过config自定义每种礼物不同的持续时间，同时已优化不同人对同种礼物的区分<br><br>
**v1.1** 修复快速送礼物重复问题,调整postDelay为ScheduledExecutorService去定时清除到期礼物，调整数据结构，用户自定义数据对象需继承BaseGiftBean并实现相应接口，取消GiftConfig配置<br><br>
**v1.2** 增加礼物LinkedBlockingQueue队列，支持高并发礼物赠送，程序模拟礼物赠送确保礼物都能够被展现，修复部分bug<br><br>
**v1.3** 生成lib库,gradle直接集成<br>


## Thanks
感谢许同学提供的切图
