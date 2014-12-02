package com.example.chad.sraMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.sra.objects.QuestionSet;

import org.quickconnectfamily.kvkit.kv.KVStore;
import org.quickconnectfamily.kvkit.kv.KVStoreEventListener;

import java.io.Serializable;
import java.util.ArrayList;


public class QuestionSetList extends Activity {

    private TableLayout questionSetTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_set_list);

        questionSetTable = (TableLayout) findViewById(R.id.question_set_table);
        questionSetTable.setStretchAllColumns(true);

        KVStore.setActivity(getApplication());
        KVStore.setInMemoryStorageCount(10);
        KVStore.setStoreEventListener(new KVStoreEventListener() {
            @Override public void errorHappened(String key, Serializable value, Exception e) {}
            @Override public boolean shouldStore(String key, Serializable value) {
                return true;
            }
            @Override public void willStore(String key, Serializable value) {}
            @Override public void didStore(String key, Serializable value) {}
            @Override public boolean shouldDelete(String key) {
                return true;
            }
            @Override public void willDelete(String key) {}
            @Override public void didDelete(String key) {}
        });

        loadQuestionSets();
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

    @Override
    public void onResume() {
        super.onResume();
        loadQuestionSets();
    }

    public void addQuestionSet(View v) {
        Intent intent = new Intent(this, EditQuestionSet.class);
        intent.putExtra("questionSetName", "");
        intent.putExtra("isNewQuestionSet", true);
        startActivity(intent);
    }

    public void loadQuestionSets() {
        questionSetTable.removeAllViews();

        ArrayList<QuestionSet> questionSets = QuestionSet.getQuestionSets();
        for (final QuestionSet qs : questionSets) {
            final TableRow row = new TableRow(getBaseContext());

            Button questionSetButton = new Button(getBaseContext());
            questionSetButton.setText(qs.getName());
            questionSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(),EditQuestionSet.class);
                    intent.putExtra("questionSetName", qs.getName());
                    intent.putExtra("isNewQuestionSet", false);
                    startActivity(intent);
                }
            });

            Button deleteButton = new Button(getBaseContext());
            deleteButton.setText("Delete");
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    questionSetTable.removeView(row);
                    QuestionSet.deleteQuestionSet(qs);
                }
            });

            row.addView(questionSetButton);
            row.addView(deleteButton);

            questionSetTable.addView(row);
        }
    }

//    public void loadQuestionsFromBaseToLocal(){
//        Firebase ref = new Firebase("https://intense-inferno-7741.firebaseio.com/Question Sets");
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot data : dataSnapshot.getChildren()){
//                  final QuestionSet set = new QuestionSet(data.getName(),data.getRef().toString());
//                     for(DataSnapshot qs : data.getChildren()){
//                         Questions question = new Questions(qs.getRef().toString());
//                         for(DataSnapshot dp : qs.getChildren()){
//                             Datapoint dataPoint = new Datapoint();
//                             dataPoint.setLabel(dp.child("label").getValue().toString());
//                             dataPoint.setDataType(dp.child("data type").getValue().toString());
//                             question.addDataPoint(dataPoint);
//                         }
//                         set.addQuestion(question);
//                     }
//                    TableRow row = new TableRow(getBaseContext());
//
//                    Button question = new Button(getBaseContext());
//                    question.setText(data.getName());
//                    question.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent intent = new Intent(getBaseContext(),CreateQuestionSet.class);
//                            intent.putExtra("questionSet", "I am the question set");
//                            startActivity(intent);
//                        }
//                    });
//
//                    Button delete = new Button(getBaseContext());
//                    delete.setText("Delete");
//
//                    row.addView(question);
//                    row.addView(delete);
//
//                    int index = questionSetTable.getChildCount() - 1;
//                    questionSetTable.addView(row, index);
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }
}
