package com.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

import LocalDatabase.Interview;

/**
 * Created by jakobhartman on 11/15/14.
 */
public class Households implements Serializable {
    private String householdName;
    private long id;
    private ArrayList<Interviews> interviews;
    private ArrayList<String> members;

    public Households(){
        householdName = "";
        id = 0;
        interviews = new ArrayList<Interviews>();
        members = new ArrayList<String>();
    }

    public void addMember(String name){
        members.add(name);
    }

    public void addInterview(Interviews interview){
        interviews.add(interview);
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setInterviews(ArrayList<Interviews> interviews) {
        this.interviews = interviews;
    }

    public ArrayList<Interviews> getInterviews() {
        return interviews;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getHouseholdName() {
        return householdName;
    }
}

