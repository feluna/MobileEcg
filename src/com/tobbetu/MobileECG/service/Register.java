package com.tobbetu.MobileECG.service;

import android.util.Log;
import com.tobbetu.MobileECG.backend.Requests;
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

    public Register(String[] params) {

        JSONObject register = new JSONObject();

        Date now = new Date();

        try {
            register.put("name", params[0]);
            register.put("surname", params[1]);
            register.put("birthday", null);
            register.put("username", params[3]);
            register.put("password", params[4]);
            register.put("phoneNumber", params[5]);
            register.put("address", params[6]);

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
