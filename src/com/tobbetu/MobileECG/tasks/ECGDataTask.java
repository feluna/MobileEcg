package com.tobbetu.MobileECG.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.tobbetu.MobileECG.models.ECGData;
import com.tobbetu.MobileECG.service.Login;
import com.tobbetu.MobileECG.service.SendECGData;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 01.02.2014
 * Time: 19:07
 */
public class ECGDataTask extends AsyncTask<List<ECGData>, Void, Boolean>{

    Context context;
    ProgressDialog progressDialog;

    private List<ECGData> ecgDatas;

    public ECGDataTask(Context context, List<ECGData> list) {
        this.context = context;
        this.ecgDatas = list;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Please Wait", "Your data is sending", true, false);
    }

    @Override
    protected Boolean doInBackground(List<ECGData>... lists) {


        SendECGData newSendECGData = new SendECGData(ecgDatas);

        try {
            return newSendECGData.makeRequest();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        progressDialog.dismiss();
        if (aBoolean)
            Log.i("basarili", "tamamdir yolladimm panpi");
        else
            Log.i("basarisiz", "scitik panpi");
    }
}
