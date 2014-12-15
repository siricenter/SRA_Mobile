package com.sra.helperClasses;

import com.sra.objects.Question;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imac on 12/15/14.
 */
public class FirebaseQuestionSet implements Serializable {
    ArrayList<FirebaseQuestion> Questions;
    String Name;

    public FirebaseQuestionSet(){
        this.Questions = new ArrayList<FirebaseQuestion>();
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }
}
