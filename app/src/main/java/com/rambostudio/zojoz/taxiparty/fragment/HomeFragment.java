package com.rambostudio.zojoz.taxiparty.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.activity.AddPartyActivity;
import com.rambostudio.zojoz.taxiparty.activity.MainActivity;
import com.rambostudio.zojoz.taxiparty.adapter.PartyAdapter;
import com.rambostudio.zojoz.taxiparty.adapter.PartyRecyclerViewAdapter;
import com.rambostudio.zojoz.taxiparty.listener.PartyRecyclerViewAdapterListener;
import com.rambostudio.zojoz.taxiparty.manager.FirebaseManager;
import com.rambostudio.zojoz.taxiparty.manager.HttpManager;
import com.rambostudio.zojoz.taxiparty.manager.PartyListManager;
import com.rambostudio.zojoz.taxiparty.manager.UserManager;
import com.rambostudio.zojoz.taxiparty.model.Party;
import com.rambostudio.zojoz.taxiparty.model.User;
import com.rambostudio.zojoz.taxiparty.model.UserInParty;
import com.rambostudio.zojoz.taxiparty.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class HomeFragment extends Fragment implements PartyRecyclerViewAdapterListener {


    private static final String TAG = "HomeFragment";

    public interface HomeFragmentListener {
        void onPartyItemClicked(String partyId);
    }

    List<Party> mList;
    RecyclerView recyclerView;
    PartyRecyclerViewAdapter recyclerViewAdapter;
    PartyAdapter partyAdapter;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    AppCompatButton btnAddParty;
    ChildEventListener childEventListener;
    ValueEventListener valueEventListener;
    Query userInPartyRef;
    private DatabaseReference mFirebaseDatabaseReference;

    public HomeFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initInstances(rootView, savedInstanceState);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private void mockupData() {
//        mList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            List<UserInParty> users = new ArrayList<>();
//            FirebaseUser userObj = UserManager.getInstance().getUser();
//            UserInParty user = new UserInParty(userObj.getUid(), userObj.getDisplayName(), new Date(), 1);
//            users.add(user);
//            mList.add(new Party(UUID.randomUUID().toString(), "Bangkok" + i, new Date(), 1, users));
//        }
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        setHasOptionsMenu(true);
        getActivity().setTitle("Party");

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
//
//        recyclerViewAdapter = new PartyRecyclerViewAdapter(Party.class,
//                R.layout.view_party_item,
//                PartyRecyclerViewAdapter.PartyViewHolder.class,
//                mFirebaseDatabaseReference.child("party"),
//                getActivity()
//        );
//        recyclerView.setAdapter(recyclerViewAdapter);

    }

    private void reloadData() {
        mList.clear();
        getDataFromServer();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getDataFromLocal() {
    }

    private void getDataFromServer() {
        //get data from firebase
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            mList = FirebaseManager.getInstance().getPartyByUserId(user.getUid());
//            Toast.makeText(Contextor.getInstance().getContext(), "mList.size()= "+mList.size(), Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(Contextor.getInstance().getContext(), "Not found userId", Toast.LENGTH_SHORT).show();
//        }
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        getPartyByDistance();
    }

    private void getPartyByDistance() {
        DatabaseReference partyRef = mFirebaseDatabaseReference.child("party");
        partyRef.orderByChild("createAt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("firebase", "onDataChange dataSnapshot: " + dataSnapshot);
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    mList.add(data.getValue(Party.class));
                }
                partyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPartyByUserId() {
//        userInPartyRef = mFirebaseDatabaseReference.child("userInParty").orderByChild("userId").equalTo(getUid());
//        valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                Log.i("valueEventListener", "onDataChange: "+dataSnapshot);
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Log.i("valueEventListener", "onDataChange: " + data);
//                    UserInParty uip = data.getValue(UserInParty.class);
//                    mFirebaseDatabaseReference
//                            .child("party").child(uip.getPartyId())
//                            .orderByChild("createAt")
//                            .limitToLast(20)
//                            .addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    Log.i("firebase", "onDataChange dataSnapshot: " + dataSnapshot);
//                                    mList.add(dataSnapshot.getValue(Party.class));
//                                    partyAdapter.notifyDataSetChanged();
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        userInPartyRef.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mList = new ArrayList<>();

        if (Utils.isOnline()) {
            getDataFromServer();
        } else {
            getDataFromLocal();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        Log.i(TAG, "onStart: mList :" + mList);
        partyAdapter = new PartyAdapter(mList, getContext(), this);
        recyclerView.setAdapter(partyAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("fragmentState", "onStop: ");
        if (valueEventListener != null) {
//            userInPartyRef.removeEventListener(valueEventListener);

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
    public void onPartyItemClick(String partyId) {
        Toast.makeText(Contextor.getInstance().getContext(), "partyId :" + partyId, Toast.LENGTH_SHORT).show();

        checkUserIdIsInParty(partyId, getUid());
        Toast.makeText(Contextor.getInstance().getContext(), "Have't User", Toast.LENGTH_SHORT).show();

        HomeFragmentListener listener = (HomeFragmentListener) getActivity();
        listener.onPartyItemClicked(partyId);
//        Toast.makeText(Contextor.getInstance().getContext(), "partyId :" +partyId, Toast.LENGTH_SHORT).show();
    }

    private void addUserToParty(String partyId, String userId) {
        Log.i(TAG, "addUserToParty");
        UserInParty uip = new UserInParty();
        uip.setCreateAt(new Date());
        uip.setId(UUID.randomUUID().toString());
        uip.setRole(2);
        uip.setPartyId(partyId);
        uip.setUserId(userId);
        mFirebaseDatabaseReference.child("userInParty").push().setValue(uip);
    }


    private void checkUserIdIsInParty(final String partyId, final String userId) {
        final boolean[] result = {true};
        final String[] mUserId = new String[1];
        Query query = mFirebaseDatabaseReference.child("userInParty").orderByChild("partyId").equalTo(partyId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    mUserId[0] = data.child("userId").getValue().toString();
                    Log.i(TAG, "checkUserIdIsIn-PartyonDataChange: " + data.child("userId").getValue());
                    if (userId.equals(mUserId[0])) {
                        result[0] = false;
                    }
                }
                if (result[0]) {
                    addUserToParty(partyId, userId);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("fragmentState", "onResume: ");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("fragmentState", "onPause: ");

    }
}
