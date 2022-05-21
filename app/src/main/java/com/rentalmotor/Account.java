package com.rentalmotor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account extends Fragment {
    private Button editBT;
    private Button keluarBT;
    private TextView usernameTF;
    private TextView emailTF;
    private String idUser;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;    private String mParam2;

    public Account() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account.
     */
    // TODO: Rename and change types and number of parameters
    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();
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

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        editBT = view.findViewById(R.id.edit_bt);
        keluarBT = view.findViewById(R.id.keluar_bt);
        this.usernameTF = view.findViewById(R.id.username_tf_account);
        this.emailTF = view.findViewById(R.id.email_tf_account);

        MainActivity mainActivity = (MainActivity) getActivity();

        if (mainActivity != null) {
            this.idUser = mainActivity.idUser;
            userview(this.idUser);
        }

        editBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),EditProfile.class);
                intent.putExtra("id_user",idUser);
                startActivity(intent);
            }
        });

        keluarBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Keluar")
                        .setMessage("Apakah anda yakin ingin keluar akun?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getContext(),SignIn.class);
                                startActivity(intent);
                                requireActivity().finish();
                            }
                        })
                        .setNegativeButton("Batalkan", null)
                        .setIcon(null)
                        .show();
            }
        });

        return view;
    }

    public void userview(String id){
        String url = "http://192.168.100.4:80/jualmotor/index.php?op=userview&id="+id;
        RequestQueue mQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;
                        try {
                            obj = new JSONObject(response);
                            JSONObject user = obj.getJSONObject("user");
                            //Log.i("HASIL",obj.getString("message"));
                            usernameTF.setText(user.getString("username"));
                            emailTF.setText(user.getString("email"));
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