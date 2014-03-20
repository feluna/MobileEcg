package com.tobbetu.MobileECG.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.tobbetu.MobileECG.activities.MainActivity;
import com.tobbetu.MobileECG.service.Login;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 21.01.2014
 * Time: 22:24
 */
public class LoginTask extends AsyncTask<String,Void, Boolean>{

    Context context;
    ProgressDialog progressDialog;

    public LoginTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Please Wait", "Login started", true, false);
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        Login newLogin = new Login(strings);

        try {
            return newLogin.makeRequest();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        progressDialog.dismiss();

        if (result)
            context.startActivity(new Intent(context, MainActivity.class));
        else
            Toast.makeText(context, "Login FAILED", Toast.LENGTH_LONG).show();
    }
}
