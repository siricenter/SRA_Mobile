package com.example.chad.sra_mobile;

import com.firebase.client.DataSnapshot;

import java.io.Serializable;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class Datapoint implements Serializable {
    public String label;
    public String dataType;


    public Datapoint(String label,String dataType){
        this.label = label;
        this.dataType = dataType;
    }

}
