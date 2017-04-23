package com.rambostudio.zojoz.taxiparty.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by rambo on 16/4/2560.
 */

public class Party implements Parcelable {
    String id;
    String destination;
    Date createdAt;
    int type;

    public Party() {
    }


    public Party(String id, String destination, Date createdAt, int type) {
        this.id = id;
        this.destination = destination;
        this.createdAt = createdAt;
        this.type = type;
    }


    protected Party(Parcel in) {
        id = in.readString();
        destination = in.readString();
        type = in.readInt();
    }

    public static final Creator<Party> CREATOR = new Creator<Party>() {
        @Override
        public Party createFromParcel(Parcel in) {
            return new Party(in);
        }

        @Override
        public Party[] newArray(int size) {
            return new Party[size];
        }
    };

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(destination);
        parcel.writeInt(type);
    }
}
