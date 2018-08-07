package com.zhangyf.reward.bean;

/**
 * Created by zhangyf on 2017/3/30.
 */

public class SendGiftBean extends BaseGiftBean{

    /**
     * 用户id
     */
    private int userId;
    /**
     * 礼物id
     */
    private int giftId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 礼物名称
     */
    private String giftName;
    /**
     * 礼物本地图片也可以定义为远程url
     */
    private int giftImg;
    /**
     * 礼物持续时间
     */
    private long giftStayTime;

    /**
     * 礼物计数
     */
    private int giftCount;
    /**
     * 礼物刷新时间
     */
    private long latestRefreshTime;
    /**
     * 当前index
     */
    private int currentIndex;

    public SendGiftBean() {
    }

    public SendGiftBean(int userId, int giftId, String userName,String giftName,int giftImg,long time) {
        this.userId = userId;
        this.giftId = giftId;
        this.userName = userName;
        this.giftName = giftName;
        this.giftImg = giftImg;
        this.giftStayTime = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getGiftImg() {
        return giftImg;
    }

    public void setGiftImg(int giftImg) {
        this.giftImg = giftImg;
    }

    @Override
    public int getTheGiftId() {
        return giftId;
    }

    @Override
    public void setTheGiftId(int gid) {
        this.giftId = gid;
    }

    @Override
    public int getTheUserId() {
        return userId;
    }

    @Override
    public void setTheUserId(int uid) {
        this.userId = uid;
    }

    @Override
    public long getTheGiftStay() {
        return giftStayTime;
    }

    @Override
    public void setTheGiftStay(long stay) {
        giftStayTime = stay;
    }
}
