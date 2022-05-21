package com.rentalmotor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignIn extends AppCompatActivity {
    private TextView emailTF;
    private TextView passwordTF;
    private Button submitBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        emailTF = findViewById(R.id.email_tf_signin);
        passwordTF = findViewById(R.id.password_tf_signin);
        submitBT = findViewById(R.id.submit_bt_signin);

        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSignIn(emailTF.getText().toString(),passwordTF.getText().toString());
            }
        });
    }

    public void sign_up(View v){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void buttonSignIn(String email,String password){
        String url = "http://192.168.100.4:80/jualmotor/login.php";
        //Start ProgressBar first (Set visibility VISIBLE)
        if(!email.equals("") && !password.equals("")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[2];
                    field[0] = "email";
                    field[1] = "password";
                    //Creating array for data
                    String[] data = new String[2];
                    data[0] = email;
                    data[1] = password;
                    PutData putData = new PutData(url, "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            String[] a = result.split(",");
                            result = a[0];
                            if (result.equals("Login Success")) {
                                Toast.makeText(SignIn.this, result, Toast.LENGTH_SHORT).show();
                                Log.i("PutData", result);
                                Intent intent = new Intent(SignIn.this, MainActivity.class);
                                String idUser = a[1];
                                intent.putExtra("id_user",idUser);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignIn.this, result, Toast.LENGTH_SHORT).show();
                                Log.e("PutData", result);
                            }
                        }
                    }
                    //End Write and Read data with URL
                }
            });
        }
        else{
            Toast.makeText(SignIn.this,"All Fields are Required",Toast.LENGTH_SHORT).show();
        }
    }
}