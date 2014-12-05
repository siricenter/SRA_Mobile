package com.sra.objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */
public class Questions implements Serializable {
    private String name;
    private String referenceUrl;
    private ArrayList <Datapoint> dataPoints;
    private Boolean isMultiUse;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMultiUse(Boolean use) {
        isMultiUse = use;
    }

    public Boolean getMultiUse() {
        return isMultiUse;
    }

    public void addDataPoint(Datapoint datapoint){
        for (Datapoint dp : dataPoints) {
            if (dp == datapoint) {
                return;
            }
        }
        dataPoints.add(datapoint);
    }

    public Datapoint getDataPoint(String label) {
        for (Datapoint dp : dataPoints) {
            if (dp.getLabel().equals(label)) {
                return dp;
            }
        }
        return null;
    }

    public void deleteDataPoint(Datapoint dp) { dataPoints.remove(dp); }
    public void deleteDataPoint(String label) {
        for (Datapoint dp : dataPoints) {
            if (dp.getLabel().equals(label)) {
                dataPoints.remove(dp);
                return;
            }
        }
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

    public Questions() {
        this.name = new String();
        this.dataPoints = new ArrayList<Datapoint>();
        this.isMultiUse = false;
    }

    public Questions(JSONObject q) {
        try {
            if (q.has("referenceUrl")) {
                referenceUrl = q.getString("referenceUrl");
            }
            if (q.has("dataPoints")) {
                JSONArray dps = q.getJSONArray("dataPoints");
                for (int i = 0; i < dps.length(); i++) {
                    addDataPoint(new Datapoint(dps.getJSONObject(i)));
                }
            }
        }
        catch (org.json.JSONException e) {

        }
    }
}
