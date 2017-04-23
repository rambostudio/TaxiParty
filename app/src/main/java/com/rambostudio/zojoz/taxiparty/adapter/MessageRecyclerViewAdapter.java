package com.rambostudio.zojoz.taxiparty.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.model.Message;
import com.rambostudio.zojoz.taxiparty.model.MessageData;
import com.rambostudio.zojoz.taxiparty.utils.Utils;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by rambo on 21/4/2560.
 */

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final String TAG = "MessageRecycler";
    List<MessageData> mList;
    Context mContext;
    String mUserId;
    final int MY_MESSAGE = 1;
    final int OTHER_MESSAGE = 2;

    public MessageRecyclerViewAdapter(List<MessageData> mList, Context mContext, String userId) {
        this.mList = mList;
        this.mContext = mContext;
        this.mUserId = userId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MY_MESSAGE:
                return new MyMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_my_message_item, parent, false));
            case OTHER_MESSAGE:
                return new OtherMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_other_message_item, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case MY_MESSAGE:
                if (Utils.isAvailableData(mList, position)) {
                    MyMessageViewHolder myHolder = (MyMessageViewHolder) holder;
                    myHolder.bindData(mList.get(position));
                }
                break;
            case OTHER_MESSAGE:
                if (Utils.isAvailableData(mList, position)) {
                    OtherMessageViewHolder otherHolder = (OtherMessageViewHolder) holder;
                    otherHolder.bindData(mList.get(position));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return Utils.getSize(mList);
    }

    @Override
    public int getItemViewType(int position) {
        if (mUserId.equals(mList.get(position).getUserId())) {
            return MY_MESSAGE;
        } else {
            return OTHER_MESSAGE;
        }
    }


    public class OtherMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        AppCompatImageView ivAccountImage;
        AppCompatTextView tvName;
        public OtherMessageViewHolder(View itemView) {
            super(itemView);
            initInstances();
        }

        private void initInstances() {
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            ivAccountImage = (AppCompatImageView) itemView.findViewById(R.id.ivImageOtherAccount);
            tvName = (AppCompatTextView) itemView.findViewById(R.id.tvName);
        }

        public void bindData(MessageData message) {
            Log.i(TAG, "bindData: " + message.toString());
            tvContent.setText(message.getText());
            if (message.getImageAccountUrl() != null) {
                loadAccountImage(message.getImageAccountUrl());
                tvName.setText(message.getSenderName());
            }
        }

        private void loadAccountImage(String url) {
            Glide.with(mContext)
                    .load(url)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(ivAccountImage);
        }
    }

    public class MyMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;

        public MyMessageViewHolder(View itemView) {
            super(itemView);
            initInstances();
        }

        private void initInstances() {
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);

        }

        public void bindData(MessageData message) {
            tvContent.setText(message.getText());
        }
    }
}
