package com.tobbetu.MobileECG.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.tobbetu.MobileECG.activities.MainActivity;
import com.tobbetu.MobileECG.activities.RegisterActivity;
import com.tobbetu.MobileECG.models.User;
import com.tobbetu.MobileECG.service.Register;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 01.02.2014
 * Time: 16:50
 */
public class RegisterTask extends AsyncTask<User, Void, Boolean>{

    Context context;
    ProgressDialog progressDialog;

    public RegisterTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Please Wait", "Register started", true, false);
    }

    @Override
    protected Boolean doInBackground(User... users) {

        Register newRegister = new Register(users[0]);

        try {
            return newRegister.makeRequest();
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
            Toast.makeText(context, "Register FAILED", Toast.LENGTH_LONG).show();
    }
}
