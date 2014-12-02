package com.sra.objects;

import android.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.quickconnectfamily.kvkit.kv.KVStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class QuestionSet implements Serializable {

    private ArrayList<Questions> questions;
    private String name;
    private String refUrl;

    public void addQuestion(Questions qs){
          questions.add(qs);
    }
    public void deleteQuestion(Questions qs) {
        questions.remove(qs);
    }

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


    public QuestionSet(JSONObject set) {
        HashMap<String, Object> poop;
        try {
            if (set.has("name")) {
                name = set.getString("name");
            }
            if (set.has("refUrl")) {
                refUrl = set.getString("refUrl");
            }
            if (set.has("questions")) {
                JSONArray q = set.getJSONArray("questions");
                for (int i = 0; i < q.length(); i++) {
                    addQuestion(new Questions(q.getJSONObject(i)));
                }
            }
        }
        catch (org.json.JSONException e) {

        }
    }

    public Questions getQuestion(String questionName) {
        for (Questions q : questions) {
            if (q.getName().equals(questionName)) {
                return q;
            }
        }
        return null;
    }

    private static ArrayList<QuestionSet> questionSets = null;
    public static ArrayList<QuestionSet> getQuestionSets() {
        if (questionSets == null) { loadQuestionSets(); }
        return questionSets;
    }
    private static void loadQuestionSets() {
        questionSets = new ArrayList<QuestionSet>();
        JSONArray sets = null;
        String setsString = null;
        try {
            setsString = (String) KVStore.getValue("QuestionSetBank");
            System.out.println("Loaded JSON: " + setsString);
            sets = new JSONArray(setsString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sets == null) {
            saveQuestionSets();
            return;
        }

        for (int i = 0; i < sets.length(); i++) {
            try {
                String json = "";
                try {
                    json = sets.getString(i);
                } catch (Exception e) { }
                Gson gson = new GsonBuilder().create();
                QuestionSet set = gson.fromJson(json, QuestionSet.class);
                questionSets.add(set);
            } catch (NullPointerException e){
                System.out.println("Nothing Here");
            }
        }
    }

    public static void saveQuestionSets() {
        if (questionSets == null) {
            loadQuestionSets();
            return;
        }
        try {
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(questionSets);
            KVStore.storeValue("QuestionSetBank", json);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: Couldn't store QuestionSet bank using KVStore.");
        }
    }

    public static QuestionSet getQuestionSet(String name) {
        if (questionSets == null) { loadQuestionSets(); }
        for (QuestionSet set : questionSets) {
            if (set.getName().equals(name)) {
                return set;
            }
        }
        return null;
    }

    public static void deleteQuestionSet(QuestionSet qs) {
        if (questionSets == null) { loadQuestionSets(); }
        questionSets.remove(qs);
        saveQuestionSets();
    }

    public static void addQuestionSet(QuestionSet qs) {
        if (questionSets == null) { loadQuestionSets(); }
        questionSets.add(qs);
        saveQuestionSets();
    }
}
