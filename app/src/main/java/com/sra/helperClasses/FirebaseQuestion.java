package com.sra.helperClasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imac on 12/15/14.
 */
public class FirebaseQuestion implements Serializable {
    ArrayList<FirebaseDatapoint> Datapoints;
    String Name;

    public FirebaseQuestion(){
        this.Datapoints = new ArrayList<FirebaseDatapoint>();
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }
}
