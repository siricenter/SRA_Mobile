package com.sra.helperClasses;

import java.util.ArrayList;

/**
 * Created by imac on 12/15/14.
 */
public class FirebaseDatapoint {
    ArrayList<String> Answers;
    String Type;
    String Label;

    public  FirebaseDatapoint(){
        this.Answers = new ArrayList<String>();
    }

    public void setAnswers(ArrayList<String> answers) {
        Answers = answers;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public void setType(String type) {
        Type = type;
    }

    public ArrayList<String> getAnswers() {
        return Answers;
    }

    public String getLabel() {
        return Label;
    }

    public String getType() {
        return Type;
    }
}
