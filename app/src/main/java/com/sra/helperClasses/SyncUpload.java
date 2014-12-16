package com.sra.helperClasses;

import android.app.Activity;

import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sra.objects.Areas;
import com.sra.objects.Datapoint;
import com.sra.objects.DeleteRecord;
import com.sra.objects.Households;
import com.sra.objects.Interviews;
import com.sra.objects.Member;
import com.sra.objects.Question;
import com.sra.objects.QuestionSet;
import com.sra.objects.Region;
import com.sra.objects.loginObject;

import org.json.JSONObject;
import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONUtilities;
import org.quickconnectfamily.kvkit.kv.KVStore;
import org.quickconnectfamily.kvkit.kv.SearchRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by jakobhartman on 12/10/14.
 */
public class SyncUpload {
    Activity activity;
    Region region;
    DeleteRecord deleteRecord;
    String organization;
    ArrayList<FirebaseArea> fareas;
    ArrayList<String> urls;

    public SyncUpload(Region region,DeleteRecord deleted,String org,Activity activity){
                this.region = region;
                this.deleteRecord = deleted;
                this.organization = org;
                this.urls = new ArrayList<String>();
                this.fareas = new ArrayList<FirebaseArea>();
                this.activity = activity;
    }

    public void startUpload(){
        Firebase.setAndroidContext(activity.getApplication());
        Firebase base;
        buildFirebase();
        int i = 0;
        for(FirebaseArea area : fareas) {
            String url = urls.get(i);
            url = url.replaceAll("%20", " ");
            base = new Firebase(url);
            base.child("Name").setValue(area.getName());
            if (area.Resources.size() > 0) {
                for (FirebaseHousehold household : area.Resources) {
                    Firebase houses = base.child("Resources").child(household.getName());
                    houses.child("Name").setValue(household.getName());
                    if (household.Interviews.size() > 0) {
                        for (FirebaseInterview interview : household.Interviews) {
                            Firebase inter = houses.child("Interviews").push();
                            inter.child("Date Created").setValue(interview.getDateCreated());
                            if (interview.QuestionSets.size() > 0) {
                                for (FirebaseQuestionSet questionSet : interview.QuestionSets) {
                                    Firebase qs = inter.child("Question Sets").push();
                                    qs.child("Name").setValue(questionSet.getName());
                                    if(questionSet.Questions.size() > 0) {
                                        for (FirebaseQuestion question : questionSet.Questions) {
                                            Firebase q = qs.child("Questions").push();
                                            q.child("Name").setValue(question.getName());
                                            if(question.Datapoints.size() > 0) {
                                                for (FirebaseDatapoint datapoint : question.Datapoints) {
                                                    Firebase data = q.child("Data Points").push();
                                                    data.child("Type").setValue(datapoint.getType());
                                                    data.child("Label").setValue(datapoint.getLabel());
                                                    for (String answer : datapoint.Answers) {
                                                        Firebase answers = data.child("Answers").push();
                                                        answers.child("Value").setValue(answer);
                                                    }
                                                }
                                            }else{
                                                Firebase data = q.child("Data Points").push();
                                                data.child("Type").setValue("String");
                                                data.child("Label").setValue("Label");
                                                Firebase answers = data.child("Answers").push();
                                                answers.child("Value").setValue("Answer");
                                            }
                                        }
                                    }else{
                                        Firebase q = qs.child("Questions").push();
                                        q.child("Name").setValue("QuestionName");
                                        Firebase data = q.child("Data Points").push();
                                        data.child("Type").setValue("String");
                                        data.child("Label").setValue("Label");
                                        Firebase answers = data.child("Answers").push();
                                        answers.child("Value").setValue("Answer");
                                    }
                                }
                            }else{
                                Firebase qs = inter.child("Question Sets").push();
                                qs.child("Name").setValue("QuestionSetName");
                                Firebase q = qs.child("Questions").push();
                                q.child("Name").setValue("QuestionName");
                                Firebase data = q.child("Data Points").push();
                                data.child("Type").setValue("String");
                                data.child("Label").setValue("Label");
                                Firebase answers = data.child("Answers").push();
                                answers.child("Value").setValue("Answer");
                            }
                        }
                        if(household.Members.size() > 0) {
                            for (FirebaseMember member : household.Members) {
                                houses.child("Members").child(member.getName()).child("Name").setValue(member.getName());
                            }
                        }else{
                            houses.child("Members").child("Member").child("Name").setValue("Name");
                        }
                    }else{
                        Firebase inter = houses.child("Interviews").push();
                        inter.child("Date Created").setValue("9/99/99");
                        Firebase qs = inter.child("Question Sets").push();
                        qs.child("Name").setValue("QuestionSetName");
                        Firebase q = qs.child("Questions").push();
                        q.child("Name").setValue("QuestionName");
                        Firebase data = q.child("Data Points").push();
                        data.child("Type").setValue("String");
                        data.child("Label").setValue("Label");
                        Firebase answers = data.child("Answers").push();
                        answers.child("Value").setValue("Answer");
                    }
                }
                i++;
            }else{
                Firebase houses = base.child("Resources").child("Household");
                houses.child("Name").setValue("Household");
                Firebase inter = houses.child("Interviews").push();
                inter.child("Date Created").setValue("9/99/99");
                Firebase qs = inter.child("Question Sets").push();
                qs.child("Name").setValue("QuestionSetName");
                Firebase q = qs.child("Questions").push();
                q.child("Name").setValue("QuestionName");
                Firebase data = q.child("Data Points").push();
                data.child("Type").setValue("String");
                data.child("Label").setValue("Label");
                Firebase answers = data.child("Answers").push();
                answers.child("Value").setValue("Answer");
            }
        }
        removeDeleted();
    }

    public void addNewAreaToUser(){
        try{
            String json = JSONUtilities.stringify(KVStore.getValue("User"));
            Gson gson = new GsonBuilder().create();
            loginObject login = gson.fromJson(json,loginObject.class);
            final String Node = login.getUsername().split("@")[0];
            Firebase.setAndroidContext(activity);
            Firebase base = new Firebase("https://intense-inferno-7741.firebaseio.com/Users/" + Node + "/Organizations/" + organization + "/Regions/");
            for(String areas : login.getAreaNames()){
                base.child("South Africa").child("Areas").child(areas).child("Name").setValue(areas);
            }
        }catch (JSONException e){}
    }

    public void removeDeleted(){
        Firebase base;

        for (Areas areas : deleteRecord.getDeletedAreas()){
            String url = areas.getRef();
            url = url.replaceAll("%20"," ");
            base = new Firebase(url);
            base.removeValue();
        }
        for (Households households : deleteRecord.getDeletedHouseholds()){
            String url = households.getRef();
            url = url.replaceAll("%20"," ");
            base = new Firebase(url);
            base.removeValue();
        }
        for (Member member : deleteRecord.getDeletedMembers()) {
            String url = member.getRef();
                  url =  url.replaceAll("%20"," ");
            base = new Firebase(url);
            base.removeValue();
        }
        deleteRecord = new DeleteRecord();
        KVStore.removeValue("Delete");
    }


    public void buildFirebase(){
        for(Areas area : region.getAreas()){
            urls.add(area.getRef());
            FirebaseArea farea = new FirebaseArea();
            farea.setName(area.getAreaName());
            for(Households households : area.getHouseholds()){
                FirebaseHousehold fhousehold = new FirebaseHousehold();
                fhousehold.setName(households.getHouseholdName());
                for(String members : households.getMembers()){
                    FirebaseMember fmember = new FirebaseMember();
                    fmember.setName(members);
                    fhousehold.Members.add(fmember);
                }
                for(Interviews interviews : households.getInterviews()){
                    FirebaseInterview fInterview = new FirebaseInterview();
                    fInterview.setDateCreated(interviews.getCreatedDate());
                    for(QuestionSet questionSet : interviews.getQuestionSets()){
                        FirebaseQuestionSet fQuestionSet = new FirebaseQuestionSet();
                        fQuestionSet.setName(questionSet.getName());
                        for(Question question : questionSet.getQuestions()){
                            FirebaseQuestion fQuestion = new FirebaseQuestion();
                            fQuestion.setName(question.getName());
                            for(Datapoint datapoint : question.getDataPoints()){
                                FirebaseDatapoint fDatapoint = new FirebaseDatapoint();
                                fDatapoint.setLabel(datapoint.getLabel());
                                fDatapoint.setType(datapoint.getDataType());
                                for(String answer : datapoint.getAnswers()){
                                   fDatapoint.Answers.add(answer);
                                }
                                fQuestion.Datapoints.add(fDatapoint);
                            }
                            fQuestionSet.Questions.add(fQuestion);
                        }
                        fInterview.QuestionSets.add(fQuestionSet);
                    }
                    fhousehold.Interviews.add(fInterview);
                }
                farea.Resources.add(fhousehold);
            }
            fareas.add(farea);
        }
    }

    public Region getRegion() {
        return region;
    }

    public static HashMap jsonToMap(String t) throws JSONException {

        HashMap<String, String> map = new HashMap<String, String>();
        try {
            JSONObject jObject = new JSONObject(t);
            Iterator<?> keys = jObject.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = jObject.getString(key);
                map.put(key, value);

            }

        }catch (org.json.JSONException e){

        }
        return map;
    }
}
