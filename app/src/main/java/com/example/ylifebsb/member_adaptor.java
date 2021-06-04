package com.example.ylifebsb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class member_adaptor extends BaseAdapter {
    Context context;
    ArrayList<items> td;

    member_adaptor(Context context, ArrayList<items> td) {
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
                    inflate(R.layout.lst_items, parent, false);
        }

        TextView item1 = (TextView) convertView.findViewById(R.id.textView1);
        TextView item2 = (TextView) convertView.findViewById(R.id.textView2);
        TextView item3 = (TextView) convertView.findViewById(R.id.textView3);
        char[] c = td.get(position).name.toCharArray();
        char d = c[0];
        if (Character.isDigit(c[0])) {
            item1.setText("Level " + String.valueOf(td.get(position).name));
        } else {
            item1.setText(String.valueOf(td.get(position).name));
        }
        item2.setText(String.valueOf(td.get(position).email));
        item3.setText(String.valueOf(td.get(position).joiningDate));

        return convertView;

    }
}

