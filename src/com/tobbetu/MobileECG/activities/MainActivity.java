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
import android.widget.*;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.shimmerresearch.android.Shimmer;
import com.shimmerresearch.driver.FormatCluster;
import com.shimmerresearch.driver.ObjectCluster;
import com.shimmerresearch.graph.GraphView;
import com.tobbetu.MobileECG.R;
import com.tobbetu.MobileECG.models.ECGData;
import com.tobbetu.MobileECG.tasks.ECGDataTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

    Button bConnectToShimmer, bDisconnectFromShimmer, bStartStreamFromShimmer, bStopStreamFromShimmer;
    TextView tvConnectionStatus;
    Spinner spinnerActivityLabel;
    GraphView myGraphView = null;

    Message shimmerMessage = null;

    Handler handler = null;
    Runnable runnable = null;

    List<ECGData> ecgDatas = new ArrayList<ECGData>();

    private boolean isConnected = false;
    private static int mGraphSubSamplingCount = 0;

    private Pubnub pubnub;
    com.pubnub.api.Callback callback;
    JSONArray jsonArray = new JSONArray();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        context = this;

        pubnub = new Pubnub(getString(R.string.pubnup_publish_key), getString(R.string.pubnup_subscribe_key));

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        callback = new com.pubnub.api.Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
            }

            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };
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

        tvConnectionStatus = (TextView) findViewById(R.id.tvConnectionStatus);
        spinnerActivityLabel = (Spinner) findViewById(R.id.spinnerActivityLabel);
        List<String> listOfSpinnerActions = new ArrayList<String>();
        listOfSpinnerActions.add("default");
        listOfSpinnerActions.add("Resting");
        listOfSpinnerActions.add("Sleeping");
        listOfSpinnerActions.add("Running");
        listOfSpinnerActions.add("Walking");
        listOfSpinnerActions.add("Eating");
        listOfSpinnerActions.add("Working");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listOfSpinnerActions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerActivityLabel.setAdapter(dataAdapter);

        bConnectToShimmer = (Button) findViewById(R.id.bConnectToShimmer);
        bConnectToShimmer.setOnClickListener(this);

        bDisconnectFromShimmer = (Button) findViewById(R.id.bDisconnectFromShimmer);
        bDisconnectFromShimmer.setOnClickListener(this);

        bStartStreamFromShimmer = (Button) findViewById(R.id.bStartStreamFromShimmer);
        bStartStreamFromShimmer.setOnClickListener(this);

        bStopStreamFromShimmer = (Button) findViewById(R.id.bStopStreamFromShimmer);
        bStopStreamFromShimmer.setOnClickListener(this);

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

                        int[] dataArray = new int[2];
                        String units = "u12";

                        ECGData ecgData = new ECGData();
                        ecgData.setLatitude(0);
                        ecgData.setLongitude(0);

                        if (objectCluster.mMyName.equals("Device")) {

                            Collection<FormatCluster> ecg_ra_ll = objectCluster.mPropertyCluster.get("ECG RA-LL");  // first retrieve all the possible formats for the current sensor device
                            if (ecg_ra_ll != null) {
                                FormatCluster formatCluster = ObjectCluster.returnFormatCluster(ecg_ra_ll, "CAL"); // retrieve the calibrated data
                                if (formatCluster != null) {
                                    Log.d("CalibratedData", "ECG RA-LL: " + formatCluster.mData + " " + formatCluster.mUnits);

                                    //Obtain data for graph
                                    dataArray[0] = (int) ObjectCluster.returnFormatCluster(ecg_ra_ll, "RAW").mData;
                                    ecgData.setRAW_ra_ll(dataArray[0]);
                                    ecgData.setRa_ll(formatCluster.mData);
                                    ecgData.setUserState(spinnerActivityLabel.getSelectedItemPosition() - 1);
                                }
                            }

                            Collection<FormatCluster> ecg_la_ll = objectCluster.mPropertyCluster.get("ECG LA-LL");  // first retrieve all the possible formats for the current sensor device
                            if (ecg_la_ll != null) {
                                FormatCluster formatCluster = ObjectCluster.returnFormatCluster(ecg_la_ll, "CAL"); // retrieve the calibrated data
                                if (formatCluster != null) {
                                    Log.d("CalibratedData", "ECG LA-LL: " + formatCluster.mData + " " + formatCluster.mUnits);

                                    //Obtain data for graph
                                    dataArray[1] = (int) ObjectCluster.returnFormatCluster(ecg_la_ll, "RAW").mData;
                                    ecgData.setRAW_la_ll(dataArray[1]);
                                    ecgData.setLa_ll(formatCluster.mData);
                                    ecgData.setUserState(spinnerActivityLabel.getSelectedItemPosition() - 1);
                                }
                            }

                            // ecg data timestamp
                            byte[] timeAsByteArray = objectCluster.mSystemTimeStamp;
                            ByteBuffer buffer = ByteBuffer.allocate(8);
                            buffer.put(timeAsByteArray);
                            buffer.flip();
                            ecgData.setDate(new Date(buffer.getLong()));


                            ecgDatas.add(ecgData);

                            int maxNumberofSamplesPerSecond = 50; //Change this to increase/decrease the number of samples which are graphed
                            int subSamplingCount = 0;
                            if (mShimmerDevice.getSamplingRate() > maxNumberofSamplesPerSecond) {
                                subSamplingCount = (int) (mShimmerDevice.getSamplingRate() / maxNumberofSamplesPerSecond);
                                mGraphSubSamplingCount++;
                            }
                            if (mGraphSubSamplingCount == subSamplingCount) {
                                myGraphView.setDataWithAdjustment(dataArray, "Shimmer : " + deviceName, units);

                                mGraphSubSamplingCount = 0;

                                //live stream
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("r", dataArray[0]);
                                    jsonObject.put("l", dataArray[1]);
                                    jsonArray.put(jsonObject);

                                    if (jsonArray.length() == 24) {
                                        pubnub.publish("hello_world", jsonArray, callback);
                                        jsonArray = new JSONArray();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                //to the server
                                if (ecgDatas.size() == 800) {
                                    new ECGDataTask(context, ecgDatas).execute();
                                    ecgDatas = new ArrayList<ECGData>();
                                }

                                ecgDatas = new ArrayList<ECGData>();
                            }
                        }
                    }
                    break;
                case Shimmer.MESSAGE_TOAST:
                    Log.d("toast", msg.getData().getString(Shimmer.TOAST));

                    //connection is failed, retry connection
                    if (msg.getData().getString(Shimmer.TOAST).equals(getResources().getString(R.string.unable_to_connect_device))) {

                        Toast.makeText(context, msg.getData().getString(Shimmer.TOAST) + " Retrying to connect",
                                Toast.LENGTH_SHORT).show();

//                        MainActivity.this.connectShimmer();
                    } else {
                        Toast.makeText(context, msg.getData().getString(Shimmer.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }


                    break;

                case Shimmer.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case Shimmer.MSG_STATE_FULLY_INITIALIZED:
                            if (mShimmerDevice.getShimmerState() == Shimmer.STATE_CONNECTED) {
                                Log.d("ConnectionStatus", "Successful");
                                setTvConnectionStatus("Connected");
                                configureDeviceForStreaming();
                            }
                            break;
                        case Shimmer.STATE_CONNECTING:
                            Log.d("ConnectionStatus", "Connecting");
                            setTvConnectionStatus("Connecting");
                            break;
                        case Shimmer.STATE_NONE:
                            Log.d("ConnectionStatus", "No State");
                            setTvConnectionStatus("Not Connected");
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
        disableAndEnableButtons(bConnectToShimmer, bDisconnectFromShimmer);

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

        isConnected = false;
        finish();
        onDestroy();

        System.exit(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bConnectToShimmer:
                connect();
                break;
            case R.id.bDisconnectFromShimmer:
                disconnect();
                break;
            case R.id.bStartStreamFromShimmer:
                startStreaming();
                break;
            case R.id.bStopStreamFromShimmer:
                stopStreaming();
                break;
        }
    }

    private void disableAndEnableButtons(Button disableButton, Button enableButton) {
        disableButton.setVisibility(Button.GONE);
        enableButton.setVisibility(Button.VISIBLE);
    }

    private void setTvConnectionStatus(String status) {
        tvConnectionStatus.setText(status);
    }


    private void connect() {
        this.connectShimmer();
    }

    private void disconnect() {
        disableAndEnableButtons(bDisconnectFromShimmer, bConnectToShimmer);
        try {
            stopStreaming();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Disconnect", "ERROR on disconnect", e);
        }
    }

    private void startStreaming() {
        if (isConnected) {
            mShimmerDevice.startStreaming();
            disableAndEnableButtons(bStartStreamFromShimmer, bStopStreamFromShimmer);
        } else {
            Toast.makeText(this, "You have to connect shimmer device first", Toast.LENGTH_LONG).show();
        }
    }

    private void stopStreaming() {
        mShimmerDevice.stopStreaming();
        disableAndEnableButtons(bStopStreamFromShimmer, bStartStreamFromShimmer);
    }
}