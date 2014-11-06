package com.example.chad.sra_mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class QuestionSetList extends Activity {

    private TableLayout questionSetTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_set_list);

        questionSetTable = (TableLayout) findViewById(R.id.question_set_table);

        Firebase.setAndroidContext(this);
        loadQuestionsFromBaseToLocal();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question_set_list, menu);
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

    public void addQuestionSet(View v) {
        Intent intent = new Intent(this, CreateQuestionSet.class);
        startActivity(intent);
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

                    int index = questionSetTable.getChildCount() - 1;
                    questionSetTable.addView(row, index);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}