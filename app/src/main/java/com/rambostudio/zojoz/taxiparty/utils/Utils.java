package com.rambostudio.zojoz.taxiparty.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.rambostudio.zojoz.taxiparty.R;

import java.util.Collection;
import java.util.List;

/**
 * Created by nuuneoi on 10/16/2014.
 */
public class Utils {

    private static Utils instance;

    public static Utils getInstance() {
        if (instance == null)
            instance = new Utils();
        return instance;
    }

    private Context mContext;

    private Utils() {
        mContext = Contextor.getInstance().getContext();
    }


    public String getDeviceId() {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getVersionName() {
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pInfo.versionName;
        } catch (Exception e) {
            return "1.0";
        }
    }
    public static ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(Contextor.getInstance().getContext());
            mProgressDialog.setMessage(Contextor.getInstance().getContext().getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) Contextor.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean isAvailableData(Collection<?> list, int holderAdapterPosition) {
        return null != list && !list.isEmpty() && holderAdapterPosition >= 0 && list.size() > holderAdapterPosition;
    }

    public static int getSize(List<?> list){
        int size = 0;
        if(null != list && !list.isEmpty()){
            size = list.size();
        }
        return size;
    }
}
