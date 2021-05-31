package com.example.ylifebsb;

import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.mhmtk.twowaygrid.TwoWayGridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;




public class member extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member, container, false);
        TwoWayGridView grid = (TwoWayGridView) view.findViewById(R.id.gridview);

        String[] data = new String[]{"Dinesh Suthar","asdev82@gmail.com","0987654321","09/10/1999"};

        grid.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,data));



        return view;
    }
}