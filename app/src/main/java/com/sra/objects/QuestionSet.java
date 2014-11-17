package com.sra.objects;

import android.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class QuestionSet implements Serializable {

    private ArrayList<Questions> questions;
    private  String name;
    private String refUrl;

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestions(ArrayList<Questions> questions) {
        this.questions = questions;
    }

    public void setRefUrl(String refUrl) {
        this.refUrl = refUrl;
    }

    public ArrayList<Questions> getQuestions() {
        return questions;
    }

    public String getName() {
        return name;
    }

    public String getRefUrl() {
        return refUrl;
    }

    public QuestionSet(String name,String url){
        this.questions = new ArrayList<Questions>();
        this.name = name;
        this.refUrl = url;
    }

    public Fragment generateFragment(){
        Fragment f = new Fragment();


        return f;
    }

    public void createQuestion(){

    }

}
