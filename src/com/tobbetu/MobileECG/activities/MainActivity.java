package com.tobbetu.MobileECG.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.shimmerresearch.android.Shimmer;
import com.shimmerresearch.driver.FormatCluster;
import com.shimmerresearch.driver.ObjectCluster;
import com.shimmerresearch.graph.GraphView;
import com.tobbetu.MobileECG.R;
import com.tobbetu.MobileECG.models.ECGData;
import com.tobbetu.MobileECG.tasks.ECGDataTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 11/01/14
 * Time: 17:00
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private static Context context;
    static final int REQUEST_ENABLE_BT = 1;

    private Shimmer mShimmerDevice = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    String bluetoothAddress = "00:06:66:46:BD:90";
    String deviceName = "Device";

    Button bConnectionWithShimmer, bStreamFromShimmer;
    GraphView myGraphView = null;

    Message shimmerMessage = null;

    Handler handler = null;
    Runnable runnable = null;

    List<ECGData> ecgDatas = new ArrayList<ECGData>();

    private boolean isConnected = false;
    private boolean isStreaming = false;

    private static int mGraphSubSamplingCount = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bluetoothIntent, REQUEST_ENABLE_BT);
        } else {
            if (mShimmerDevice == null) {
                init();
            }
        }
    }

    private void init() {
        bConnectionWithShimmer = (Button) findViewById(R.id.bConnectionWithShimmer);
        bConnectionWithShimmer.setOnClickListener(this);

        bStreamFromShimmer = (Button) findViewById(R.id.bStreamFromShimmer);
        bStreamFromShimmer.setOnClickListener(this);

        myGraphView = (GraphView) findViewById(R.id.graphView);

        mShimmerDevice = new Shimmer(this, mHandler, deviceName, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mShimmerDevice != null)
            mShimmerDevice.stop();
    }

    private void showStatus() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mShimmerDevice != null) {
                    Log.e("CalibratedData", "Status : " + mShimmerDevice.getShimmerState());
                }
                showStatus();
            }
        };

        handler.postDelayed(runnable, 4000);
    }

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            shimmerMessage = msg;
            switch (msg.what) { // handlers have a what identifier which is used to identify the type of msg
                case Shimmer.MESSAGE_READ:
                    if ((msg.obj instanceof ObjectCluster)) {    // within each msg an object can be include, objectclusters are used to represent the data structure of the shimmer device
                        ObjectCluster objectCluster = (ObjectCluster) msg.obj;

                        String[] sensorName = new String[2];
                        int[] dataArray = new int[2];
                        double[] calibratedDataArray = new double[2];
                        sensorName[0] = "ECG RA-LL";
                        sensorName[1] = "ECG LA-LL";
                        String units = "u12";
                        String calibratedUnits = "";

                        ECGData ecgData = new ECGData();
                        ecgData.setDate(null);
                        ecgData.setLatitude(0);
                        ecgData.setLongitude(0);

                        if (objectCluster.mMyName.equals("Device")) {

                            Collection<FormatCluster> ecg_ra_ll = objectCluster.mPropertyCluster.get("ECG RA-LL");  // first retrieve all the possible formats for the current sensor device
                            if (ecg_ra_ll != null) {
                                FormatCluster formatCluster = ObjectCluster.returnFormatCluster(ecg_ra_ll, "CAL"); // retrieve the calibrated data
                                if (formatCluster != null) {
                                    Log.d("CalibratedData", "ECG RA-LL: " + formatCluster.mData + " " + formatCluster.mUnits);
                                    //Obtain data for text view
                                    calibratedDataArray[0] = formatCluster.mData;
                                    calibratedUnits = formatCluster.mUnits;

                                    //Obtain data for graph
                                    dataArray[0] = (int) ((FormatCluster) ObjectCluster.returnFormatCluster(ecg_ra_ll, "RAW")).mData;
                                    ecgData.setRa_ll(formatCluster.mData);
                                }
                            }

                            Collection<FormatCluster> ecg_la_ll = objectCluster.mPropertyCluster.get("ECG LA-LL");  // first retrieve all the possible formats for the current sensor device
                            if (ecg_la_ll != null) {
                                FormatCluster formatCluster = ObjectCluster.returnFormatCluster(ecg_la_ll, "CAL"); // retrieve the calibrated data
                                if (formatCluster != null) {
                                    Log.d("CalibratedData", "ECG LA-LL: " + formatCluster.mData + " " + formatCluster.mUnits);

                                    //Obtain data for text view
                                    calibratedDataArray[1] = formatCluster.mData;
                                    calibratedUnits = formatCluster.mUnits;

                                    //Obtain data for graph
                                    dataArray[1] = (int) ((FormatCluster) ObjectCluster.returnFormatCluster(ecg_la_ll, "RAW")).mData;
                                    ecgData.setLa_ll(formatCluster.mData);
                                }
                            }

                            ecgDatas.add(ecgData);

                            int maxNumberofSamplesPerSecond=50; //Change this to increase/decrease the number of samples which are graphed
                            int subSamplingCount=0;
                            if (mShimmerDevice.getSamplingRate()>maxNumberofSamplesPerSecond){
                                subSamplingCount=(int) (mShimmerDevice.getSamplingRate()/maxNumberofSamplesPerSecond);
                                mGraphSubSamplingCount++;
                            }
                            if (mGraphSubSamplingCount==subSamplingCount){
                                myGraphView.setDataWithAdjustment(dataArray,"Shimmer : " + deviceName,units);

                                mGraphSubSamplingCount=0;


                                if (ecgDatas.size() == 500) {
                                    new ECGDataTask(MainActivity.this, ecgDatas).execute();

                                    ecgDatas = new ArrayList<ECGData>();
                                }

                            }
                        }
                    }
                    break;
                case Shimmer.MESSAGE_TOAST:
                    Log.d("toast", msg.getData().getString(Shimmer.TOAST));
                    Toast.makeText(getApplicationContext(), msg.getData().getString(Shimmer.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;

                case Shimmer.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case Shimmer.MSG_STATE_FULLY_INITIALIZED:
                            if (mShimmerDevice.getShimmerState() == Shimmer.STATE_CONNECTED) {
                                Log.d("ConnectionStatus", "Successful");
                                configureDeviceForStreaming();
                            }
                            break;
                        case Shimmer.STATE_CONNECTING:
                            Log.d("ConnectionStatus", "Connecting");
                            break;
                        case Shimmer.STATE_NONE:
                            Log.d("ConnectionStatus", "No State");
                            break;
                        case Shimmer.MESSAGE_STOP_STREAMING_COMPLETE:
                            Log.d("ConnectionStatus", "Stop Streaming Completed");
                    }
                    break;

            }
        }
    };

    private void connectShimmer() {
        mShimmerDevice.connect(bluetoothAddress, "default");
        Log.d("ConnectionStatus", "Trying");
    }

    private void configureDeviceForStreaming() {
        isConnected = true;
        bConnectionWithShimmer.setText("Disconnect");

        mShimmerDevice.writeEnabledSensors(Shimmer.SENSOR_ECG);
        mShimmerDevice.writeSamplingRate(51.2);
    }


    @Override
    public void onBackPressed() {

        if (handler != null) {
            handler.removeCallbacks(runnable);

            handler = null;
            runnable = null;
        }

        finish();
        onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bConnectionWithShimmer:
                if (!isConnected) {
                    this.connectShimmer();
                } else {
                    this.mShimmerDevice.stop();
                    isConnected = false;
                }
                break;
            case R.id.bStreamFromShimmer:
                if (!isStreaming) {
                    mShimmerDevice.startStreaming();
                    isStreaming = true;
                    bStreamFromShimmer.setText("Stop Streaming");
                } else {
                    mShimmerDevice.stopStreaming();
                    isStreaming = false;
                }
                break;
        }
    }
}