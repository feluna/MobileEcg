package com.tobbetu.MobileECG.activities;

import android.app.Activity;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import java.util.Collection;

import com.shimmerresearch.driver.*;
import com.shimmerresearch.android.*;
import com.tobbetu.MobileECG.R;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 11/01/14
 * Time: 17:00
 */
public class MainActivity extends Activity {
    private Shimmer mShimmerDevice = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String bluetoothAddress = "00:06:66:46:9A:2C";
        String deviceName = "Device";

        this.connectShimmer(bluetoothAddress , deviceName);
    }

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) { // handlers have a what identifier which is used to identify the type of msg
                case Shimmer.MESSAGE_READ:
                    if ((msg.obj instanceof ObjectCluster)) {	// within each msg an object can be include, objectclusters are used to represent the data structure of the shimmer device
                        ObjectCluster objectCluster =  (ObjectCluster) msg.obj;
                        if (objectCluster.mMyName.equals("Device")) {

                            Collection<FormatCluster> ecg_ra_ll = objectCluster.mPropertyCluster.get("ECG RA-LL");  // first retrieve all the possible formats for the current sensor device
                            if (ecg_ra_ll != null){
                                FormatCluster formatCluster = ObjectCluster.returnFormatCluster(ecg_ra_ll , "CAL"); // retrieve the calibrated data
                                Log.d("CalibratedData","ECG RA-LL: " + formatCluster.mData + " " + formatCluster.mUnits);
                            }

                            Collection<FormatCluster> ecg_la_ll = objectCluster.mPropertyCluster.get("ECG LA-LL");  // first retrieve all the possible formats for the current sensor device
                            if (ecg_la_ll != null){
                                FormatCluster formatCluster = ObjectCluster.returnFormatCluster(ecg_la_ll,"CAL"); // retrieve the calibrated data
                                Log.d("CalibratedData","ECG LA-LL: " + formatCluster.mData + " " + formatCluster.mUnits);
                            }

                        }
                    }
                    break;
                case Shimmer.MESSAGE_TOAST:
                    Log.d("toast",msg.getData().getString(Shimmer.TOAST));
                    Toast.makeText(getApplicationContext(), msg.getData().getString(Shimmer.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;

                case Shimmer.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case Shimmer.MSG_STATE_FULLY_INITIALIZED:
                            if (mShimmerDevice.getShimmerState() == Shimmer.STATE_CONNECTED){
                                Log.d("ConnectionStatus","Successful");
                                mShimmerDevice.writeEnabledSensors(Shimmer.SENSOR_ECG);
                                mShimmerDevice.writeSamplingRate(51.2);
                                mShimmerDevice.startStreaming();
                            }
                            break;
                        case Shimmer.STATE_CONNECTING:
                            Log.d("ConnectionStatus","Connecting");
                            break;
                        case Shimmer.STATE_NONE:
                            Log.d("ConnectionStatus","No State");
                            break;
                    }
                    break;

            }
        }
    };

    private void connectShimmer(String bluetoothAddress , String deviceName) {

        mShimmerDevice = new Shimmer(this, mHandler, deviceName ,false);
        mShimmerDevice.connect(bluetoothAddress,"default");

        Log.d("ConnectionStatus", "Trying");
    }
}