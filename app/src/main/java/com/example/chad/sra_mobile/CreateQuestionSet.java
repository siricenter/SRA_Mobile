package com.example.chad.sra_mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class CreateQuestionSet extends Activity {

    private TableLayout questionTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        loadQuestionsFromBaseToLocal();
        setContentView(R.layout.activity_create_question_set);

        // Set food table attributes
        questionTable = (TableLayout) findViewById(R.id.question_table);
        questionTable.setStretchAllColumns(true);
        questionTable.setColumnShrinkable(0, true);
        questionTable.setColumnShrinkable(1, true);
        questionTable.setColumnShrinkable(2, true);

        // Setup the addFoodButton
        Button addQuestionButton = (Button) findViewById(R.id.add_question_button);
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestion();
            }
        });
    }

    public void generateRowsFromBase(){

    }

    public void loadQuestionsFromBaseToLocal(){
        Firebase ref = new Firebase("https://intense-inferno-7741.firebaseio.com/Question Sets");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    TableRow row = new TableRow(getBaseContext());
//
                    Button question = new Button(getBaseContext());
                    question.setText(data.getName());

                    Button delete = new Button(getBaseContext());
                    delete.setText("Delete");

                    row.addView(question);
                    row.addView(delete);

                    int index = questionTable.getChildCount() - 1;
                    questionTable.addView(row, index);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



    public void addQuestion() {

        Intent intent = new Intent(this, CreateOrEditQuestion.class);
        startActivity(intent);

//        TableRow row = new TableRow(this);
//
//        Button question = new Button(this);
//        question.setText("Question1");
//
//        Button delete = new Button(this);
//        delete.setText("Delete");
//
//        row.addView(question);
//        row.addView(delete);
//
//        int index = questionTable.getChildCount() - 1;
//        questionTable.addView(row, index);
    }


}