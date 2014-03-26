package com.tobbetu.MobileECG.service;

import android.util.Log;
import com.tobbetu.MobileECG.backend.Requests;
import com.tobbetu.MobileECG.models.User;
import com.tobbetu.MobileECG.util.HttpURL;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 01.02.2014
 * Time: 16:58
 */
public class Register {

    private final String TAG = "Register";
    private final String registerInfo;

    public Register(User user) {

        JSONObject register = new JSONObject();

        Date now = new Date();

        try {
            register.put("birthday", user.getBirthday().getTime());
            register.put("name", user.getName());
            register.put("surname", user.getSurname());
            register.put("username", user.getUsername());
            register.put("password", user.getPassword());
            register.put("phoneNumber", user.getPhoneNumber());
            register.put("address", user.getAddress());
            register.put("deviceID", user.getDeviceID());

            register.put("sex", user.getSex());
            register.put("activityFrequency", user.getActivityFrequency());
            register.put("weight", user.getWeight());
            register.put("height", user.getHeight());
            register.put("smokingFrequency", user.getSmokingFrequency());
            register.put("alcoholUsageFrequency", user.getAlcoholUsageFrequency());

            register.put("kolesterolLDL", user.getKolesterolLDL());
            register.put("kolesterolHDL", user.getKolesterolHDL());
            register.put("hasHypertension", user.isHasHypertension());
            register.put("hasDiabetes", user.isHasDiabetes());

            register.put("bmi", user.getBmi());

        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }

        this.registerInfo = register.toString();
    }

    public boolean makeRequest() throws IOException, JSONException {
        HttpResponse registerResponse = Requests.post(HttpURL.OP_REGISTER, registerInfo);

        Log.i(TAG, "status code : " + registerResponse.getStatusLine().getStatusCode());

        if (Requests.checkStatusCode(registerResponse, HttpStatus.SC_OK))
            return true;
        else if (Requests.checkStatusCode(registerResponse, HttpStatus.SC_BAD_REQUEST))
            return false; //user exists
        else
            return false; //unexpected status;
    }
}
