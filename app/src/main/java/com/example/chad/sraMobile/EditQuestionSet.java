package com.example.chad.sraMobile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.sra.objects.Question;
import com.sra.objects.QuestionSet;
import com.sra.objects.QuestionSetBank;

import java.util.ArrayList;
import java.util.Arrays;

public class EditQuestionSet extends Activity {

    private TableLayout questionTable;
    private EditText questionSetNameField;

    private QuestionSet questionSet;
    private Boolean isNewQuestionSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question_set);

        Intent intent = getIntent();
        String questionSetName = (String) intent.getStringExtra("questionSetName");
        isNewQuestionSet = (Boolean) intent.getBooleanExtra("isNewQuestionSet", true);
        if (isNewQuestionSet) {
            questionSetName = "";
            questionSet = new QuestionSet(questionSetName, "");
        }
        else {
            questionSet = QuestionSetBank.getQuestionSet(questionSetName);
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
                if (questionSetNameField.getText().length() == 0) {
                    errorDialog();
                } else {
                    save();
                    goToQuestionEdit("", true);
                }
            }
        });

        final Spinner type = (Spinner) findViewById(R.id.question_set_type_spinner);
        String[] typesArray = getResources().getStringArray(R.array.question_set_categories_array);
        final ArrayList<String> typesArrayList = new ArrayList<String>(Arrays.asList(typesArray));
        ArrayAdapter<String> freqAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, typesArrayList);
        type.setAdapter(freqAdapter);
        type.setSelection(typesArrayList.indexOf(questionSet.getType()));
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                questionSet.setType(typesArrayList.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        loadQuestions();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadQuestions();
    }

    @Override
    public void onBackPressed() {
        if (questionSetNameField.getText().length() > 0) {
            save();
        }
        super.onBackPressed();
    }

    public void save() {
        questionSet.setName(questionSetNameField.getText().toString());
        if (isNewQuestionSet) {
            QuestionSetBank.addQuestionSet(questionSet);
            isNewQuestionSet = false;
        }
        QuestionSetBank.saveQuestionSets();
    }

    public void loadQuestions() {
        questionTable.removeAllViews();

        ArrayList<Question> questions = questionSet.getQuestions();
        for (final Question qs : questions) {
            final TableRow row = new TableRow(getBaseContext());

            Button questionButton = new Button(getBaseContext());
            questionButton.setTextColor(Color.BLACK);
            questionButton.setText(qs.getName());
            questionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToQuestionEdit(qs.getName(), false);
                }
            });

            Button deleteButton = new Button(getBaseContext());
            deleteButton.setTextColor(Color.BLACK);
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

    public void errorDialog() {
        Dialog alert = new Dialog(this);
        alert.setTitle("Question set name is required");
        alert.show();
    }
}