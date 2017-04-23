package com.rambostudio.zojoz.taxiparty.model;

import java.util.Date;
import java.util.List;

/**
 * Created by rambo on 20/4/2560.
 */

public class PartyData {

    String id;
    String destination;
    Date createAt;
    List<User> userList;
    List<MessageData> messageDataList;

    public PartyData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<MessageData> getMessageDataList() {
        return messageDataList;
    }

    public void setMessageDataList(List<MessageData> messageDataList) {
        this.messageDataList = messageDataList;
    }
}
