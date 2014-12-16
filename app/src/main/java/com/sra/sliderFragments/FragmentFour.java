package com.sra.sliderFragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.chad.sraMobile.EditQuestionSet;
import com.example.chad.sraMobile.InterviewActivity;
import com.example.chad.sraMobile.R;
import com.sra.objects.QuestionSet;
import com.sra.objects.QuestionSetBank;

import org.quickconnectfamily.kvkit.kv.KVStore;
import org.quickconnectfamily.kvkit.kv.KVStoreEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class FragmentFour extends Fragment {

    ImageView ivIcon;
    TextView tvItemName;

    public static final String IMAGE_RESOURCE_ID = "iconResourceID";
    public static final String ITEM_NAME = "itemName";

    private TableLayout questionSetTable;

    public FragmentFour() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.button4slide, container, false);

        ivIcon = (ImageView) view.findViewById(R.id.frag4_icon);
        tvItemName = (TextView) view.findViewById(R.id.frag4_text);

        tvItemName.setText(getArguments().getString(ITEM_NAME));
        ivIcon.setImageDrawable(view.getResources().getDrawable(
                getArguments().getInt(IMAGE_RESOURCE_ID)));

        questionSetTable = (TableLayout) view.findViewById(R.id.question_set_table);
        questionSetTable.setStretchAllColumns(true);

        Button addQuestionSetButton = (Button) view.findViewById(R.id.add_question_set_button);
        addQuestionSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestionSet();
            }
        });

        Button tempButton = (Button) view.findViewById(R.id.go_to_data_gather_button);
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InterviewActivity.class);
                startActivity(intent);
            }
        });

        KVStore.setActivity(getActivity().getApplication());
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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadQuestionSets();
    }

    public void addQuestionSet() {
        Intent intent = new Intent(getActivity(), EditQuestionSet.class);
        intent.putExtra("questionSetName", "");
        intent.putExtra("isNewQuestionSet", true);
        startActivity(intent);
    }

    public void loadQuestionSets() {
        questionSetTable.removeAllViews();

        ArrayList<QuestionSet> questionSets = QuestionSetBank.getQuestionSets();
        for (final QuestionSet qs : questionSets) {
            final TableRow row = new TableRow(getActivity());

            Button questionSetButton = new Button(getActivity());
            questionSetButton.setText(qs.getName());
            questionSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),EditQuestionSet.class);
                    intent.putExtra("questionSetName", qs.getName());
                    intent.putExtra("isNewQuestionSet", false);
                    startActivity(intent);
                }
            });

            Button deleteButton = new Button(getActivity());
            deleteButton.setText("Delete");
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    questionSetTable.removeView(row);
                    QuestionSetBank.deleteQuestionSet(qs);
                }
            });

            row.addView(questionSetButton);
            row.addView(deleteButton);

            questionSetTable.addView(row);
        }
    }
}