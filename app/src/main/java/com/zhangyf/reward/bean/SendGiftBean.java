package com.zhangyf.reward.bean;

/**
 * Created by zhangyf on 2017/3/30.
 */

public class SendGiftBean {

    private int userId;
    private int giftId;
    private String userName;

    public SendGiftBean(int userId, int giftId, String userName) {
        this.userId = userId;
        this.giftId = giftId;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
