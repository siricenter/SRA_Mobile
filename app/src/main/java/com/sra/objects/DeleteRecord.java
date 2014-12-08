package com.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imac on 12/8/14.
 */
public class DeleteRecord implements Serializable {
    private ArrayList <Areas> deletedAreas;
    private ArrayList <Households> deletedHouseholds;
    private ArrayList <String> deletedMembers;
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

    public ArrayList<String> getDeletedMembers() {
        return deletedMembers;
    }

    public void addArea(Areas area){
        deletedAreas.add(area);
    }

    public void addhousehold(Households household){
        deletedHouseholds.add(household);
    }

    public void addMember(String member){
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
}
