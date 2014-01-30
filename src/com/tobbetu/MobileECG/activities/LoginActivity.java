package com.tobbetu.MobileECG.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.tobbetu.MobileECG.R;
import com.tobbetu.MobileECG.tasks.LoginTask;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 21.01.2014
 * Time: 21:10
 */
public class LoginActivity extends BaseActivty implements View.OnClickListener {

    EditText etUsername, etPassword;
    TextView tvRegister;
    Button bDoLogin;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    private void initialize() {

        //bindings
        etUsername = (EditText) findViewById(R.id.etLoginUsername);
        etPassword = (EditText) findViewById(R.id.etLoginPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        bDoLogin = (Button) findViewById(R.id.bDoLogin);

        //set listeners
        tvRegister.setOnClickListener(this);
        bDoLogin.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bDoLogin:
                String userName = etUsername.getText().toString();
                String password = etPassword.getText().toString();

//                if (isEmpty(userName, password)) {
//                    Toast.makeText(this, "please fill all areas", Toast.LENGTH_LONG).show();
//                } else {
                    new LoginTask(LoginActivity.this).execute(userName, password);
//                }
                break;
            case R.id.tvRegister:
                Toast.makeText(this, "register yapacagız", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}