package com.rentalmotor.HelperClasses.SeeAllFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.rentalmotor.HelperClasses.MainAdapter.DetailMotorHelperClass;
import com.rentalmotor.HelperClasses.MainAdapter.ExploreAdapter;
import com.rentalmotor.MainActivity;
import com.rentalmotor.R;
import com.rentalmotor.SeeAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllFragment extends Fragment {
    private RecyclerView allRecycler;
    private ExploreAdapter adapter;
    private String idUser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllFragment newInstance(String param1, String param2) {
        AllFragment fragment = new AllFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_all, container, false);
        allRecycler = view.findViewById(R.id.all_recycler);
        SeeAll seeAll = (SeeAll) getActivity();
        if (seeAll != null) {
            featuredRecycler(seeAll.idUser);
        }
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
                            allRecycler.setHasFixedSize(true);
                            allRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
                            ArrayList<DetailMotorHelperClass> allMotor = new ArrayList<>();
                            adapter = new ExploreAdapter(idUser,allMotor,R.layout.seeall_card_design);
                            for (int i = 0;i < motor.length();i++){
                                JSONObject detailmotor = (JSONObject) motor.get(i);
                                allMotor.add(new DetailMotorHelperClass(detailmotor.getString("id"),detailmotor.getString("image"),detailmotor.getString("namaMotor"),detailmotor.getString("volume"),"Rp."+detailmotor.getString("harga")));
                            }
                            allRecycler.setAdapter(adapter);
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