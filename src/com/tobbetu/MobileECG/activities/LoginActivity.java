package com.tobbetu.MobileECG.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tobbetu.MobileECG.R;
import com.tobbetu.MobileECG.android_service.MobileECGService;
import com.tobbetu.MobileECG.tasks.LoginTask;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 21.01.2014
 * Time: 21:10
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    EditText etUsername, etPassword;
    TextView tvRegister;
    Button bDoLogin;

    //GCM
    private static final String SENDER_ID = "678955541563";
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "GCMDemo";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    String regid;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Location loc = MobileECGService.getLocation();

        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            } else {
                initialize();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    private void initialize() {

        //bindings
        etUsername = (EditText) findViewById(R.id.etLoginUsername);
        etPassword = (EditText) findViewById(R.id.etLoginPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        bDoLogin = (Button) findViewById(R.id.bDoLogin);

        //set listeners
        tvRegister.setOnClickListener(this);
        bDoLogin.setOnClickListener(this);

        //etUsername.setText("patient");
        //etPassword.setText("pass");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bDoLogin:
                String userName = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String regID = regid;

                /*
                if (isEmpty(userName, password)) {
//                    createToast("please fill all areas");
                    new LoginTask(LoginActivity.this).execute("anil", "123", regid);
                } else {
                    new LoginTask(LoginActivity.this).execute(userName, password);
                } */
                //new LoginTask(LoginActivity.this).execute("patient", "pass", regid);
                new LoginTask(LoginActivity.this).execute(userName, password, regid);

                break;
            case R.id.tvRegister:
                Intent i = new Intent(this, RegisterActivity.class);
                i.putExtra("regid", regid);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        /*int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        */
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /*
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    */

    private void registerInBackground() {

        new AsyncTask<Void, String, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, msg);
                initialize();
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
        // Your implementation here.
        Log.i(TAG, "your regid is " + regid);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        //int appVersion = getAppVersion(context);
        //Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        //editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }
}