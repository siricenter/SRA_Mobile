package com.example.chad.sraMobile;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jakobhartman on 10/8/14.
 */
public class customList extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> txt1;
    private final ArrayList<String> txt2;

    public customList(Activity context,ArrayList<String> txt1, ArrayList<String> txt2) {
        super(context, R.layout.row, txt1);
        this.context = context;
        this.txt1 = txt1;
        this.txt2 = txt2;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.row, null, true);
        TextView txtTitle1 = (TextView) rowView.findViewById(R.id.txt1);
        TextView txtTitle2 = (TextView) rowView.findViewById(R.id.txt2);
        txtTitle1.setText(txt1.get(position));
        txtTitle2.setText(txt2.get(position));
        return rowView;
    }
}

