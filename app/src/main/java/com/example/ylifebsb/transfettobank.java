package com.example.ylifebsb;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class transfettobank extends Fragment {

    ListView listView ;
    ArrayList<transaction_Data> Atd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_transfettobank, container, false);
        EditText amount = (EditText) view.findViewById(R.id.amountEditTextTransfertobank);
        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        TextView alertbox = (TextView) view.findViewById(R.id.alertBoxTransfertoBank);
        Button  proceed = (Button) view.findViewById(R.id.proceedBtnTransfertobank);
        listView = (ListView) view.findViewById(R.id.listViewTransfertobank);
        Atd = new ArrayList<transaction_Data>();
        transactionlist();

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertbox.setText("");
                if (amount.getText().toString().equals("0") || amount.getText().toString().equals("")) {
                    alertbox.setText("Enter Amount");
                } else if(Float.parseFloat(amount.getText().toString())<500) {
                    alertbox.setText("Amount should be greater than 500 INR.");
                }else
                    {
                        view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                        StringRequest request = new StringRequest(Request.Method.POST, "https://srbn.herokuapp.com/wallet/payout-request", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                                JSONObject data = null;
                                String message = null;
                                try {
                                    data = new JSONObject(response);
                                    message = (String) data.get("message");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (message.equals("Entered amount is less than the wallet balance.")) {
                                    alertbox.setText(message);
                                } else {

                                    Toast.makeText(getActivity(), "Payment successful!", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getContext(),homelayout.class);
                                    startActivity(i);

                                }

                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                String responseBody = null;
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
                                } else {
                                    try {
                                        responseBody = new String(error.networkResponse.data, "utf-8");

                                        JSONObject data = new JSONObject(responseBody);
                                        String mes = (String) data.get("message");
                                        alertbox.setText(mes);
                                    } catch (UnsupportedEncodingException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            //This is for Headers
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                DBHelper db = new DBHelper(getContext());
                                Cursor c = db.getdata();
                                c.moveToNext();
                                String token = c.getString(c.getColumnIndex("token"));
                                headers.put("Authorization", "Bearer " + token);
                                return headers;
                            }

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> params = new Hashtable<String, String>();
                                params.put("amount", amount.getText().toString());
                                return params;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                        queue.add(request);

                    }
                }

        });
        return view;
    }

    public void transactionlist(){



        StringRequest request = new StringRequest(Request.Method.GET,"https://srbn.herokuapp.com/wallet/payout-request-old", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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
                    item.type="";
                    getView().findViewById(R.id.notransactionImageviewtransfertobank).setVisibility(View.VISIBLE);
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
                }else {
                    message = "failed";
                }
                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();

            }
        }) {
            //This is for Headers
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                DBHelper db = new DBHelper(getActivity());
                Cursor c = db.getdata();
                c.moveToNext();
                String token = c.getString(c.getColumnIndex("token"));
                headers.put("Authorization","Bearer "+ token);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

        return;
    }


}