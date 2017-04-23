package com.rambostudio.zojoz.taxiparty.manager;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class UserManager {

    FirebaseUser user;
    private static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    private Context mContext;

    private UserManager() {
        mContext = Contextor.getInstance().getContext();
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }
}
