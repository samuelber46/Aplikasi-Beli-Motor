package com.rentalmotor;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rentalmotor.HelperClasses.MainAdapter.ExploreAdapter;
import com.rentalmotor.HelperClasses.MainAdapter.DetailMotorHelperClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Explore extends Fragment {

    private RecyclerView exploreRecycler;
    private ExploreAdapter adapter;
    private TextView seeAll;
    private String idUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_explore, container, false);
        exploreRecycler = view.findViewById(R.id.explore_recycler);
        seeAll = view.findViewById(R.id.see_all);
        Bundle extras = getArguments();
        if (extras != null) {
            this.idUser = extras.getString("id_user");
            featuredRecycler(idUser);
        }
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SeeAll.class);
                intent.putExtra("id_user",idUser);
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }

    private void featuredRecycler(String idUser){
        String url = "http://192.168.100.4:80/jualmotor/index.php?op=motorview";
        RequestQueue mQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            JSONArray motor = obj.getJSONArray("motor");
                            //Log.i("HASIL",obj.getString("motor"));
                            exploreRecycler.setHasFixedSize(true);
                            exploreRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                            ArrayList<DetailMotorHelperClass> exploreMotor = new ArrayList<>();
                            adapter = new ExploreAdapter(idUser,exploreMotor,R.layout.explore_card_design);
                            for (int i = 0;i < motor.length();i++){
                                JSONObject detailmotor = (JSONObject) motor.get(i);
                                exploreMotor.add(new DetailMotorHelperClass(detailmotor.getString("id"),detailmotor.getString("image"),detailmotor.getString("namaMotor"),detailmotor.getString("volume"),"Rp."+detailmotor.getString("harga")));
                            }
                            exploreRecycler.setAdapter(adapter);
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