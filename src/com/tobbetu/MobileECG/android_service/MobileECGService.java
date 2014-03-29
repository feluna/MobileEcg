package com.tobbetu.MobileECG.android_service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by kanilturgut on 29/03/14, 20:07.
 */
public class MobileECGService extends Service implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener{

    public static final int UPDATE_INTERVAL = 5 * 1000;
    public static final int FAST_CEILING = 1 * 1000;

    private LocationRequest mLocationRequest = null;
    private LocationClient mLocationClient = null;

    private static Location myBestLoc = null;
    private boolean mInProgress;
    private IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MobileECGService getServerInstance() {
            return MobileECGService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(FAST_CEILING);

        mLocationClient = new LocationClient(this, this, this);
    }

    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode)
            return true;

        else
            return false;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (!servicesConnected() || mLocationClient.isConnected()
                || mInProgress)
            return START_STICKY;

        setUpLocationClientIfNeeded();
        if (!mLocationClient.isConnected() || !mLocationClient.isConnecting()
                && !mInProgress) {

            mInProgress = true;
            mLocationClient.connect();
        }

        return START_STICKY;
    }

    private void setUpLocationClientIfNeeded() {
        if (mLocationClient == null)
            mLocationClient = new LocationClient(this, this, this);

    }

    @Override
    public void onDestroy() {
        mInProgress = false;
        if (servicesConnected() && mLocationClient != null) {
            mLocationClient.removeLocationUpdates(this);
            mLocationClient = null;
        }

        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            myBestLoc = mLocationClient.getLastLocation();
        } catch (Exception e) {
            // don't post your last location because it doesn't exist
        }

        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    @Override
    public void onDisconnected() {
        mInProgress = false;
        mLocationClient = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (myBestLoc == null) {
            myBestLoc = location;
        } else if (location.getLatitude() != myBestLoc.getLatitude()
                && location.getLongitude() != myBestLoc.getLongitude()) {


            myBestLoc = location;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mInProgress = false;
    }

    public static Location getLocation() {
        return myBestLoc;
    }
}
