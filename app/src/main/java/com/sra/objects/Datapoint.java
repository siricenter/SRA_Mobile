package com.sra.objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class Datapoint implements Serializable {
    private String label;
    private String dataType;
    private String answer;

    private ArrayList<String> options;
    private String optionListType;

    public Datapoint(){
        this.label = "";
        this.dataType = "";
        this.answer = "";

        options = new ArrayList<String>();
        optionListType = "List";
    }

    public Datapoint(JSONObject dp) {
        try {
            if (dp.has("label")) {
                label = dp.getString("label");
            }
            if (dp.has("dataType")) {
                dataType = dp.getString("dataType");
            }
            if (dp.has("answer")) {
                answer = dp.getString("answer");
            }
            if (dataType.equals("Option List")) {
                if (dp.has("options")) {
                    if (dp.has("optionListType")) {
                        optionListType = dp.getString("optionListType");
                    }
                    JSONArray opts = dp.getJSONArray("options");
                    for (int i = 0; i < opts.length(); i++) {
                        addOption(opts.getString(i));
                    }
                }
            }
        }
        catch (org.json.JSONException e) {

        }
    }

    public String getAnswer() {
        return answer;
    }

    public String getDataType() {
        return dataType;
    }

    public String getLabel() {
        return label;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getOptionListType() {
        return optionListType;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setLabel(String label) {
        this.label = label;
        if (!label.equals("Option List")) {
            options.clear();
        }
    }

    public void addOption(String option) {
        options.add(option);
    }

    public void deleteOption(String option) {
        options.remove(option);
    }

    public void setOptionsType(String type) {
        optionListType = type;
    }
}
