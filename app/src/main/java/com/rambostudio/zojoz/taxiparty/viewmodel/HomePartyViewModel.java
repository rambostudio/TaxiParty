package com.rambostudio.zojoz.taxiparty.viewmodel;

/**
 * Created by rambo on 18/4/2560.
 */

public class HomePartyViewModel {
    String id;
    String destination;
    int type;
    String typeImageUrl;
    String createdAt;

    public HomePartyViewModel() {
    }

    public HomePartyViewModel(String id, String destination, int type, String typeImageUrl, String createdAt) {
        this.id = id;
        this.destination = destination;
        this.type = type;
        this.typeImageUrl = typeImageUrl;
        this.createdAt = createdAt;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeImageUrl() {
        return typeImageUrl;
    }

    public void setTypeImageUrl(String typeImageUrl) {
        this.typeImageUrl = typeImageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
