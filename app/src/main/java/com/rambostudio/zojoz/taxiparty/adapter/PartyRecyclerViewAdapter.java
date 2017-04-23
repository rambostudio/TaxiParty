package com.rambostudio.zojoz.taxiparty.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.listener.PartyRecyclerViewAdapterListener;
import com.rambostudio.zojoz.taxiparty.model.Party;
import com.rambostudio.zojoz.taxiparty.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rambo on 15/4/2560.
 */

public class PartyRecyclerViewAdapter extends FirebaseRecyclerAdapter<Party,PartyRecyclerViewAdapter.PartyViewHolder>  {
    Context mContext;

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     *                        You will be responsible for populating an instance of the corresponding
     *                        view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public PartyRecyclerViewAdapter(Class<Party> modelClass, int modelLayout, Class<PartyViewHolder> viewHolderClass, Query ref,Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(PartyViewHolder viewHolder, final Party party, int position) {
        if (party.getId() != null) {
            viewHolder.bindData(party);
            viewHolder.cvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mPartyRecyclerViewAdapterListener.onPartyItemClick(party.getId());
                }
            });
        }
    }

//
//    @Override
//    public PartyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        return new PartyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_party_item, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(PartyViewHolder holder, int position) {
//        if (Utils.isAvailableData(mList, position)) {
//            holder.bindData(mList.get(position));
//        }
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return Utils.getSize(mList);
//    }

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
            if (party != null) {
                tvDestination.setText(party.getDestination());
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
//                case R.id.cvContent:
//                    mPartyRecyclerViewAdapterListener.onPartyItemClick(mList.get(getAdapterPosition()).getId());
////                    Toast.makeText(mContext, mList.get(getAdapterPosition()).getDestination(), Toast.LENGTH_SHORT).show();
//                    break;
            }
        }
    }
}
