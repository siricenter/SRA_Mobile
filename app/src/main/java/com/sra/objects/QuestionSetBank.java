package com.sra.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.quickconnectfamily.kvkit.kv.KVStore;

import java.util.ArrayList;

/**
 * Created by jordanreed on 12/9/14.
 */
public class QuestionSetBank {

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
