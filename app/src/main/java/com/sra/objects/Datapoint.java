package com.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class Datapoint implements Serializable {
    private String label;
    private String dataType;
    private String answer;

    private String optionListType;
    private ArrayList<String> options;

    /*
     * Constructor
     */
    public Datapoint() {
        this.label = "";
        this.dataType = "";
        this.answer = "";

        optionListType = "List";
        options = new ArrayList<String>();
    }

    /*
     * Modifiers
     */
    public void setLabel(String label) {
        this.label = label;
        if (!label.equals("Option List")) options.clear();
    }
    public void setDataType(String dataType) { this.dataType = dataType; }
    public void setAnswer(String answer) { this.answer = answer; }
    public void setOptionsType(String type) { this.optionListType = type; }
    public void addOption(String option) { options.add(option); }
    public void deleteOption(String option) { options.remove(option); }

    /*
     * Accessors
     */
    public String getLabel() { return label; }
    public String getDataType() { return dataType; }
    public String getAnswer() { return answer; }
    public String getOptionListType() { return optionListType; }
    public ArrayList<String> getOptions() { return options; }
}
