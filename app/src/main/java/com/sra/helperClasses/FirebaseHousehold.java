package com.sra.helperClasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imac on 12/15/14.
 */
public class FirebaseHousehold implements Serializable {
    String Name;
    ArrayList<FirebaseInterview> Interviews;
    ArrayList<FirebaseMember> Members;

    public FirebaseHousehold(){
        this.Interviews = new ArrayList<FirebaseInterview>();
        this.Members = new ArrayList<FirebaseMember>();
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
