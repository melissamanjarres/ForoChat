package com.uninorte.googleauth;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "SingIn" ;
    private GoogleApiClient mgoogleApiClient;
    private GoogleApiClient googleApiClient;
    private  int RC_SING = 1;
    //Facebook
    LoginButton loginButton;
    CallbackManager callbackManager;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize(); //Esto es súper importante aquí
        setContentView(R.layout.activity_main);
        //Login Detalles
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        getLoginDetails(loginButton);
        //

        GoogleSignInOptions singin = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mgoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, singin).build();

        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }
    //Facebook Login
    protected void getLoginDetails(LoginButton loginButton) {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }


            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }
//


//Facebook needs these three
        @Override
    protected void onResume(){
            super.onResume();
               // Logs 'install' and 'app activate' App Events.<br />
                AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
            super.onPause();
                // Logs 'app deactivate' App Event.<br />
                AppEventsLogger.deactivateApp(this);
    }

    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }
//

    public void Login() {
        Intent singInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(singInIntent, RC_SING);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SING){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(intent);
        }else
            callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, requestCode+"");
    }

    private void handleSingInResult(GoogleSignInResult result) {

        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String personName = account.getDisplayName();
            String mail = account.getEmail();
            Uri personPhoto = account.getPhotoUrl();
            Toast.makeText(this, "Handled SingIn Result "+ personName, Toast.LENGTH_LONG).show();
        }else
            Toast.makeText(this, "Handled SingIn Result noo", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_in_button:
                Login();
                break;
        }
    }



}
