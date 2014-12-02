package com.example.chad.sraMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

public class CreateQuestionSet extends Activity {

    private TableLayout questionTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_set);

        Intent intent = getIntent();
        String questionSet = (String) intent.getStringExtra("questionSet");
        System.out.println("Question set: " + questionSet);

        questionTable = (TableLayout) findViewById(R.id.question_table);
        questionTable.setStretchAllColumns(true);
        questionTable.setColumnShrinkable(0, true);
        questionTable.setColumnShrinkable(1, true);
        questionTable.setColumnShrinkable(2, true);

        Button addQuestionButton = (Button) findViewById(R.id.add_question_button);
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestion();
            }
        });
    }

    public void addQuestion() {
        Intent intent = new Intent(this, CreateOrEditQuestion.class);
        intent.putExtra("question", "I am the question");
        startActivity(intent);
    }
}