package com.rentalmotor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    private TextView emailTF;
    private TextView usernameTF;
    private TextView passwordTF;
    private TextView alamatTF;
    private FloatingActionButton confirmEditBT;

    public interface VolleyOnCallBack{
        void onSuccess();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        emailTF = findViewById(R.id.email_edit_tf);
        usernameTF = findViewById(R.id.username_edit_tf);
        passwordTF = findViewById(R.id.password_edit_tf);
        alamatTF = findViewById(R.id.alamat_edit_tf);
        confirmEditBT = findViewById(R.id.confirm_edit_bt);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userview(extras.getString("id_user"));
            confirmEditBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    useredit(new VolleyOnCallBack(){
                        @Override
                        public void onSuccess() {
                            Toast.makeText(EditProfile.this, "Data berhasil dirubah", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    },extras.getString("id_user"),usernameTF.getText().toString(),alamatTF.getText().toString());
                }
            });
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void userview(String idUser){
        String url = "http://192.168.100.4:80/jualmotor/index.php?op=userview&id="+idUser;
        RequestQueue mQueue = Volley.newRequestQueue(EditProfile.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            JSONObject user = obj.getJSONObject("user");
                            Log.i("HASIL",obj.getString("message"));
                            usernameTF.setText(user.getString("username"));
                            emailTF.setText(user.getString("email"));
                            passwordTF.setText(user.getString("password"));
                            if (!user.getString("alamat").equals("null")){
                                alamatTF.setText(user.getString("alamat"));
                            }else{
                                alamatTF.setText("");
                            }
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

    public void useredit(VolleyOnCallBack callBack,String idUser,String username,String alamat){
        String url = "http://192.168.100.4:80/jualmotor/index.php?op=useredit";
        RequestQueue mQueue = Volley.newRequestQueue(EditProfile.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            Log.i("RESULT", obj.getString("message"));
                            callBack.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("GAGAL", error.getMessage());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idUser);
                params.put("username", username);
                params.put("alamat", alamat);
                return params;
            }
        };
        mQueue.add(stringRequest);
    }
}