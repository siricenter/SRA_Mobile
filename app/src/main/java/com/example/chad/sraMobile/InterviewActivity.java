package com.example.chad.sraMobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class InterviewActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        Intent intent = getIntent();

        listView = (ListView) findViewById(R.id.question_set_list_view);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile" };
        QuestionSetListItemAdapter adapter = new QuestionSetListItemAdapter(this, values);
        listView.setAdapter(adapter);
    }

    public void removeQuestionSet(View v) {
        listView.removeViewInLayout(v);
        listView.refreshDrawableState();
    }

    public class QuestionSetListItemAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public QuestionSetListItemAdapter(Context context, String[] values) {
            super(context, R.layout.interview_list_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View itemView = inflater.inflate(R.layout.interview_list_item, parent, false);
            TextView textView = (TextView) itemView.findViewById(R.id.list_item_name);
            textView.setText(values[position]);

            Button removeInterviewButton = (Button) itemView.findViewById(R.id.delete_button);
            removeInterviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeQuestionSet(itemView);
                }
            });

            return itemView;
        }
    }
}
