package com.rambostudio.zojoz.taxiparty.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.model.Party;
import com.rambostudio.zojoz.taxiparty.model.User;
import com.rambostudio.zojoz.taxiparty.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by rambo on 15/4/2560.
 */

public class UserInPartyRecyclerViewAdapter extends RecyclerView.Adapter<UserInPartyRecyclerViewAdapter.UserInPartyViewHolder> {

    List<User> mList;
    Context mContext;

    public UserInPartyRecyclerViewAdapter(List<User> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override

    public UserInPartyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserInPartyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_user_in_party_account_item, parent, false));
    }

    @Override
    public void onBindViewHolder(UserInPartyViewHolder holder, int position) {
        if (Utils.isAvailableData(mList, position)) {
            holder.bindData(mList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return Utils.getSize(mList);
    }

    public class UserInPartyViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivAccountImage;

        public UserInPartyViewHolder(View itemView) {
            super(itemView);
            initInstances();

        }

        private void initInstances() {
            ivAccountImage = (AppCompatImageView) itemView.findViewById(R.id.ivAccountImage);

        }

        public void bindData(User user) {
            if (user.getImageUrl() != null) {
                loadImageAccount(user.getImageUrl());

            }
        }

        private void loadImageAccount(String url) {
            Glide.with(mContext)
                    .load(url)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(ivAccountImage);
        }
    }
}
