package com.sra.helperClasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imac on 12/15/14.
 */
public class FirebaseInterview implements Serializable {
    ArrayList<FirebaseQuestionSet> QuestionSets;
    String DateCreated;

    public FirebaseInterview(){
        this.QuestionSets = new ArrayList<FirebaseQuestionSet>();
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getDateCreated() {
        return DateCreated;
    }
}
