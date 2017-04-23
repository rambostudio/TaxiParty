package com.rambostudio.zojoz.taxiparty.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rambostudio.zojoz.taxiparty.R;
import com.rambostudio.zojoz.taxiparty.manager.FirebaseManager;
import com.rambostudio.zojoz.taxiparty.manager.UserManager;
import com.rambostudio.zojoz.taxiparty.model.User;
import com.rambostudio.zojoz.taxiparty.utils.Utils;

import java.util.Date;
import java.util.UUID;

public class LoginActivity extends BaseActivity implements  View.OnClickListener ,GoogleApiClient.OnConnectionFailedListener{


    private static final String TAG = "Login";
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    AppCompatButton btnCustomGmailLogin, btnCustomFacebookLogin;
    TextView txtLoginErrorMessage;


    // Google
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;


    // firebase
    private DatabaseReference mRootRef, mUsersRef, mMessageRef;
    private static final String CHILD_USERS = "users";
    private static final String CHILD_MESSAGES = "messages";
    private static final String UID = "id-12345";
    private ValueEventListener mValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initInstances();
        authentication();
        initializeProviders();
        initFireBase();

    }

    private void initFireBase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        mRootRef = firebaseDatabase.getReference();
        mUsersRef = mRootRef.child(CHILD_USERS);
        mMessageRef = mRootRef.child(CHILD_MESSAGES);

        setEventListener();
    }

    private void setEventListener() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void initializeProviders() {
        initFacebookProvider();
        initGmailProvider();

    }

    private void initGmailProvider() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initFacebookProvider() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.btnFacebookLogin);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                updateUI(null);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                updateUI(null);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        showProgressDialog();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    txtLoginErrorMessage.setTextColor(Color.RED);
                    txtLoginErrorMessage.setText(task.getException().getMessage());
                }
                hideProgressDialog();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            txtLoginErrorMessage.setText("Authentication failed.");

                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [START signin]
    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void initInstances() {
        txtLoginErrorMessage = (TextView) findViewById(R.id.txtLoginErrorMessage);
        btnCustomGmailLogin = (AppCompatButton) findViewById(R.id.btnCustomGmailLogin);
        btnCustomFacebookLogin = (AppCompatButton) findViewById(R.id.btnCustomFacebookLogin);
        btnCustomFacebookLogin.setOnClickListener(this);
        btnCustomGmailLogin.setOnClickListener(this);
    }

    /*
     **********************  Function   *********************
     */

    private void authentication() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    insertOrUpdateUser(user);
                    UserManager.getInstance().setUser(user);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };
    }

    private void insertOrUpdateUser(FirebaseUser mUser) {
        User user = new User(
                mUser.getUid(),
                mUser.getDisplayName(),
                mUser.getEmail(),
                new Date(),
                new Date(),
                new Date(),
                mUser.getPhotoUrl().toString()
        );
        FirebaseManager.getInstance().createOrUpdateUser(user);


    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            goToMainActivity();
        } else {
            txtLoginErrorMessage.setVisibility(View.VISIBLE);
        }
        Utils.hideProgressDialog();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btnLoginGmail:
//                googleSignIn();
//                break;
            case R.id.btnCustomFacebookLogin:
                findViewById(R.id.btnFacebookLogin).performClick();
                break;
            case R.id.btnCustomGmailLogin:
                googleSignIn();
                break;
            default:
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
        txtLoginErrorMessage.setText("Google Play Services error.");

    }

    public void googleSignOut() {
        FirebaseAuth.getInstance().signOut();
    }
}
