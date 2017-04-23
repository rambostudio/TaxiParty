package com.rambostudio.zojoz.taxiparty.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.rambostudio.zojoz.taxiparty.adapter.PartyRecyclerViewAdapter;
import com.rambostudio.zojoz.taxiparty.model.Party;
import com.rambostudio.zojoz.taxiparty.model.PartyData;
import com.rambostudio.zojoz.taxiparty.model.User;
import com.rambostudio.zojoz.taxiparty.model.UserInParty;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class FirebaseManager {


    private final DatabaseReference usersRef;
    private final DatabaseReference partyRef;
    private final DatabaseReference userInPartyRef;
    FirebaseDatabase firebaseDatabase;
    private static FirebaseManager instance;
    DatabaseReference rootDatabaseReference;

    public static FirebaseManager getInstance() {
        if (instance == null)
            instance = new FirebaseManager();
        return instance;
    }

    private Context mContext;

    private FirebaseManager() {
        mContext = Contextor.getInstance().getContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        rootDatabaseReference = firebaseDatabase.getReference();
        usersRef = rootDatabaseReference.child("user");
        partyRef = rootDatabaseReference.child("party");
        userInPartyRef = rootDatabaseReference.child("userInParty");


    }

    public DatabaseReference getDatabaseReference() {
        return firebaseDatabase.getReference();
    }

    private User getUserById(String id) {
        final User[] mUser = new User[1];
        usersRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mUser[0] = dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, "databaseError ", Toast.LENGTH_SHORT).show();
            }
        });
        return mUser[0];
    }

    public boolean createOrUpdateUser(User user) {
        try {
            User mUser = getUserById(user.getId());
            if (mUser == null) {
                usersRef.child(user.getId()).setValue(user);
            } else {
                mUser.setSignInAt(new Date());
                usersRef.child(user.getId()).child("signInAt").setValue(mUser.getSignInAt());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public List<Party> getPartyByUserId(final String id) {
        //get List party from user in party
        //map party data from party
        //map all data to PattyViewModel
        final List<Party> mPartyList = new ArrayList<>();

        Log.i("Firebase", "Uid " + UserManager.getInstance().getUser().getUid());
        Query query = userInPartyRef.child(UserManager.getInstance().getUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(mContext, "dataSnapshot" + dataSnapshot, Toast.LENGTH_SHORT).show();
                for (DataSnapshot userInPartyDataSnapshot : dataSnapshot.getChildren()) {
                    Log.i("Firebase", "userInPartyDataSnapshot :" + userInPartyDataSnapshot);
                    UserInParty userInParty = userInPartyDataSnapshot.getValue(UserInParty.class);
                    //get data by partyid
                    if (userInParty != null) {
                        Log.i("Firebase", "userInParty != null");
                        partyRef.child(userInParty.getPartyId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                Log.i("Firebase", "dataSnapshot1 :" + dataSnapshot1);
                                Party party = dataSnapshot1.getValue(Party.class);
                                mPartyList.add(party);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(mContext, "partyReference databaseError ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, "userInPartyRef databaseError ", Toast.LENGTH_SHORT).show();
            }
        });
        return mPartyList;
    }

    public boolean createOrUpdateParty(final Party party, List<UserInParty> userInParties) {
        if (party.getId() == null) {
            // create
            String key = partyRef.push().getKey();
            Log.d("Firebase", "mPartyRef.getKey():" +key );
            party.setId(key);
            party.setCreatedAt(new Date());
            partyRef.child(key).setValue(party);

            if (userInParties == null || userInParties.size() == 0) {
                userInParties = new ArrayList<>();
                UserInParty userInParty = new UserInParty(UUID.randomUUID().toString(),null,getUid(),new Date(),1);
                userInParties.add(userInParty);
                UserInParty userInParty2 = new UserInParty(UUID.randomUUID().toString(),null,UUID.randomUUID().toString(),new Date(),1);
                userInParties.add(userInParty2);
            }
            for (UserInParty uip : userInParties) {
                uip.setPartyId(key);
                userInPartyRef.push().setValue(uip);
            }
        } else {
            // Update

        }
        return true;
    }

    public List<PartyData> getPartyDataById(String id) {



        return null;
    }

    private String  getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
