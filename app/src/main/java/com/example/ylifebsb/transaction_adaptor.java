package com.example.ylifebsb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class transaction_adaptor extends BaseAdapter {
    Context context;


    ArrayList<transaction_Data> td=null;

    transaction_adaptor(Context context, ArrayList<transaction_Data> td ){
        this.context = context;
        this.td = td;


    }
    @Override
    public int getCount() {
        return td.size();
    }

    @Override
    public Object getItem(int position) {
        return td.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.transaction_list_items, parent, false);
        }
        TextView date = (TextView) convertView.findViewById(R.id.dateTransactionlist);
        TextView memberid = (TextView) convertView.findViewById(R.id.memberidTransactionlist);
        ImageView statusimg = (ImageView) convertView.findViewById(R.id.statusImageView);
        TextView amount = (TextView) convertView.findViewById(R.id.AmountTransactionList);

        if(String.valueOf(td.get(position).status).equals("SUCCESS")){
            statusimg.setBackgroundResource(R.drawable.right);
        }
        else if(String.valueOf(td.get(position).status).equals("DECLINED")) {
            statusimg.setBackgroundResource(R.drawable.wrong);
        }
        else {
            statusimg.setBackgroundResource(R.drawable.exclamation);
        }
        date.setText(String.valueOf(td.get(position).createdAt));
        memberid.setText("Member : "+String.valueOf(td.get(position).member));
        if(String.valueOf(td.get(position).type).equals("CREDIT")){
            amount.setText("+ "+String.valueOf(td.get(position).amount)+" INR");
        }
        else {
            amount.setText("- "+String.valueOf(td.get(position).amount)+" INR");
        }


        return convertView;
    }
}
