package com.tobbetu.MobileECG.service;

import android.util.Log;
import com.tobbetu.MobileECG.backend.Requests;
import com.tobbetu.MobileECG.models.ECGData;
import com.tobbetu.MobileECG.util.HttpURL;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 01.02.2014
 * Time: 19:12
 */
public class SendECGData {

    private final String TAG = "SendECGData";
    private final String sendECGDataInfo;

    public SendECGData(List<ECGData> ecgDatas) {

        JSONArray jsonArray = new JSONArray();

        for (ECGData ecgData: ecgDatas) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("date", ecgData.getDate());
                jsonObject.put("latitude", ecgData.getLatitude());
                jsonObject.put("longitude", ecgData.getLongitude());
                jsonObject.put("ra_ll", ecgData.getRa_ll());
                jsonObject.put("la_ll", ecgData.getLa_ll());
                jsonObject.put("label", ecgData.getUserState());
                jsonObject.put("raw_ra_ll", ecgData.getRAW_ra_ll());
                jsonObject.put("raw_la_ll", ecgData.getRAW_la_ll());

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        this.sendECGDataInfo = jsonArray.toString();
    }

    public boolean makeRequest() throws IOException, JSONException {
        HttpResponse sendECGDataResponse = Requests.post(HttpURL.OP_SEND_ECG_DATA, sendECGDataInfo);

        Log.i(TAG, "status code : " + sendECGDataResponse.getStatusLine().getStatusCode());

        if (Requests.checkStatusCode(sendECGDataResponse, HttpStatus.SC_OK))
            return true;
        else
            return false; //unexpected status;
    }
}
