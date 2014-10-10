package com.example.chad.sra_mobile;

import android.os.Bundle;
import android.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import com.activeandroid.query.Delete;

import java.util.List;
import java.util.ArrayList;

import LocalDatabase.ConsumedFood;
import LocalDatabase.Household;
import LocalDatabase.Interview;

public class NutritionTab extends Fragment {

    private TableLayout foodTable;
    Household household = null;
    Interview interview = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_nutrition_tab, container, false);

        InterviewActivity interviewActivity = (InterviewActivity) getActivity();
        household = interviewActivity.getHousehold();
        interview = interviewActivity.getInterview();

//        List<ConsumedFood> foods = ConsumedFood.getConsumedFoods(interview.getId());
//        int numFoodItems = foods.size();
//        System.out.println("Number of food items: " + numFoodItems);
//        for (int i = 0; i < numFoodItems; i++) {
//            addNewFoodItemRow(foods.get(i).frequency);
//        }

        foodTable = (TableLayout) view.findViewById(R.id.food_table);

        TextView householdLabel = (TextView) view.findViewById(R.id.interview_household_label);
        householdLabel.setText(household.name + " family Interview");

        Button addFoodButton = (Button) view.findViewById(R.id.add_food_button);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewFoodItem(view);
            }
        });

        Button saveInterviewButton = (Button) view.findViewById(R.id.save_interview_button);
        saveInterviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numRows = foodTable.getChildCount();
                for (int i = 1; i < numRows - 1; i++) {
                    TableRow row = (TableRow) foodTable.getChildAt(i);
                    EditText item = (EditText) row.getChildAt(0);
                    System.out.println(item.getText());
                }
            }
        });

        return view;
    }

    public void addNewFoodItem(View v) {
        addNewFoodItemRow("");
    }

    public void addNewFoodItemRow(String food_item) {
        final TableRow row = new TableRow(this.getActivity());

        // Food item name
        EditText foodName = new EditText(this.getActivity());
        foodName.setHint("food item");
        row.addView(foodName);

        // Food serving size
        EditText servingSize = new EditText(this.getActivity());
        servingSize.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        servingSize.setHint("serving size");
        row.addView(servingSize);

        // Food item amount units
        Spinner servingUnits = new Spinner(this.getActivity());
        ArrayAdapter<CharSequence> unitsAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.serving_units, android.R.layout.simple_spinner_item);
        servingUnits.setAdapter(unitsAdapter);
        row.addView(servingUnits);

        // Number of servings
        Spinner servings = new Spinner(this.getActivity());
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= 10; i++) { list.add(i); }
        ArrayAdapter<Integer> servingsAdapter = new ArrayAdapter<Integer>(this.getActivity(),
                android.R.layout.simple_spinner_item, list);
        servings.setAdapter(servingsAdapter);
        row.addView(servings);

        // Consumption frequency
        Spinner consumptionFreq = new Spinner(this.getActivity());
        ArrayAdapter<CharSequence> freqAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.frequency, android.R.layout.simple_spinner_item);
        consumptionFreq.setAdapter(freqAdapter);
        row.addView(consumptionFreq);

        // Remove food item button
        Button removeFoodItem = new Button(this.getActivity());
        removeFoodItem.setText(R.string.remove_food_item_button);
        removeFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodTable.removeView(row);
            }
        });
        row.addView(removeFoodItem);

        int numChildren = foodTable.getChildCount();
        int insertionIndex = numChildren - 1;
        foodTable.addView(row, insertionIndex);
    }
}
