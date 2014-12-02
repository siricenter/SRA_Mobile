package com.example.chad.sraMobile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.sra.objects.QuestionSet;
import com.sra.objects.Questions;

import java.util.ArrayList;

public class EditQuestionSet extends Activity {

    private TableLayout questionTable;
    private EditText questionSetNameField;

    private QuestionSet questionSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question_set);

        Intent intent = getIntent();
        String questionSetName = (String) intent.getStringExtra("questionSetName");
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
        questionSetNameField.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                questionSet.setName(questionSetNameField.getText().toString());
            }
        });

        questionTable = (TableLayout) findViewById(R.id.question_table);
        questionTable.setStretchAllColumns(true);
        questionTable.setColumnShrinkable(0, true);
        questionTable.setColumnShrinkable(1, true);
        questionTable.setColumnShrinkable(2, true);

        Button addQuestionButton = (Button) findViewById(R.id.add_question_button);
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                goToQuestionEdit("", true);
            }
        });
        loadQuestions();
    }

    @Override
    protected void onDestroy() {
        save();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadQuestions();
    }

    public void save() {
        questionSet.setName(questionSetNameField.getText().toString());
        QuestionSet.saveQuestionSets();
    }

    public void loadQuestions() {
        questionTable.removeAllViews();

        ArrayList<Questions> questions = questionSet.getQuestions();
        for (final Questions qs : questions) {
            final TableRow row = new TableRow(getBaseContext());

            Button questionButton = new Button(getBaseContext());
            questionButton.setText(qs.getName());
            questionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToQuestionEdit(qs.getName(), false);
                }
            });

            Button deleteButton = new Button(getBaseContext());
            deleteButton.setText("Delete");
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    questionTable.removeView(row);
                    questionSet.deleteQuestion(qs);
                }
            });

            row.addView(questionButton);
            row.addView(deleteButton);

            questionTable.addView(row);
        }
    }

    public void goToQuestionEdit(String questionName, Boolean isNew) {
        Intent intent = new Intent(this, EditQuestion.class);
        intent.putExtra("questionSetName", questionSet.getName());
        intent.putExtra("questionName", questionName);
        intent.putExtra("isNewQuestion", isNew);
        startActivity(intent);
    }

    public void errorLabel() {
        Dialog alert = new Dialog(this);
        alert.setTitle("Question set name is required");
        alert.show();
    }
}