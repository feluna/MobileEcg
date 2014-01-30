package com.tobbetu.MobileECG.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.tobbetu.MobileECG.activities.ConnectionActivity;
import com.tobbetu.MobileECG.activities.MainActivity;
import com.tobbetu.MobileECG.models.User;
import org.apache.http.HttpStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 21.01.2014
 * Time: 22:24
 */
public class LoginTask extends AsyncTask<String,Void, User>{

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
    protected User doInBackground(String... strings) {
        // TODO will implement after Request class

        return null;
    }

    @Override
    protected void onPostExecute(User user) {
        progressDialog.dismiss();

        //change screen
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
