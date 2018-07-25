# RewardLayout
仿斗鱼送礼物效果
> 关于我，欢迎关注  
  邮箱：437220638@qq.com
  如果对你有点帮助的话，点个star哦~
 
## Screenshots
手动版&nbsp;&&nbsp;模拟自动版<br>
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
        implementation 'com.github.Yuphee:RewardLayout:1.0.0'
}
 ```

## Demo
[Demo体验](/sample.apk)

## Statement
本项目旨在提供实现参考，交流学习。<br>
可自定义礼物item布局，动画，最大条数，每种礼物持续时间，继承BaseGiftBean实现自定义的SendGiftBean并实现相应接口方法，可轻松实现自定义的效果；<br>
最大礼物数可在xml上指定或者代码直接定义，其它参数可以在自定义SendGiftBean中指定，具体接入请参考demo

## 快速预览
Activity
 ```java
    // 可以由服务器返回json解析得到
    bean1 = new SendGiftBean(1,1,"林喵喵","糖果",R.mipmap.tg,2700);
    bean2 = new SendGiftBean(2,2,"马甲","666",R.mipmap.good,3000);
    bean3 = new SendGiftBean(3,3,"小梦梦","小香蕉",R.mipmap.banana,2500);
    bean4 = new SendGiftBean(4,4,"大枫哥","鱼丸",R.mipmap.yw,2000);
    bean5 = new SendGiftBean(4,1,"大枫哥","糖果",R.mipmap.tg,2700);
    rewardLayout.setGiftItemRes(R.layout.gift_animation_item);//设置礼物item布局
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
**1.4** 支持更多礼物替换规则<br>
如果又发现任何Bug或者改进的意见欢迎提issue或者邮件#，#

 
## Fixed 
**v1.0** 已改进不同礼物消失机制，采用postHandler及removeCallbacks去更新和执行删除时机，可以通过config自定义每种礼物不同的持续时间，同时已优化不同人对同种礼物的区分<br>
**v1.1** 修复快速送礼物重复问题,调整postDelay为ScheduledExecutorService去定时清除到期礼物，调整数据结构，用户自定义数据对象需继承BaseGiftBean并实现相应接口，取消GiftConfig配置<br>
**v1.2** 增加礼物LinkedBlockingQueue队列，支持高并发礼物赠送，程序模拟礼物赠送确保礼物都能够被展现，修复部分bug
**v1.3** 生成lib库,gradle直接集成<br>


## Thanks
感谢许同学提供的切图
