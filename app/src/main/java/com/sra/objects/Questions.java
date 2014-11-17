package com.sra.objects;

import com.sra.objects.Datapoint;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */
public class Questions implements Serializable {

    private String referenceUrl;
    private ArrayList <Datapoint> dataPoints;

    public void addDataPoint(Datapoint datapoint){
        dataPoints.add(datapoint);
    }

    public ArrayList<Datapoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(ArrayList<Datapoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public String getReferenceUrl() {
        return referenceUrl;
    }

    public void setReferenceUrl(String referenceUrl) {
        this.referenceUrl = referenceUrl;
    }

    public Questions(String url){
        this.dataPoints = new ArrayList<Datapoint>();
        this.referenceUrl = url;
    }


}
