package com.rambostudio.zojoz.taxiparty.model;

import java.util.Date;

/**
 * Created by rambo on 16/4/2560.
 */

public class User {
    String id;
    String email;
    String name;
    Date createAt;
    Date updateAt;
    Date signInAt;
    String imageUrl;
   public User() {
    }

    public User(String id, String name, String email,  Date createAt, Date updateAt,Date signInAt,String imageUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.signInAt = signInAt;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getSignInAt() {
        return signInAt;
    }

    public void setSignInAt(Date signInAt) {
        this.signInAt = signInAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
