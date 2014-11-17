package com.sra.objects;

import com.firebase.client.DataSnapshot;

import java.io.Serializable;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class Datapoint implements Serializable {
    private String label;
    private String dataType;
    private String answer;


    public Datapoint(){
        this.label = "";
        this.dataType = "";
        this.answer = "";
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

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
