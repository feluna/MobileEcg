package com.tobbetu.MobileECG.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.tobbetu.MobileECG.R;
import com.tobbetu.MobileECG.adapter.MyViewPagerAdapter;
import com.tobbetu.MobileECG.models.User;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 01.02.2014
 * Time: 15:50
 */
public class RegisterActivity extends FragmentActivity {

    final String TAG = "RegisterActivity";
    static ViewPager viewPager;
    MyViewPagerAdapter myViewPagerAdapter;

    public static User user;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = new User();
        user.setDeviceID(getIntent().getStringExtra("regid"));

        viewPager = (ViewPager) findViewById(R.id.vpRegisterPager);
        myViewPagerAdapter = new com.tobbetu.MobileECG.adapter.MyViewPagerAdapter(getSupportFragmentManager(), RegisterActivity.this);

        if (viewPager != null)
            viewPager.setAdapter(myViewPagerAdapter);

    }

    public static void goNextPage(int i) {
        viewPager.setCurrentItem(i);
    }

    public static void goPreviousPage(int i) {
        viewPager.setCurrentItem(i);
    }
}