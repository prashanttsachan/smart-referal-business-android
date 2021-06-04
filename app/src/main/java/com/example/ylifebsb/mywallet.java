package com.example.ylifebsb;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class mywallet extends Fragment {

    ListView listView ;
    ArrayList<transaction_Data> Atd;
    DBHelper db ;
    Cursor c ;
    String token ;
    TextView walletbalance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_mywallet, container, false);
        db = new DBHelper(getContext());
        c = db.getdata();
        c.moveToNext();
        token = c.getString(c.getColumnIndex("token"));
        walletbalance = (TextView) view.findViewById(R.id.walletBalanceWalletTextView);
        listView = (ListView) view.findViewById(R.id.listViewMyWallet);
        Atd = new ArrayList<transaction_Data>();
        mywalletbalance();
        transactionlist();
        return view;
    }



    public void transactionlist(){
        StringRequest request = new StringRequest(Request.Method.GET,"https://srbn.herokuapp.com/wallet/transactions", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = new JSONObject();
                //Data is shown in the profile throw api
                try {
                    jsonObject= new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray mylist = null;
                try {
                    mylist = jsonObject.getJSONArray("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(mylist==null || mylist.length()<=0){
                    transaction_Data item = new transaction_Data();
                    listView.setVisibility(View.GONE);
                    ImageView img = (ImageView) getView().findViewById(R.id.notransactionImageview);
                    img.setVisibility(View.VISIBLE);
                    item.type="";
                    Atd.add(item);
                }else
                for (int i = 0; i < mylist.length(); i++) {
                    try {
                        JSONObject jsonobject = null;

                        jsonobject = (JSONObject) mylist.get(i);

                        transaction_Data td = new transaction_Data();
                        td.amount = jsonobject.optString("amount");
                        td.balance = jsonobject.optString("balance");
                        td.createdAt = jsonobject.optString("createdAt");
                        td.updatedAt = jsonobject.optString("updatedAt");
                        td.status = jsonobject.optString("status");
                        td.id = jsonobject.optString("id");
                        td.member = jsonobject.optString("member");
                        td.remarks = jsonobject.optString("remarks");
                        td.type = jsonobject.optString("type");
                        Atd.add(td);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                transaction_adaptor adapt = new transaction_adaptor(getContext(),Atd);
                listView.setAdapter(adapt);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent startint = new Intent(getActivity(),detail_transaction.class);
                        transaction_Data td;
                        td = (transaction_Data) listView.getItemAtPosition(position);
                        startint.putExtra("transaction_data", (Serializable)td);
                        startActivity(startint);
                    }
                });

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }else {
                    message = "failed";
                }
                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();

            }
        }) {
            //This is for Headers
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization","Bearer "+ token);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

        return;
    }

    public void mywalletbalance(){
        StringRequest request = new StringRequest(Request.Method.GET,"https://srbn.herokuapp.com/auth/checkauth", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                //Data is shown in the profile throw api
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = new JSONObject();
                    data = jsonObject.getJSONObject("data");
                    JSONObject user = new JSONObject();
                    user = data.getJSONObject("user");
                    walletbalance.setText(user.getString("wallet")+" INR");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                else {
                    message = "Failed!";
                }
                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
            }
        }) {
            //This is for Headers
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization","Bearer "+ token);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }


}