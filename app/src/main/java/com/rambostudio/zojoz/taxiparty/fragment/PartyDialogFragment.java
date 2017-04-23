package com.rambostudio.zojoz.taxiparty.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.adapter.PartyRecyclerViewAdapter;
import com.rambostudio.zojoz.taxiparty.model.Party;
import com.rambostudio.zojoz.taxiparty.model.PartyData;
import com.rambostudio.zojoz.taxiparty.model.User;
import com.rambostudio.zojoz.taxiparty.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class PartyDialogFragment extends DialogFragment {
    Party party;
    RecyclerView recyclerView;
    PartyRecyclerViewAdapter recyclerViewAdapter;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    AppCompatButton btnAddParty;
    DatabaseReference mFirebaseDatabaseReference;
    String mPartyId;
    ChildEventListener childEventListener;
   ValueEventListener valueEventListener;


    DatabaseReference partyRef;

    public PartyDialogFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static PartyDialogFragment newInstance(String partyId) {
        PartyDialogFragment fragment = new PartyDialogFragment();
        Bundle args = new Bundle();
        args.putString("partyId", partyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        mPartyId = getArguments().getString("partyId");

//        if (Utils.getSize(PartyListManager.getInstance().getPartyList()) == 0) {
//            mockupData();
//            PartyListManager.getInstance().setPartyList(mList);
//
//        }


        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_party, container, false);
        initInstances(rootView, savedInstanceState);

        return rootView;
    }


    private void mockupData() {
//        mList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            List<UserInParty> users = new ArrayList<>();
//            FirebaseUser userObj = UserManager.getInstance().getUser();
//            UserInParty user = new UserInParty(
//                    userObj.getUid(), userObj.getDisplayName(), new Date(),1
//            );
//            users.add(user);
//            mList.add(new Party(UUID.randomUUID().toString(),"Bangkok"+i, new Date(), 1,users));
//        }

    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
//        setHasOptionsMenu(true);
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvParty);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        recyclerViewAdapter = new PartyRecyclerViewAdapter(Party.class,
//                R.layout.view_party_item,
//                PartyRecyclerViewAdapter.PartyViewHolder.class,
//                mFirebaseDatabaseReference.child("party"),
//                getActivity(),
//                this
//        );
//        recyclerView.setAdapter(recyclerViewAdapter);

    }

    private void reloadData() {
//        mockupData();
        recyclerViewAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getDataFromLocal() {
    }

    private void getDataFromServer() {
        // TODO: 20/4/2560 getPartyDataById
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final PartyData partyData = new PartyData();

        partyRef = mFirebaseDatabaseReference.child("party").child(mPartyId);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                party = dataSnapshot.getValue(Party.class);
                partyData.setId(dataSnapshot.getKey());
                partyData.setDestination(party.getDestination());
                partyData.setCreateAt(party.getCreatedAt());
                setTitleToolbar(party.getDestination());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        partyRef.addValueEventListener(valueEventListener);


        final List<User> userList = new ArrayList<>();
        // TODO: 20/4/2560 get userInParty
        Query userInPartyRefQuery = mFirebaseDatabaseReference.child("userInParty").orderByChild("partyId").equalTo(mPartyId);
        userInPartyRefQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.i("FirebaseDatabase", "addListenerForSingleValueEvent onDataChange userId: " +  dataSnapshot.getValue());

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String userId = data.child("userId").getValue().toString();
                    mFirebaseDatabaseReference.child("user").child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("FirebaseDatabase", "addValueEventListener onDataChange user: " + dataSnapshot);
                            userList.add(dataSnapshot.getValue(User.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // TODO: 20/4/2560 get MessageInParty
        mFirebaseDatabaseReference.child("message").child(mPartyId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setTitleToolbar(String text) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(text);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Utils.isOnline()) {
            getDataFromServer();
        } else {
            getDataFromLocal();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (valueEventListener != null) {
            partyRef.removeEventListener(valueEventListener);
        }
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_party, menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        if (item.getItemId() == R.id.action_add_party) {
////            Toast.makeText(getActivity(), "Add Party", Toast.LENGTH_SHORT).show();
////
////        }
//        return super.onOptionsItemSelected(item);
//    }
}
