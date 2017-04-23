package com.rambostudio.zojoz.taxiparty.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.listener.UserSignOutListener;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class UserFragment extends Fragment {

    UserSignOutListener userSignOutListener;
    AppCompatButton btnSignOut;
    AppCompatImageView ivImage;
    TextView tvName, tvEmail;
    public UserFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        initInstances(rootView, savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tvName.setText(user.getDisplayName());
        tvEmail.setText(user.getEmail());
        Glide.with(getActivity())
                .load(user.getPhotoUrl())
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(ivImage);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        ivImage = (AppCompatImageView) rootView.findViewById(R.id.ivImage);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        tvEmail = (TextView) rootView.findViewById(R.id.tvEmail);
        btnSignOut = (AppCompatButton) rootView.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignOut();
                userSignOutListener = (UserSignOutListener) getActivity();
                userSignOutListener.onUserSignOut();
            }
        });
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
    public void googleSignOut() {
        FirebaseAuth.getInstance().signOut();
    }



}
