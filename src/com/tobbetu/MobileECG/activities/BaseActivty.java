package com.tobbetu.MobileECG.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 21.01.2014
 * Time: 22:44
 */
public class BaseActivty extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //checks if one of the components is null
    protected boolean isNull(View... params) {
        for (View param: params) {
            if (param == null)
                return true;
        }
        return false;
    }

    //checks if one of the input is empty
    protected boolean isEmpty(String... params) {
        for (String param: params){
            if (param.equals(""))
                return true;
        }
        return false;
    }

    //checks if str1 equals str2
    protected boolean isEqual(String str1, String str2) {
        return str1.equals(str2);
    }

    protected void createToast(String message) {
        Toast.makeText(BaseActivty.this, message, Toast.LENGTH_LONG).show();
    }
}