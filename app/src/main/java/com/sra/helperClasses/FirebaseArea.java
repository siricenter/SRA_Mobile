package com.sra.helperClasses;

import com.sra.objects.Households;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imac on 12/15/14.
 */
public class FirebaseArea implements Serializable {
    String Name;
    ArrayList<FirebaseHousehold> Resources;

    public FirebaseArea(){
        this.Resources = new ArrayList<FirebaseHousehold>();
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }
}
