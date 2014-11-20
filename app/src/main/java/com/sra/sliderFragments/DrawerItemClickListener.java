package com.sra.sliderFragments;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DrawerItemClickListener implements
        ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        SelectItem(position);

    }

    private void SelectItem(int position) {
    }
}
