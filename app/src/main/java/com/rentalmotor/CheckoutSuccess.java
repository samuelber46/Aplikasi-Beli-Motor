package com.rentalmotor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class CheckoutSuccess extends AppCompatActivity {
    private Button berandaBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_success);
        getWindow().setStatusBarColor(ContextCompat.getColor(CheckoutSuccess.this,R.color.green_500));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        this.berandaBT = findViewById(R.id.beranda_bt);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            berandaBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CheckoutSuccess.this, MainActivity.class);
                    intent.putExtra("id_user",extras.getString("id_user"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }
    }
}