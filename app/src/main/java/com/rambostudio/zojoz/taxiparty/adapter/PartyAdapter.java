package com.rambostudio.zojoz.taxiparty.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.activity.MainActivity;
import com.rambostudio.zojoz.taxiparty.listener.PartyRecyclerViewAdapterListener;
import com.rambostudio.zojoz.taxiparty.model.Party;
import com.rambostudio.zojoz.taxiparty.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rambo on 19/4/2560.
 */

public class PartyAdapter extends RecyclerView.Adapter<PartyAdapter.PartyViewHolder> {
    String TAG = "PartyAdapter";
    List<Party> mPartyList;
    Context mContext;
    PartyRecyclerViewAdapterListener mPartyRecyclerViewAdapterListener;

    public PartyAdapter(List<Party> mPartyList, Context context ,PartyRecyclerViewAdapterListener partyRecyclerViewAdapterListener) {

        this.mPartyList = mPartyList;
        this.mContext = context;
        this.mPartyRecyclerViewAdapterListener = partyRecyclerViewAdapterListener;
    }

    @Override
    public PartyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new PartyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_party_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PartyViewHolder holder, int position) {

        int newPosition = getItemCount() - 1 - position;
        if (Utils.isAvailableData(mPartyList, newPosition)) {
            Log.i(TAG, "onBindViewHolder: mPartyList.get(position)"+mPartyList.get(position).getDestination());
            if (mPartyList.get(position).getDestination() != null) {
                holder.bindData(mPartyList.get(newPosition));
            }

        }
    }

    @Override
    public int getItemCount() {
        return mPartyList.size();
    }

    public class PartyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvDestination, tvCreatedTime;
        CardView cvContent;
        public PartyViewHolder(View itemView) {
            super(itemView);
            initInstances();
        }

        private void initInstances() {
            tvDestination = (TextView) itemView.findViewById(R.id.tvDestination);
            cvContent = (CardView) itemView.findViewById(R.id.cvContent);
            tvCreatedTime = (TextView) itemView.findViewById(R.id.tvCreatedTime);
            cvContent = (CardView) itemView.findViewById(R.id.cvContent);
            cvContent.setOnClickListener(this);
        }

        public void bindData(Party party) {
            Log.i(TAG, "bindData: "+party.getCreatedAt().toString());
            if (party != null) {
                tvDestination.setText(party.getDestination());
                Log.d("CHECK ERR", "getDestination: " + party.getDestination());

                String format = "";
                if (party.getCreatedAt().getDate() == new Date().getDate()) {
                    format = "hh:mm:ss";
                } else {
                    format = "dd/MM/yyyy";
                }
                SimpleDateFormat simpleDate =  new SimpleDateFormat(format);

                String strDt = simpleDate.format(party.getCreatedAt());
                tvCreatedTime.setText(strDt);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cvContent:
                    int newPosition = getItemCount() - 1 - getAdapterPosition();
                    mPartyRecyclerViewAdapterListener.onPartyItemClick(mPartyList.get(newPosition).getId());
                    break;
            }
        }
    }
}
