package com.zhangyf.gift.bean;

/**
 * 礼物标识
 *
 * @author zhangyf
 * @date 2018/7/19 0019.
 */
public interface GiftIdentify extends Comparable<GiftIdentify>{

    int getTheGiftId();

    void setTheGiftId(int gid);

    int getTheUserId();

    void setTheUserId(int uid);

    int getTheGiftCount();

    void setTheGiftCount(int count);

    long getTheGiftStay();

    void setTheGiftStay(long stay);

    long getTheLatestRefreshTime();

    void setTheLatestRefreshTime(long time);

    int getTheCurrentIndex();

    void setTheCurrentIndex(int index);
}
