package com.rentalmotor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.rentalmotor.HelperClasses.MainAdapter.DetailMotorHelperClass;
import com.rentalmotor.HelperClasses.MainAdapter.ExploreAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Checkout extends AppCompatActivity {
    private Button checkoutBT;
    private Button addToCartBT;
    private String idUser;
    private String idMotor;
    private ImageView imageMotor;
    private TextView alamatTF;
    private TextView namaMotorTF;
    private TextView hargaMotorTF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        checkoutBT = findViewById(R.id.pay_bt);
        imageMotor = findViewById(R.id.img_motor_checkout);
        hargaMotorTF = findViewById(R.id.harga_checkout_tf);
        namaMotorTF = findViewById(R.id.nama_motor_checkout_tf);
        alamatTF = findViewById(R.id.alamat_checkout_tf);
        addToCartBT = findViewById(R.id.add_to_chart_bt);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.idUser = extras.getString("id_user");
            this.idMotor = extras.getString("id_motor");
            checkoutUser(idUser);
            checkoutMotor(idMotor);
            checkoutBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(Checkout.this)
                            .setTitle("Konfirmasi")
                            .setMessage("Apakah anda yakin ingin melanjutkan?")
                            .setPositiveButton("Konfirmasi", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Checkout.this, CheckoutSuccess.class);
                                    intent.putExtra("id_user",idUser);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Batalkan", null)
                            .setIcon(null)
                            .show();
                }
            });

            addToCartBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToCart(idUser,idMotor);
                }
            });
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    private void checkoutUser(String idUser){
        String urlUser = "http://192.168.100.4:80/jualmotor/index.php?op=userview&id="+idUser;
        RequestQueue mQueue = Volley.newRequestQueue(Checkout.this);
        StringRequest stringRequestUser = new StringRequest(Request.Method.GET, urlUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            JSONObject user = obj.getJSONObject("user");
                            alamatTF.setText(user.getString("alamat"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.getMessage());
                    }
                }
        );
        mQueue.add(stringRequestUser);
    }
    private void checkoutMotor(String idMotor){
        String urlMotor = "http://192.168.100.4:80/jualmotor/index.php?op=motorview&id="+idMotor;
        RequestQueue mQueue = Volley.newRequestQueue(Checkout.this);
        StringRequest stringRequestMotor = new StringRequest(Request.Method.GET, urlMotor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            JSONObject motor = obj.getJSONObject("motor");
                            Glide.with(Checkout.this).load(motor.getString("image")).into(imageMotor);
                            namaMotorTF.setText(motor.getString("namaMotor"));
                            hargaMotorTF.setText(motor.getString("harga"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.getMessage());
                    }
                }
        );
        mQueue.add(stringRequestMotor);
    }

    private void addToCart(String idUser,String idMotor){
        String urlMotor = "http://192.168.100.4:80/jualmotor/index.php?op=addtocart";
        RequestQueue mQueue = Volley.newRequestQueue(Checkout.this);
        StringRequest stringRequestMotor = new StringRequest(Request.Method.POST, urlMotor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            Toast.makeText(Checkout.this,obj.getString("message"),Toast.LENGTH_LONG).show();
                            Log.i("TAG",obj.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.getMessage());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", idUser);
                params.put("id_motor", idMotor);
                return params;
            }
        };
        mQueue.add(stringRequestMotor);
    }

}