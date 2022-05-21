package com.rentalmotor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Objects;


public class SignUp extends AppCompatActivity {
    private TextView emailTF;
    private TextView usernameTF;
    private TextView passwordTF;
    private Button submitBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        emailTF = findViewById(R.id.email_tf_signup);
        usernameTF = findViewById(R.id.username_tf_signup);
        passwordTF = findViewById(R.id.password_tf_signup);
        submitBT = findViewById(R.id.submit_bt_signup);

        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSignUp(emailTF.getText().toString(),usernameTF.getText().toString(),passwordTF.getText().toString());
            }
        });
    }

    public void buttonSignUp(String email,String username,String password){
        String url = "http://192.168.100.4:80/jualmotor/signup.php";
        //Start ProgressBar first (Set visibility VISIBLE)
        if(!email.equals("") && !username.equals("") && !password.equals("")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[3];
                    field[0] = "email";
                    field[1] = "username";
                    field[2] = "password";
                    //Creating array for data
                    String[] data = new String[3];
                    data[0] = email;
                    data[1] = username;
                    data[2] = password;
                    PutData putData = new PutData(url, "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            //End ProgressBar (Set visibility to GONE)
                            if (result.equals("Sign Up Success")) {
                                Toast.makeText(SignUp.this, result, Toast.LENGTH_SHORT).show();
                                emailTF.setText("");
                                usernameTF.setText("");
                                passwordTF.setText("");
                                Log.i("PutData", result);
                            } else {
                                Toast.makeText(SignUp.this, result, Toast.LENGTH_SHORT).show();
                                Log.e("PutData", result);
                            }
                        }
                    }
                    //End Write and Read data with URL
                }
            });
        }
        else{
            Toast.makeText(SignUp.this,"All Fields are Required",Toast.LENGTH_SHORT).show();
        }
    }
}