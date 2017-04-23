package com.rambostudio.zojoz.taxiparty.fragment;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.adapter.MessageRecyclerViewAdapter;
import com.rambostudio.zojoz.taxiparty.adapter.PartyRecyclerViewAdapter;
import com.rambostudio.zojoz.taxiparty.adapter.UserInPartyRecyclerViewAdapter;
import com.rambostudio.zojoz.taxiparty.listener.PartyRecyclerViewAdapterListener;
import com.rambostudio.zojoz.taxiparty.manager.PartyListManager;
import com.rambostudio.zojoz.taxiparty.manager.UserManager;
import com.rambostudio.zojoz.taxiparty.model.Message;
import com.rambostudio.zojoz.taxiparty.model.MessageData;
import com.rambostudio.zojoz.taxiparty.model.Party;
import com.rambostudio.zojoz.taxiparty.model.PartyData;
import com.rambostudio.zojoz.taxiparty.model.User;
import com.rambostudio.zojoz.taxiparty.model.UserInParty;
import com.rambostudio.zojoz.taxiparty.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class PartyFragment extends Fragment implements View.OnClickListener {
    final String TAG = "PartyFragment";
    Party party;
    List<User> mUserList;
    List<MessageData> mMessageList;
    RecyclerView rvMessage;
    RecyclerView rvUerInParty;

    PartyRecyclerViewAdapter recyclerViewAdapter;
    UserInPartyRecyclerViewAdapter userInPartyRecyclerViewAdapter;
    MessageRecyclerViewAdapter messageRecyclerViewAdapter;
    ProgressBar progressBar;
    AppCompatButton btnAddParty;
    DatabaseReference mFirebaseDatabaseReference;
    String mPartyId;
    ChildEventListener childEventListener;
    ValueEventListener valueEventListener;


    DatabaseReference partyRef;
    private Toolbar toolbar;


    AppCompatImageButton btnSendMessage;
    AppCompatEditText edtMessage;
    private ValueEventListener userInPartyValueEventListener;
    private Query userInPartyRefQuery;

    public PartyFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static PartyFragment newInstance(String partyId) {
        PartyFragment fragment = new PartyFragment();
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
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
//        setHasOptionsMenu(true);
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setOnClickListener(this);
        rvUerInParty = (RecyclerView) rootView.findViewById(R.id.rvUserInParty);
        rvUerInParty.setOnClickListener(this);
        rvUerInParty.setVisibility(View.INVISIBLE);
        rvUerInParty.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rvMessage = (RecyclerView) rootView.findViewById(R.id.rvMessage);
        rvMessage.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        btnSendMessage = (AppCompatImageButton) rootView.findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(this);

        edtMessage = (AppCompatEditText) rootView.findViewById(R.id.edtMessage);
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
//                partyData.setId(dataSnapshot.getKey());
//                partyData.setDestination(party.getDestination());
//                partyData.setCreateAt(party.getCreatedAt());
                setTitleToolbar(party.getDestination());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        partyRef.addValueEventListener(valueEventListener);


        mUserList = new ArrayList<>();
        // TODO: 20/4/2560 get userInParty
        userInPartyRefQuery = mFirebaseDatabaseReference.child("userInParty").orderByChild("partyId").equalTo(mPartyId);
        userInPartyValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("FirebaseDatabase", "mUserList.size() : " + mUserList.size());
                mUserList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String userId = data.child("userId").getValue().toString();
                    Log.i("FirebaseDatabase", "userInPartyValueEventListener onDataChange user: " + userId);

                    mFirebaseDatabaseReference.child("user").child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("FirebaseDatabase", "addValueEventListener onDataChange user: " + dataSnapshot);
                            User user = dataSnapshot.getValue(User.class);
                            addUserToList(user);

                            Log.i("FirebaseDatabase", "addValueEventListener onDataChange mUserList.size(): " + mUserList.size());
                            userInPartyRecyclerViewAdapter.notifyDataSetChanged();
                        }

                        private void addUserToList(User user) {
                            boolean res = true;
                            for (User mUser : mUserList) {
                                if (mUser.getId().equals(user.getId())) {
                                    res = false;
                                }
                            }
                            if (res) {
                                mUserList.add(user);
                            }
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
        };
        userInPartyRefQuery.addValueEventListener(userInPartyValueEventListener);

        mMessageList = new ArrayList<>();
        // TODO: 20/4/2560 get MessageInParty
        mFirebaseDatabaseReference.child("message").child(mPartyId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("Firebase", "message onChildAdded: " + dataSnapshot);
                Message mMessage = dataSnapshot.getValue(Message.class);
                final MessageData messageData = new MessageData();
                messageData.setText(mMessage.getText());
                messageData.setId(mMessage.getId());
                messageData.setUserId(mMessage.getUserId());
                messageData.setType(mMessage.getType());
                mFirebaseDatabaseReference.child("user").child(mMessage.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        messageData.setSenderName(dataSnapshot.child("name").getValue().toString());
                        messageData.setImageAccountUrl(dataSnapshot.child("imageUrl").getValue().toString());
                        mMessageList.add(messageData);
                        rvMessage.scrollToPosition(mMessageList.size()- 1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                messageRecyclerViewAdapter.notifyDataSetChanged();
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
        toolbar.setTitle(text);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(text);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Utils.isOnline()) {
            getDataFromServer();
        } else {
            getDataFromLocal();
        }
//        Toast.makeText(getActivity(), "mUserList"+mUserList.size(), Toast.LENGTH_SHORT).show();
        userInPartyRecyclerViewAdapter = new UserInPartyRecyclerViewAdapter(mUserList, getActivity());
        rvUerInParty.setAdapter(userInPartyRecyclerViewAdapter);

        messageRecyclerViewAdapter = new MessageRecyclerViewAdapter(mMessageList, getActivity(), getUserId());
        rvMessage.setAdapter(messageRecyclerViewAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (valueEventListener != null) {
            partyRef.removeEventListener(valueEventListener);
        }
        if (userInPartyValueEventListener != null) {
            userInPartyRefQuery.removeEventListener(userInPartyValueEventListener);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                toggleUserInParty();
                break;
            case R.id.btnSendMessage:
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        if (validateForm()) {
            Message message = new Message();
            message.setText(edtMessage.getText().toString());
            message.setType(1);
            message.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

            mFirebaseDatabaseReference.child("message").child(mPartyId).push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getActivity(), "addOnCompleteListener : Add Complete", Toast.LENGTH_SHORT).show();
                    edtMessage.setText("");
                }
            });
        }
    }

    private boolean validateForm() {
        return !TextUtils.isEmpty(edtMessage.getText().toString());
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        if (item.getItemId() == R.id.action_add_party) {
////            Toast.makeText(getActivity(), "Add Party", Toast.LENGTH_SHORT).show();
////
////        }
//        return super.onOptionsItemSelected(item);
//    }

    private void toggleUserInParty() {
        if (rvUerInParty.getVisibility() == View.VISIBLE) {
            rvUerInParty.setVisibility(View.INVISIBLE);
        } else {
            rvUerInParty.setVisibility(View.VISIBLE);
        }
    }

    private String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
