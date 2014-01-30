package com.tobbetu.MobileECG.activities;

import android.app.Activity;
import android.os.Bundle;

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

    //checks if one of the input is empty
    protected boolean isEmpty(String... params) {
        for (String param: params){
            if (param.equals(""))
                return true;
        }
        return false;
    }
}