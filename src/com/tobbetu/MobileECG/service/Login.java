package com.tobbetu.MobileECG.service;

import android.util.Log;
import com.tobbetu.MobileECG.backend.Requests;
import com.tobbetu.MobileECG.util.HttpURL;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 01.02.2014
 * Time: 14:58
 */
public class Login {

    private final String TAG = "Login";
    private final String loginInfo;

    public Login( String[] params) {

        JSONObject login = new JSONObject();
        try {
            login.put("username", params[0]);
            login.put("password", params[1]);
            login.put("deviceID", params[2]);
        } catch ( JSONException e) {
            Log.e(TAG, "JSONException", e);
        }

        this.loginInfo = login.toString();
    }


    public boolean makeRequest() throws IOException, JSONException {
        HttpResponse loginResponse = Requests.post(HttpURL.OP_LOGIN, loginInfo);

        if (Requests.checkStatusCode(loginResponse, HttpStatus.SC_OK)) {
            return true;
        } else {
            return false;
        }
    }
}
