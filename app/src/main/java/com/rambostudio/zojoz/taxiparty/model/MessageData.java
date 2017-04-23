package com.rambostudio.zojoz.taxiparty.model;

/**
 * Created by rambo on 20/4/2560.
 */

public class MessageData {
    String id;
    String text;
    int type;
    String userId;
    String imageAccountUrl;
    String SenderName;

    public MessageData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageAccountUrl() {
        return imageAccountUrl;
    }

    public void setImageAccountUrl(String imageAccountUrl) {
        this.imageAccountUrl = imageAccountUrl;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }
}
