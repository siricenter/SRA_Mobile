package com.example.chad.sra_mobile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import com.sra.objects.QuestionSet;

public class CreateQuestionSet extends Activity {

    private String questionSetName;
    private TableLayout questionTable;
    private EditText questionSetNameField;

    private QuestionSet questionSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_set);

        Intent intent = getIntent();
        questionSetName = (String) intent.getStringExtra("questionSetName");
        Boolean isNewQuestionSet = (Boolean) intent.getBooleanExtra("isNewQuestionSet", true);
        if (isNewQuestionSet) {
            questionSetName = "";
            questionSet = new QuestionSet(questionSetName, "");
            QuestionSet.addQuestionSet(questionSet);
            QuestionSet.saveQuestionSets();
        }
        else {
            questionSet = QuestionSet.getQuestionSet(questionSetName);
        }
        questionSetNameField = (EditText) findViewById(R.id.question_set_name_field);
        questionSetNameField.setText(questionSet.getName());

        questionTable = (TableLayout) findViewById(R.id.question_table);
        questionTable.setStretchAllColumns(true);
        questionTable.setColumnShrinkable(0, true);
        questionTable.setColumnShrinkable(1, true);
        questionTable.setColumnShrinkable(2, true);

        Button addQuestionButton = (Button) findViewById(R.id.add_question_button);
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionSet.setName(questionSetNameField.getText().toString());
                QuestionSet.saveQuestionSets();
                addQuestion();
            }
        });

        Button saveQuestionSetButton = (Button) findViewById(R.id.save_question_set_button);
        saveQuestionSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionSetNameField.getText().toString().equals("")) {
                    errorLabel();
                }
                else {
                    QuestionSet.saveQuestionSets();
                }
            }
        });
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        questionSet.setName(questionSetNameField.getText().toString());
//    }

    public void addQuestion() {
        Intent intent = new Intent(this, CreateOrEditQuestion.class);
        intent.putExtra("questionName", "I am the question");
        intent.putExtra("questionSetName", questionSetName);
        startActivity(intent);
    }

    public void errorLabel() {
        Dialog alert = new Dialog(this);
        alert.setTitle("Please give a label");
        alert.show();
    }
}