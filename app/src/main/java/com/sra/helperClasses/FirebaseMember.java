package com.sra.helperClasses;

import java.io.Serializable;

/**
 * Created by imac on 12/15/14.
 */
public class FirebaseMember implements Serializable {
    String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
