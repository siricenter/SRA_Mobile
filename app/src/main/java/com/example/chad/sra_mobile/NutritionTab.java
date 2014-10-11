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

import java.util.List;
import java.util.ArrayList;

import LocalDatabase.ConsumedFood;
import LocalDatabase.Household;
import LocalDatabase.Interview;

public class NutritionTab extends Fragment {

    private TableLayout foodTable;
    int householdID = -1;
    int areaID = -1;
    Interview interview = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_nutrition_tab, container, false);
        foodTable = (TableLayout) view.findViewById(R.id.food_table);

        InterviewActivity interviewActivity = (InterviewActivity) getActivity();
        householdID = interviewActivity.getHouseholdID();
        areaID = interviewActivity.getAreaID();
        interview = interviewActivity.getInterview();
        List<ConsumedFood> foods = ConsumedFood.getConsumedFoods(interview.getId());
        int numFoodItems = foods.size();
        for (int i = 0; i < numFoodItems; i++) {
            addFoodItemRow(foods.get(i));
        }

        TextView householdLabel = (TextView) view.findViewById(R.id.interview_household_label);
        List<Household> households = Household.getHousehold(areaID);
        Household house = households.get(householdID - 1);
        householdLabel.setText(house.name + " household Interview");

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
                saveFoodItems();
            }
        });

        return view;
    }

    public void onDestroyView() {
        saveFoodItems();
        super.onDestroyView();
    }

    public void saveFoodItems() {
        int numRows = foodTable.getChildCount();
        for (int i = 1; i < numRows - 1; i++) {
            TableRow row = (TableRow) foodTable.getChildAt(i);
            TextView idField = (TextView) row.getChildAt(0);
            EditText enteredFood = (EditText) row.getChildAt(1);
            //EditText servingSize = (EditText) row.getChildAt(2);

            ConsumedFood food;
            if (idField.getText().toString().equals("-1")) {
                food = new ConsumedFood();
            }
            else {
                long id = Integer.parseInt(idField.getText().toString());
                food = ConsumedFood.load(ConsumedFood.class, id);
            }
            food.interview = interview;
            food.entered_food = enteredFood.getText().toString();
            //food.servings = Integer.parseInt(servingSize.getText().toString());
            food.save();
            idField.setText(food.getId().toString());
        }
    }

    public void addNewFoodItem(View v) {
        addFoodItemRow(new ConsumedFood());
    }

    public void addFoodItemRow(ConsumedFood food) {
        final TableRow row = new TableRow(this.getActivity());

        // Food database id (will be hidden in view)
        final TextView foodIDView = new TextView(this.getActivity());
        Long id = food.getId();
        String strID;
        if (id == null) {
            strID = "-1";
        }
        else {
            strID = id.toString();
        }
        foodIDView.setText(strID);
        foodIDView.setVisibility(View.GONE);
        row.addView(foodIDView);

        // Food item name
        EditText foodName = new EditText(this.getActivity());
        foodName.setHint("food item");
        foodName.setText(food.entered_food);
        row.addView(foodName);

        // Food serving size
        EditText servingSize = new EditText(this.getActivity());
        servingSize.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        servingSize.setHint("serving size");
        //servingSize.setText(Integer.toString(food.servings));
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
        //servings.setSelection(food.quantity - 1);
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
                TextView idView = (TextView) row.getChildAt(0);
                if (idView.getText().toString().equals("-1")) { }
                else {
                    long id = Integer.parseInt(idView.getText().toString());
                    ConsumedFood.delete(ConsumedFood.class, id);
                }
                foodTable.removeView(row);
            }
        });
        row.addView(removeFoodItem);

        int numChildren = foodTable.getChildCount();
        int insertionIndex = numChildren - 1;
        foodTable.addView(row, insertionIndex);
    }
}
