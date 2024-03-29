package com.example.ylifebsb;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.mhmtk.twowaygrid.TwoWayGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;




public class member extends Fragment {
    ArrayList<items> td;
    ListView listView;
    String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);
        listView = view.findViewById(R.id.listitems);
        id = getArguments().getString("id");
        listView = (ListView) view.findViewById(R.id.listitemsmembers);
        td = new ArrayList<items>();
        FillList();
        homelayout hl =(homelayout)getActivity();
        hl.i = 1;
        return view;
    }


    public void FillList() {
        StringRequest request = new StringRequest(Request.Method.POST,"https://srbn.herokuapp.com/member/downline", new Response.Listener<String>() {
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
                if (mylist.length()<=0 || mylist==null){
                    items item = new items();
                    item.name="";
                    item.joiningDate="";
                    item.email = "NO DATA PRESENT";
                    td.add(item);
                    listView.setClickable(false);
                }else
                for (int i = 0; i < mylist.length(); i++) {
                    try {
                        JSONObject jsonobject = null;
                        items it = new items();
                        jsonobject = (JSONObject) mylist.get(i);
                        String firstname = jsonobject.optString("firstname");
                        String lastname = jsonobject.optString("lastname");
                        it.name = firstname +" "+ lastname;
                        it.email = jsonobject.optString("email");
                        it.joiningDate = jsonobject.optString("createdAt");
                        td.add(it);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                member_adaptor adapt = new member_adaptor(getContext(),td);
                listView.setAdapter(adapt);

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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> para = new HashMap<String, String>();

                para.put("level",id);
                return para;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}