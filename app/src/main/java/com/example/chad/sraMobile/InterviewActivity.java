package com.example.chad.sraMobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sra.objects.QuestionSet;
import com.sra.objects.QuestionSetBank;
import com.sra.objects.Region;

import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONUtilities;
import org.quickconnectfamily.kvkit.kv.KVStore;

import java.util.ArrayList;

public class InterviewActivity extends Activity {

    private ArrayList<String> questionSetNames;
    private ListView listView;
    private QuestionSetListItemAdapter questionSetsAdapter;

    private Region region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        Intent intent = getIntent();

        listView = (ListView) findViewById(R.id.question_set_list_view);

        TextView textView = new TextView(this);
        textView.setText("Interview");
        textView.setTextAppearance(this, R.style.MyListTitle);
        listView.addHeaderView(textView);

        questionSetNames = new ArrayList<String>();
//        region = getRegion();


        questionSetsAdapter = new QuestionSetListItemAdapter(this, questionSetNames);
        listView.setAdapter(questionSetsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.interview, menu);
        return true;
    }

    public Region getRegion() {
        Region r = null;
        try {
            String json = JSONUtilities.stringify(KVStore.getValue("Field"));
            System.out.println("Loading Areas");
            System.out.println(json);
            Gson gson = new GsonBuilder().create();
            r = gson.fromJson(json,Region.class);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("Nothing Here");
        }
        return region;
    }

    public void addQuestionSet(MenuItem menuItem) {
        final Dialog alert = new Dialog(this);
        alert.setContentView(R.layout.select_question_set_dialog);
        alert.setCancelable(true);
        alert.setTitle("Question Sets");

        final ListView listView = (ListView) alert.findViewById(R.id.question_set_list_view);
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<QuestionSet> sets = QuestionSetBank.getQuestionSets();
        for (QuestionSet qs : sets) {
            list.add(qs.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                questionSetNames.add(item);
                questionSetsAdapter.notifyDataSetChanged();
                alert.dismiss();
            }
        });

        alert.show();
    }

    public void removeQuestionSet(String text) {
        questionSetNames.remove(text);
        questionSetsAdapter.notifyDataSetChanged();
    }

    public void goToDataGather(String questionSetName) {
        Intent intent = new Intent(this, DataGather.class);
        intent.putExtra("questionSetName", questionSetName);
        startActivity(intent);
    }

    public class QuestionSetListItemAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> values;

        public QuestionSetListItemAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.interview_list_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View itemView = inflater.inflate(R.layout.interview_list_item, parent, false);
            TextView textView = (TextView) itemView.findViewById(R.id.list_item_name);
            final String value = values.get(position);
            textView.setText(value);

            Button removeInterviewButton = (Button) itemView.findViewById(R.id.delete_button);
            removeInterviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setMessage("Are you sure you want to remove this question set?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    removeQuestionSet(value);
                                    Toast.makeText(context, "Deleted " + value, Toast.LENGTH_SHORT).show();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDataGather(value);
                }
            });

            return itemView;
        }
    }
}
