package com.sra.listViewSlider;

import android.graphics.drawable.Drawable;

public class ItemRow {

    String itemName;
    Drawable icon;

    public ItemRow(String itemName) {
        super();
        this.itemName = itemName;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

}
