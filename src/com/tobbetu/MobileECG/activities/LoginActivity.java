package com.tobbetu.MobileECG.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.tobbetu.MobileECG.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 21.01.2014
 * Time: 21:10
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    EditText etUsername, etPassword;
    TextView tvRegister;
    Button bDoLogin;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    private void initialize() {
        etUsername = (EditText) findViewById(R.id.etLoginUsername);
        etPassword = (EditText) findViewById(R.id.etLoginPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        bDoLogin = (Button) findViewById(R.id.bDoLogin);

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
                Toast.makeText(this, "login yapacagız", Toast.LENGTH_LONG).show();
                break;
            case R.id.tvRegister:
                Toast.makeText(this, "register yapacagız", Toast.LENGTH_LONG).show();
                break;
        }
    }
}