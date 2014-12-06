package com.sra.listViewSlider;


import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chad.sraMobile.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sra.objects.Region;
import com.sra.sliderFragments.FragmentOne;

import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONUtilities;
import org.quickconnectfamily.kvkit.kv.KVStore;

public class ItemAdapter extends ArrayAdapter {

    List   data;
    Context context;
    int layoutResID;
    FragmentOne fragmentOne;

    public ItemAdapter(Context context, int layoutResourceId,List data,FragmentOne fragmentOne) {
        super(context, layoutResourceId, data);

        this.data=data;
        this.context=context;
        this.layoutResID=layoutResourceId;
        this.fragmentOne = fragmentOne;

        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        NewsHolder holder = null;
        View row = convertView;
        holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResID, parent, false);

            holder = new NewsHolder();

            holder.itemName = (TextView)row.findViewById(R.id.example_itemname);
            holder.icon=(ImageView)row.findViewById(R.id.example_image);
            holder.button2=(Button)row.findViewById(R.id.swipe_button2);
            holder.button3=(Button)row.findViewById(R.id.swipe_button3);
            row.setTag(holder);
        }
        else
        {
            holder = (NewsHolder)row.getTag();
        }

        ItemRow itemdata = (ItemRow)data.get(position);
        holder.itemName.setText(itemdata.getItemName());
        holder.icon.setImageDrawable(itemdata.getIcon());


        holder.button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                fragmentOne.editRow(position);
            }
        });

        holder.button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                fragmentOne.deleteRow(position);
            }
        });

        return row;

    }

    static class NewsHolder{

        TextView itemName;
        ImageView icon;
        Button button2;
        Button button3;
    }

}
