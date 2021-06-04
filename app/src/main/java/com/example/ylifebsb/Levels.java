package com.example.ylifebsb;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.List;
import java.util.Map;


public class Levels extends Fragment {
    ListView listView;
    Bundle bundle;
    Fragment mem ;
    ArrayList<items> td;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_levels, container, false);
        listView = view.findViewById(R.id.listitems);
        td = new ArrayList<items>();
        bundle = new Bundle();
        mem = new member();
        FillList();
        return view;
    }



    public void FillList(){
        StringRequest request = new StringRequest(Request.Method.GET,"https://srbn.herokuapp.com/member/levels", new Response.Listener<String>() {
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
                if (mylist.length()<=0){
                    items item = new items();
                    item.name="NO DATA PRESENT";
                    item.joiningDate="NO DATA PRESENT";
                    item.email = "NO DATA PRESENT";
                    td.add(item);
                }else
                for (int i = 0; i < mylist.length(); i++) {
                    try {
                        JSONObject jsonobject = null;
                        items it = new items();
                        jsonobject = (JSONObject) mylist.get(i);
                        it.name = String.valueOf(jsonobject.optInt("level"));
                        it.email = String.valueOf(jsonobject.optInt("totalMembers"));
                        it.joiningDate = jsonobject.optString("createdAt");
                        it.id = jsonobject.optString("id");
                        td.add(it);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                member_adaptor adapt = new member_adaptor(getContext(),td);
                listView.setAdapter(adapt);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        items item;
                        item = (items) listView.getItemAtPosition(position);
                        String name = item.name;
                        mem.setArguments(bundle);
                        bundle.putString("id",item.id);
                        getFragmentManager().beginTransaction().replace(R.id.homeFrameLayout,mem).addToBackStack(null).commit();
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
    }


}