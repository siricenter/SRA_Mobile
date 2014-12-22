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
import com.sra.helperClasses.CRUDFlinger;
import com.sra.objects.Areas;
import com.sra.objects.Households;
import com.sra.objects.Interviews;
import com.sra.objects.QuestionSet;
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
    private Households household;
    int area;
    int house;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        Intent intent = getIntent();
        area = intent.getIntExtra("area", 0);
        house = intent.getIntExtra("household", 0);
        String type = intent.getStringExtra("interviewType");
        if (area < 0) area = 0;
        if (house < 0) house = 0;
        getRegion(area, house);

        listView = (ListView) findViewById(R.id.question_set_list_view);

        TextView textView = new TextView(this);
        textView.setText("Interview");
        textView.setTextAppearance(this, R.style.MyListTitle);
        listView.addHeaderView(textView);

        questionSetNames = new ArrayList<String>();
        questionSetsAdapter = new QuestionSetListItemAdapter(this, questionSetNames);
        listView.setAdapter(questionSetsAdapter);
        refreshQuestionSets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.interview, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void refreshQuestionSets() {
        ArrayList<Interviews> interviews = household.getInterviews();
        for (Interviews i : interviews) {
            ArrayList<QuestionSet> sets = i.getQuestionSets();
            for (QuestionSet qs : sets) {
                questionSetNames.add(qs.getName());
                System.out.println("adding qs: " + qs.getName());
            }
        }
        questionSetsAdapter.notifyDataSetChanged();
    }

    public void goToDataGather(int area, int house, String qsName) {
        Intent intent = new Intent(this, DataGather.class);
        intent.putExtra("area", area);
        intent.putExtra("household", house);
        intent.putExtra("questionSetName", qsName);
        startActivity(intent);
    }

    public void getRegion(int area, int house) {
        region = null;
        try {
            String json = JSONUtilities.stringify(KVStore.getValue("Field"));
            System.out.println("Loading Areas");
            System.out.println(json);
            Gson gson = new GsonBuilder().create();
            region = gson.fromJson(json,Region.class);

            household = null;
            ArrayList<Areas> areas = region.getAreas();
            if (areas.size() > 0) {
                ArrayList<Households> households = areas.get(area).getHouseholds();
                if (households.size() > house) {
                    household = households.get(house);
                }
            }
            if (household == null) {
                household = new Households();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("Nothing Here");
        }
    }

    public void addQuestionSet(MenuItem menuItem) {
        final Dialog alert = new Dialog(this);
        alert.setContentView(R.layout.select_question_set_dialog);
        alert.setCancelable(true);
        alert.setTitle("Question Sets");

        final ListView listView = (ListView) alert.findViewById(R.id.question_set_list_view);
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<QuestionSet> sets = CRUDFlinger.getQuestionSets();
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
                household.addQuestionSet(CRUDFlinger.getQuestionSet(item));
                try {
                    KVStore.storeValue("Field", region);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Could not save region");
                }
            }
        });

        alert.show();
    }

    public void removeQuestionSet(String text) {
        questionSetNames.remove(text);
        questionSetsAdapter.notifyDataSetChanged();
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
                    goToDataGather(area, house, value);
                }
            });

            return itemView;
        }
    }
}
