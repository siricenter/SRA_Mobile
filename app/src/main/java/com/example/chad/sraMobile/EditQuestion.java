package com.example.chad.sraMobile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sra.objects.Datapoint;
import com.sra.objects.Question;
import com.sra.objects.QuestionSet;
import com.sra.objects.QuestionSetBank;

import java.util.ArrayList;
import java.util.Arrays;

public class EditQuestion extends Activity {

    private TableLayout dataPointTable;
    private Dialog alert;
    private EditText questionNameField;
    private CheckBox multiUseCheckBox;

    private QuestionSet questionSet;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        dataPointTable = (TableLayout) findViewById(R.id.data_point_table);
        dataPointTable.setStretchAllColumns(true);
        questionNameField = (EditText) findViewById(R.id.question_name_field);
        multiUseCheckBox = (CheckBox) findViewById(R.id.question_multi_use_checkbox);

        Intent intent = getIntent();
        String questionSetName = (String) intent.getStringExtra("questionSetName");
        String questionName = (String) intent.getStringExtra("questionName");
        Boolean isNewQuestion = intent.getBooleanExtra("isNewQuestion", false);
        if (isNewQuestion) {
            questionSet = QuestionSetBank.getQuestionSet(questionSetName);
            questionName = "";
            question = new Question("");
            question.setName(questionName);
            questionSet.addQuestion(question);
        }
        else {
            questionSet = QuestionSetBank.getQuestionSet(questionSetName);
            question = questionSet.getQuestion(questionName);
            questionNameField.setText(questionName);
            if (question.getMultiUse()) {
                multiUseCheckBox.setChecked(true);
            }
            else {
                multiUseCheckBox.setChecked(false);
            }
            ArrayList<Datapoint> dps = question.getDataPoints();
            for (Datapoint dp : dps) {
                updateDataPointRow(dp, new TableRow(this));
            }
        }
        save();

        questionNameField.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                question.setName(questionNameField.getText().toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_or_edit_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        question.setName(questionNameField.getText().toString());
        question.setMultiUse(multiUseCheckBox.isChecked());
        QuestionSetBank.saveQuestionSets();
    }

    public void addDataPoint(View view) {
        openDataPointDialog(new Datapoint(), new TableRow(this));
    }

    public void openDataPointDialog(final Datapoint dp, final TableRow row) {
        alert = new Dialog(this);
        alert.setContentView(R.layout.new_data_point);
        alert.setCancelable(false);
        alert.setTitle("Data point");

        EditText label = (EditText) alert.findViewById(R.id.label_field);
        label.setText(dp.getLabel());

        Spinner dataTypeSpinner = (Spinner) alert.findViewById(R.id.data_type_spinner);
        if (!dp.getDataType().equals("")) {
            String[] types = getResources().getStringArray(R.array.data_point_types_array);
            ArrayList<String> typesList = new ArrayList<String>(Arrays.asList(types));
            int index = typesList.indexOf(dp.getDataType());
            dataTypeSpinner.setSelection(index);
        }

        final TableLayout optionsTable = (TableLayout) alert.findViewById(R.id.options_menu_table);
        optionsTable.setStretchAllColumns(true);
        ArrayList<String> options = dp.getOptions();
        for (String s : options) {
            addOption(optionsTable, s);
        }

        if (dp.getOptionListType().equals("Checkbox")) {
            RadioButton checkboxRadioButton = (RadioButton) alert.findViewById(R.id.checkbox);
            checkboxRadioButton.setChecked(true);
        }
        else {
            RadioButton listRadioButton = (RadioButton) alert.findViewById(R.id.list);
            listRadioButton.setChecked(true);
        }

        Button addOptionButton = (Button) alert.findViewById(R.id.add_option_button);
        Button save = (Button) alert.findViewById(R.id.save_button);
        Button cancel = (Button) alert.findViewById(R.id.cancel_button);

        addOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOption(optionsTable, "");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText label = (EditText) alert.findViewById(R.id.label_field);
                Spinner typeSpinner = (Spinner) alert.findViewById(R.id.data_type_spinner);
                String type = (String) typeSpinner.getSelectedItem();

                dp.setLabel(label.getText().toString());
                dp.setDataType(type);

                if (type.equals("Option List")) {
                    RadioGroup optionListType = (RadioGroup) alert.findViewById(R.id.option_list_type);
                    if (optionListType.getCheckedRadioButtonId() == R.id.list) {
                        dp.setOptionsType("List");
                    }
                    else {
                        dp.setOptionsType("Checkbox");
                    }

                    int numOptions = optionsTable.getChildCount();
                    for (int i = 1; i < numOptions - 1; i++) {
                        TableRow optionRow = (TableRow) optionsTable.getChildAt(i);
                        EditText option = (EditText) optionRow.getChildAt(0);
                        dp.addOption(option.getText().toString());
                    }
                }

                question.addDataPoint(dp);
                updateDataPointRow(dp, row);

                alert.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        dataTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                TextView typeView = (TextView) selectedItemView;
                String type = typeView.getText().toString();
                if (type.equals("Option List")) {
                    optionsTable.setVisibility(View.VISIBLE);
                }
                else {
                    optionsTable.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                optionsTable.setVisibility(View.GONE);
            }
        });

        alert.show();
    }

    public void updateDataPointRow(final Datapoint dp, final TableRow row) {
        // If row has already been initialized with label, edit button, and delete button
        if (row.getChildCount() == 2) {
            TextView labelView = (TextView) row.getChildAt(0);
            labelView.setText(dp.getLabel());
        }
        // Otherwise, initialize the row and add it to the table
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(params);

            Button edit = new Button(this);
            edit.setText(dp.getLabel());
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDataPointDialog(dp, row);
                }
            });

            Button delete = new Button(this);
            delete.setText("Delete");
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    question.deleteDataPoint(dp);
                    dataPointTable.removeView(row);
                }
            });

            row.addView(edit);
            row.addView(delete);

            dataPointTable.addView(row);
        }
    }

    public void addOption(final TableLayout table, String option) {
        final TableRow row = new TableRow(this);

        EditText optionField = new EditText(this);
        optionField.setHint("Option");
        optionField.setText(option);

        Button delete = new Button(this);
        delete.setText("Delete");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.removeView(row);
            }
        });

        row.addView(optionField);
        row.addView(delete);

        table.addView(row, table.getChildCount() - 1);
    }
}
