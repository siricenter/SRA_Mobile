package com.sra.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.quickconnectfamily.kvkit.kv.KVStore;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/10/14.
 */

public class QuestionSet implements Serializable {

    private String name;
    private String refUrl;
    private String type;
    private ArrayList<Question> questions;

    /*
     * Constructor
     */
    public QuestionSet(String name, String url) {
        this.name = name;
        this.refUrl = url;
        this.type = "Region";
        this.questions = new ArrayList<Question>();
    }

    /*
     * Modifiers
     */
    public void setName(String name) { this.name = name; }
    public void setRefUrl(String refUrl) { this.refUrl = refUrl; }
    public void setType(String type) { this.type = type; }
    public void addQuestion(Question qs) { questions.add(qs); }
    public void setQuestions(ArrayList<Question> questions) { this.questions = questions; }

    /*
     * Accessors
     */
    public String getName() { return name; }
    public String getRefUrl() { return refUrl; }
    public String getType() { return type; }
    public void deleteQuestion(Question qs) { questions.remove(qs); }
    public ArrayList<Question> getQuestions() { return questions; }
    public Question getQuestion(String questionName) {
        for (Question q : questions) {
            if (q.getName().equals(questionName)) {
                return q;
            }
        }
        return null;
    }

    /*
     * Static methods
     */
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
