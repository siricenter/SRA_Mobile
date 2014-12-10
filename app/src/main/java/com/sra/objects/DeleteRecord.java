package com.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imac on 12/8/14.
 */
public class DeleteRecord implements Serializable {
    private ArrayList <Areas> deletedAreas;
    private ArrayList <Households> deletedHouseholds;
    private ArrayList <Member> deletedMembers;
    private ArrayList <QuestionSet> removedQuestionSets;
    private ArrayList <Questions> removedQuestions;
    private ArrayList <Datapoint> removedDatapoints;


    public ArrayList<Areas> getDeletedAreas() {
        return deletedAreas;
    }

    public ArrayList<Datapoint> getRemovedDatapoints() {
        return removedDatapoints;
    }

    public ArrayList<Households> getDeletedHouseholds() {
        return deletedHouseholds;
    }

    public ArrayList<Questions> getRemovedQuestions() {
        return removedQuestions;
    }

    public ArrayList<QuestionSet> getRemovedQuestionSets() {
        return removedQuestionSets;
    }

    public ArrayList<Member> getDeletedMembers() {
        return deletedMembers;
    }

    public void addArea(Areas area){
        deletedAreas.add(area);
    }

    public void addhousehold(Households household){
        deletedHouseholds.add(household);
    }

    public void addMember(Member member){
        deletedMembers.add(member);
    }

    public void addQuestionSet(QuestionSet questionSet){
        removedQuestionSets.add(questionSet);
    }

    public void addQuestion(Questions questions){
        removedQuestions.add(questions);
    }

    public void addDatapoint(Datapoint datapoint){
        removedDatapoints.add(datapoint);
    }

    public DeleteRecord(){
        this.deletedMembers = new ArrayList<Member>();
        this.deletedAreas = new ArrayList<Areas>();
        this.deletedHouseholds = new ArrayList<Households>();
        this.removedDatapoints = new ArrayList<Datapoint>();
        this.removedQuestions = new ArrayList<Questions>();
        this.removedQuestionSets = new ArrayList<QuestionSet>();
    }
}
