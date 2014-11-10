package com.example.chad.sra_mobile;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */
public class Questions implements Serializable {

    public String referenceUrl;
    public ArrayList <Datapoint> dataPoints;

    public Questions(String url){
        this.dataPoints = new ArrayList<Datapoint>();
        this.referenceUrl = url;
    }


}
