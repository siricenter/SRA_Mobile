package com.example.chad.sra_mobile;

import android.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class QuestionSet implements Serializable {

    public ArrayList<Questions> questions;
    public  String name;
    public String refUrl;


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
