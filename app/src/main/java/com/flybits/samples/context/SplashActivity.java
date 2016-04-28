package com.flybits.samples.context;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.flybits.core.api.Flybits;
import com.flybits.core.api.context.plugins.activity.ActivityProvider;
import com.flybits.core.api.context.plugins.battery.BatteryLifeProvider;
import com.flybits.core.api.context.plugins.beacon.BeaconProvider;
import com.flybits.core.api.context.plugins.carrier.CarrierProvider;
import com.flybits.core.api.context.plugins.fitness.FitnessProvider;
import com.flybits.core.api.context.plugins.language.LanguageProvider;
import com.flybits.core.api.context.plugins.location.LocationProvider;
import com.flybits.core.api.context.plugins.network.NetworkProvider;
import com.flybits.core.api.exceptions.FeatureNotSupportedException;
import com.flybits.core.api.interfaces.IRequestCallback;
import com.flybits.core.api.interfaces.IRequestLoggedIn;
import com.flybits.core.api.models.User;
import com.flybits.core.api.utils.filters.LoginOptions;
import com.flybits.samples.context.utilities.ConnectivityUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

public class SplashActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 10;

    //Need For Fitness Context Plugin
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Initialize GoogleApiClient and Connect to it. This is only needed for the Fitness Context Plugin.
        initGooglePlayServices();
        mGoogleApiClient.connect();

        //Check that Location permissions have been granted. This is only needed for Location/Beacon Context Plugin.
        checkForLoginPermissions();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void checkForLoginPermissions() {

        if (Build.VERSION.SDK_INT < 23) {

            //Device SDK is less 6.0 therefore permissions do not need to be asked for specifically.
            checkIfUserIsAlreadyLoggedIn(true);
        } else {
            if (ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //Request the location permission as the User has not yet granted it to the application.
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);

            } else if (ContextCompat.checkSelfPermission(SplashActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //User has already granted the location permission in a previous session.
                checkIfUserIsAlreadyLoggedIn(true);
            }
        }
    }

    private void initGooglePlayServices() {

        //Initialize GoogleApiClient for Fitness ContextPlugin -> Fitness.HISTORY_API & Scopes.FITNESS_ACTIVITY_READ_WRITE are mandatory.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d("Testing", "You Are Connected!");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d("Testing", "onConnectionSuspended: " + i);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d("Testing", "onConnectionFailed: " + connectionResult);
                    }
                })
                .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d("Testing", "onConnectionFailed: " + result.getErrorMessage());
                    }
                })
                .build();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION: {
                //Confirm the user has given permission for location to the application.
                boolean isLocationActivated = (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED);
                checkIfUserIsAlreadyLoggedIn(isLocationActivated);
                return;
            }
        }
    }

    private void checkIfUserIsAlreadyLoggedIn(final boolean isLocationActivated) {

        //Confirm that the user is currently connected to the Internet.
        if (ConnectivityUtils.isOnline(this)) {

            //Check to see if the application is already logged in. If so, there is no need to already login.
            Flybits.include(SplashActivity.this).isUserLoggedIn(true, new IRequestLoggedIn() {
                @Override
                public void onLoggedIn(User user) {

                    /*
                    User is already logged in therefore, the application should activate context.
                    This must be done after the application has logged in.
                      */
                    activateContext(isLocationActivated);

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onNotLoggedIn() {

                    //The application is not logged in therefore log the application in.
                    startLogin(isLocationActivated);
                }
            });

        } else {

            //No Internet is currently available hence display an error.
            showAlertDialog(R.string.errorTitleNoInternet, R.string.errorMessageNoInternet, R.string.errorBtnPositive);
        }
    }

    private void startLogin(final boolean isLocationActivated) {

        //Log the application into Flybits anonymously
        LoginOptions filterLogin = new LoginOptions.Builder(SplashActivity.this)
                .loginAnonymously()
                .setRememberMeToken()
                .setDeviceOSVersion()
                .build();

        Flybits.include(SplashActivity.this).login(filterLogin, new IRequestCallback<User>() {

            @Override
            public void onSuccess(User me) {
                  /*
                    User is already logged in therefore, the application should activate context.
                    This must be done after the application has logged in.
                      */
                activateContext(isLocationActivated);

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(String s) {}

            @Override
            public void onException(Exception e) {
                showAlertDialog(getString(R.string.errorTitleLogin), e.getMessage(), getString(R.string.errorBtnPositive));
            }

            @Override
            public void onCompleted() {}
        });
    }

    private void activateContext(final boolean isLocationActivated){
        try {
            ActivityProvider provider = new ActivityProvider(SplashActivity.this, 60000);
            Flybits.include(SplashActivity.this).activateContext(null, provider);

            BatteryLifeProvider provider2 = new BatteryLifeProvider(SplashActivity.this, 60000);
            Flybits.include(SplashActivity.this).activateContext(null, provider2);

            CarrierProvider provider3 = new CarrierProvider(SplashActivity.this, 60000);
            Flybits.include(SplashActivity.this).activateContext(null, provider3);

            LanguageProvider provider4 = new LanguageProvider(SplashActivity.this, 60000);
            Flybits.include(SplashActivity.this).activateContext(null, provider4);

            NetworkProvider provider5 = new NetworkProvider(SplashActivity.this, 60000);
            Flybits.include(SplashActivity.this).activateContext(null, provider5);

            //Only Allow Location/Beacon Providers to be activated if the user has allowed the application to use their location
            if (isLocationActivated){
                LocationProvider provider6 = new LocationProvider(SplashActivity.this, 60000);
                Flybits.include(SplashActivity.this).activateContext(null, provider6);

                BeaconProvider provider7 = new BeaconProvider(SplashActivity.this, 60000);
                Flybits.include(SplashActivity.this).activateContext(null, provider7);
            }

            //Only Allow Fitness Providers if the user has successfully connected to GoogleApiClient
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()){
                FitnessProvider provider8 = new FitnessProvider(SplashActivity.this, 60000);
                Flybits.include(SplashActivity.this).activateContext(mGoogleApiClient, provider8);
            }
        }catch (FeatureNotSupportedException exception){

        }
    }

    private void showAlertDialog(int title, int message, int buttonPositive){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(buttonPositive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SplashActivity.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showAlertDialog(String title, String message, String buttonPositive){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(buttonPositive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SplashActivity.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
