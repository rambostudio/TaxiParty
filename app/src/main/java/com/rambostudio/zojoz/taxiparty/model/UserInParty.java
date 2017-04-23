package com.rambostudio.zojoz.taxiparty.model;

import java.util.Date;

/**
 * Created by rambo on 16/4/2560.
 */

public class UserInParty {
    String id;
    String partyId;
    String userId;
    Date createAt;
    int role;

    public UserInParty(String id, String partyId, String userId, Date createAt, int role) {
        this.id = id;
        this.partyId = partyId;
        this.userId = userId;
        this.createAt = createAt;
        this.role = role;
    }

    public UserInParty() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
