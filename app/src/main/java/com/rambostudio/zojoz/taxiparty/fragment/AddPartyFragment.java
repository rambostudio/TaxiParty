package com.rambostudio.zojoz.taxiparty.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;
import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.manager.FirebaseManager;
import com.rambostudio.zojoz.taxiparty.manager.HttpManager;
import com.rambostudio.zojoz.taxiparty.manager.PartyListManager;
import com.rambostudio.zojoz.taxiparty.manager.UserManager;
import com.rambostudio.zojoz.taxiparty.model.Party;
import com.rambostudio.zojoz.taxiparty.model.User;
import com.rambostudio.zojoz.taxiparty.model.UserInParty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class AddPartyFragment extends Fragment implements View.OnClickListener {

    AppCompatSpinner spinner;
    String[] SPINNERLIST = {"Free", "Share"};
    AppCompatButton btnAddParty;
    EditText edtDestination;

    public AddPartyFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static AddPartyFragment newInstance() {
        AddPartyFragment fragment = new AddPartyFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_add_party, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Party");
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        spinner = (AppCompatSpinner) rootView.findViewById(R.id.spinnerPartyType);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        spinner.setAdapter(arrayAdapter);
        btnAddParty = (AppCompatButton) rootView.findViewById(R.id.btnAddParty);
        btnAddParty.setOnClickListener(this);
        edtDestination = (EditText) rootView.findViewById(R.id.edtDestination);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddParty:
                addParty();
        }
    }

    private void addParty() {
        //validate form
        //insert Party
        //insert user in party

        if (validateForm()) {
            Party party = new Party();
            party.setDestination(edtDestination.getText().toString());
            party.setCreatedAt(new Date());
            party.setType(getPartyType());
//            List<UserInParty> userList = new ArrayList<>();
//            UserInParty user = new UserInParty(
//                    UUID.randomUUID().toString(),
//                    null,
//                    UserManager.getInstance().getUser().getUid(),
//                    new Date(),
//                    1);
//            userList.add(user);
            FirebaseManager.getInstance().createOrUpdateParty(party, null);
//            PartyListManager.getInstance().addParttyList(party,);
//            Toast.makeText(Contextor.getInstance().getContext(), "Add Party success", Toast.LENGTH_SHORT).show();
        } else {
            return;
        }

    }

    private int getPartyType() {
        int type = 1;
        String strType = spinner.getSelectedItem().toString();
        switch (strType) {
            case "free":
                type = 1;
                break;
            case "share":
                type = 2;
                break;
            default:
                break;
        }
        return type;
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(edtDestination.getText().toString())) {
            edtDestination.setError("Required.");
            return false;
        } else {
            return true;
        }
    }
}
