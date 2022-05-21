package com.rentalmotor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.rentalmotor.HelperClasses.MainAdapter.DetailMotorHelperClass;
import com.rentalmotor.HelperClasses.MainAdapter.ExploreAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart extends Fragment {

    private RecyclerView cartRecycler;
    private ExploreAdapter adapter;
    ArrayList<DetailMotorHelperClass> cartMotor = new ArrayList<>();


    public interface VolleyOnCallbackRemoveCart{
        void onSuccess(String idMotor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cartRecycler = view.findViewById(R.id.cart_recycler);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            getCart(mainActivity.idUser);
            cartRecycler.setHasFixedSize(true);
            cartRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
            adapter = new ExploreAdapter(mainActivity.idUser,cartMotor,R.layout.cart_card_design);
        }
        return view;
    }

    private void getCart(String idUser){
        String url = "http://192.168.100.4:80/jualmotor/index.php?op=cartview&user_id="+idUser;
        RequestQueue mQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            JSONArray cart = obj.getJSONArray("cart");
                            ArrayList<String> id_motor = new ArrayList<String>();
                            for (int i = 0;i < cart.length();i++){
                                JSONObject motor = (JSONObject) cart.get(i);
                                id_motor.add(motor.getString("id"));
                                cartMotor.add(new DetailMotorHelperClass(motor.getString("id"),motor.getString("image"),motor.getString("namaMotor"),motor.getString("volume"),"Rp."+motor.getString("harga")));

                            }
                            cartRecycler.setAdapter(adapter);
                            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
                                @Override
                                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                                    return false;
                                }
                                @Override
                                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                                    removeCartItem(idUser, id_motor.get(viewHolder.getAdapterPosition()), new VolleyOnCallbackRemoveCart() {
                                        @Override
                                        public void onSuccess(String idMotor) {
                                            id_motor.remove(idMotor);
                                            cartMotor.remove(viewHolder.getAdapterPosition());
                                            Snackbar.make(requireView(),"Data removed from cart", Snackbar.LENGTH_SHORT).show();
                                            adapter.notifyDataSetChanged();
                                        }
                                    });

                                }
                            };
                            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(cartRecycler);
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
        mQueue.add(stringRequest);
    }

    private void removeCartItem(String idUser,String idMotor,VolleyOnCallbackRemoveCart callback){
        String url = "http://192.168.100.4:80/jualmotor/index.php?op=removecartitem";
        RequestQueue mQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            callback.onSuccess(idMotor);
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
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", idUser);
                params.put("id_motor", idMotor);
                return params;
            }
        };
        mQueue.add(stringRequest);
    }
}