package com.sra.sliderFragments;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chad.sra_mobile.R;

import org.quickconnectfamily.kvkit.kv.KVStore;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;
    int i;

    public CustomDrawerAdapter(Context context, int layoutResourceID,
                               List<DrawerItem> listItems,int size) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;
        this.i = size;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            drawerHolder.ItemName = (TextView) view
                    .findViewById(R.id.drawer_itemName);

            View dash = inflater.inflate(R.layout.activity_dash_board,parent,false);
            View content = dash.findViewById(Window.ID_ANDROID_CONTENT);

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            int height = size.y;

            System.out.println(height - i);
            RelativeLayout ld = (RelativeLayout)view.findViewById(R.id.sideButton);
                           ld.getLayoutParams().height = (int)(height - (i * 3.41)) / drawerItemList.size();



            drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();

        }

        DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

        drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(
                dItem.getImgResID()));
        drawerHolder.ItemName.setText(dItem.getItemName());

        return view;
    }


    private static class DrawerItemHolder {
        TextView ItemName;
        ImageView icon;
    }
}
