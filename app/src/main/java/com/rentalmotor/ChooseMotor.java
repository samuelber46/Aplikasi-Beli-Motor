package com.rentalmotor;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ChooseMotor extends AppCompatActivity {
    private Button checkoutBT;
    private ImageView detailsImage;
    private TextView namaMotorTF;
    private TextView volumeTF;
    private TextView stokTF;
    private TextView detailsTF;
    private TextView transmisiTF;
    private TextView priceTF;
    private String idMotor;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_motor);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        checkoutBT = findViewById(R.id.checkout_bt);
        detailsImage = findViewById(R.id.details_image);
        stokTF = findViewById(R.id.stok_tf);
        namaMotorTF = findViewById(R.id.nama_motor);
        volumeTF = findViewById(R.id.volume_tf);
        transmisiTF = findViewById(R.id.transmisi_tf);
        priceTF = findViewById(R.id.price_tf);
        detailsTF = findViewById(R.id.details_tf);

        checkoutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseMotor.this,Checkout.class);
                intent.putExtra("id_motor",idMotor);
                intent.putExtra("id_user",idUser);
                startActivity(intent);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.idUser = extras.getString("id_user");
            this.idMotor = extras.getString("id_motor");
            motorview(this.idMotor);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void motorview(String id){
        String url = "http://192.168.100.4:80/jualmotor/index.php?op=motorview&id="+id;
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            JSONObject motor = obj.getJSONObject("motor");
                            //Log.i("HASIL",obj.getString("motor"));
                            Glide.with(ChooseMotor.this).load(motor.getString("image")).into(detailsImage);
                            namaMotorTF.setText(motor.getString("namaMotor"));
                            stokTF.setText(motor.getString("stok"));
                            volumeTF.setText(motor.getString("volume"));
                            //int warna_motor = Color.parseColor(motor.getString("warna"));

                            transmisiTF.setText(motor.getString("jenis"));
                            detailsTF.setText(motor.getString("details"));
                            priceTF.setText("Rp."+motor.getString("harga"));
                            View view = findViewById(R.id.color_view);
                            view.getBackground().setColorFilter(Color.parseColor(motor.getString("warna")), PorterDuff.Mode.MULTIPLY);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HASIL", error.getMessage());
                    }
                }
        );
        mQueue.add(stringRequest);
    }
}