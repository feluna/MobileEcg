package com.tobbetu.MobileECG.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.tobbetu.MobileECG.R;
import com.tobbetu.MobileECG.tasks.RegisterTask;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 01.02.2014
 * Time: 15:50
 */
public class RegisterActivity extends BaseActivity {

    private final String TAG = "RegisterActivity";
    EditText etName, etSurname, etBirthday, etUsername, etPassword, etEtPasswordAgain, etPhoneNumber, etAddress;
    Button bDoRegister;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();
    }

    private void initialize() {

        etName = (EditText) findViewById(R.id.etRegisterName);
        etSurname = (EditText) findViewById(R.id.etRegisterSurname);
        etBirthday = (EditText) findViewById(R.id.etRegisterBirthday);
        etUsername = (EditText) findViewById(R.id.etRegisterUsername);
        etPassword = (EditText) findViewById(R.id.etRegisterPassword);
        etEtPasswordAgain = (EditText) findViewById(R.id.etRegisterPasswordAgain);
        etPhoneNumber = (EditText) findViewById(R.id.etRegisterPhoneNumber);
        etAddress = (EditText) findViewById(R.id.etRegisterAddress);
        bDoRegister = (Button) findViewById(R.id.bDoRegister);
        bDoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPasswordFields()) {
                    if (!isNull(etName, etSurname, etBirthday, etUsername, etPhoneNumber, etAddress)) {
                        if (!isEmpty(etName.getText().toString(), etSurname.getText().toString(), etUsername.getText().toString())) {
                            new RegisterTask(RegisterActivity.this).execute(etName.getText().toString(),
                                    etSurname.getText().toString(),
                                    etBirthday.getText().toString(),
                                    etUsername.getText().toString(),
                                    etPassword.getText().toString(),
                                    etPhoneNumber.getText().toString(),
                                    etAddress.getText().toString());
                        } else {
                            createToast("Name, Surname, Username or Password fields can not be empty");
                        }
                    } else {
                        Log.e(TAG, "NullPointerException");
                    }
                } else {
                    createToast("Passwords does not match");
                }
            }
        });
    }

    private boolean checkPasswordFields() {

        if (isNull(etPassword, etEtPasswordAgain))
            return false;
        else if (isEmpty(etPassword.getText().toString(), etEtPasswordAgain.getText().toString()))
            return false;
        else if (isEqual(etPassword.getText().toString(), etEtPasswordAgain.getText().toString()))
            return true;
        else
            return false; //for unexpected situation
    }
}