[![](https://jitpack.io/v/Yuphee/RewardLayout.svg)](https://jitpack.io/#Yuphee/RewardLayout)	
[![GitHub license](https://img.shields.io/github/license/Yuphee/RewardLayout.svg?color=brightgreen)](https://github.com/Yuphee/RewardLayout/blob/master/LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/Yuphee/RewardLayout.svg?color=%23ff69b4)](https://github.com/Yuphee/RewardLayout/stargazers)
[![GitHub stars](https://img.shields.io/badge/API-14%2B-orange.svg)](https://developer.android.com/about/versions/android-4.0.html)
![GitHub last commit](https://img.shields.io/github/last-commit/Yuphee/RewardLayout.svg)
<img src="https://img.shields.io/badge/email-437220638%40qq.com-blue.svg">
<img src="https://img.shields.io/badge/Tip-thanks%20for%20reward-red.svg">


# RewardLayout
仿斗鱼送礼物效果<br>
本项目旨在提供实现参考，交流学习。<br>
> 关于我，欢迎关注  
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
 > dependencies {
 >       implementation 'com.github.Yuphee:RewardLayout:1.0.6.3'
 > }
 ```

## Apk
[Demo体验](https://www.pgyer.com/i6qA)

## Statement
**1.关于直播间布局**<br>
可以采用Activity+LiveFragment+ContainnerDialogFragment(Viewpager(EmptyFragment+LayerFragment));不一定要用DialogFragment,如果想上下滑流畅还是得用普通的Fragment<br>
如果需要上下滑动切换直播间，可以套上verticalviewpager，可以参考我的另一个关于兼容上下左右滑动的Viewpager项目[Bidirectio](https://github.com/Yuphee/Bidirectio)也可以是其它的[VerticalViewpager](https://github.com/castorflex/VerticalViewPager)，直播页单独在LiveFragment实现,HorizontalViewpager覆盖在LiveFragment之上，HorizontalViewpager中包含2个Fragment，一个是空的透明页，用于左右滑动实现沉浸式效果,另一个则是遮罩层，包含用户列表，头像，评论，礼物布局等一些悬浮在直播TextureView之上的布局<br><br>
**2.关于RewardLayout**<br>
可自定义礼物item布局，动画，最大条数，每种礼物持续时间，继承BaseGiftBean实现自定义的SendGiftBean，并实现相应接口方法，可轻松实现自定义的效果；最大礼物数可在xml上指定或者代码直接定义，其它参数可以在自定义SendGiftBean中指定，具体接入请参考demo<br><br>
**3.实践**<br>
本库用在项目《椰趣》中，可以自行到各大应用市场下载体验

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
            public View onUpdate(View view, SendGiftBean o，SendGiftBean t) {
                //参考demo
                o返回的数据对象为Rewardlayout内部存储过的该礼物的数据对象，t返回的对象为每次put进去的新对象
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

            //判断礼物唯一性
            @Override
            public boolean checkUnique(SendGiftBean o, SendGiftBean t) {
                return o.getTheGiftId() == t.getTheGiftId() && o.getTheUserId() == t.getTheUserId();
            }
            
            //礼物展示结束，可能由于送礼者过多，轨道被替换导致结束
             @Override
            public void onKickEnd(SendGiftBean bean) {
                Log.e("zyf", "onKickEnd:" + bean.getTheGiftId() + "," + bean.getGiftName() + "," + bean.getUserName() + "," +   bean.getTheGiftCount());
            }

            //礼物连击结束,即被系统自动清理时回调
            @Override
            public void onComboEnd(SendGiftBean bean) {
                Log.e("zyf","onComboEnd:"+bean.getTheGiftId()+","+bean.getGiftName()+","+bean.getUserName()+","+bean.getTheGiftCount());
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
   //手动刷新礼物过期时间，防止被礼物回收线程回收，可用于其它需要增加礼物停留时间的需求。
   rewardLayout.updateRefreshTime(bean1,2000);
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
## Todo
支持更多礼物替换规则（如贵重礼物展示优先，总价值礼物展示优先，最近最新优先，组合策略等）<br>
 
## Fixed 
**v1.0** 已改进不同礼物消失机制，采用postHandler及removeCallbacks去更新和执行删除时机，可以通过config自定义每种礼物不同的持续时间，同时已优化不同人对同种礼物的区分<br><br>
**v1.1** 修复快速送礼物重复问题,调整postDelay为ScheduledExecutorService去定时清除到期礼物，调整数据结构，用户自定义数据对象需继承BaseGiftBean并实现相应接口，取消GiftConfig配置<br><br>
**v1.2** 增加礼物LinkedBlockingQueue队列，支持高并发礼物赠送，程序模拟礼物赠送确保礼物都能够被展现，修复部分bug<br><br>
**v1.3** 生成lib库,gradle直接集成<br><br>
**v1.4** 必须继承BaseGiftBean改为实现GiftIdentify接口,修复内存泄漏,礼物唯一条件判断由用户实现<br><br>
**v1.5** 解决相同礼物单次赠送数目变化问题，修复其它问题<br><br>
**v1.5.1** 降低礼物库的最低sdk版本至19<br><br>
**v1.5.2** 修复内存泄漏<br><br>
**v1.5.4** 增加送礼轨道被踢回调及系统判定结束连击回调<br><br>
**v1.5.8** 增加手动刷新礼物过期时间<br><br>
**v1.6.0** minsdk降为14,修改某些代码<br><br>
**v1.6.1** 修改礼物更新回调，返回新添加进去的礼物对象,tag更新内部完成，如需要更新原已展示对象的数据需自行手动更新<br><br>
**v1.6.2** 修复礼物轨道概率不消失问题 见 [issue#6](https://github.com/Yuphee/RewardLayout/issues/6)<br><br>
**v1.6.3** 修复轨道礼物消失时赠送同一种礼物，礼物轨道不显示问题<br>


## Thanks
感谢许同学提供的切图

## 赞赏
如果对大佬有用的话，赏杯下午茶吧。<br>
![image](/screenshots/alipay.png)![image](/screenshots/wechat_reward.png)
