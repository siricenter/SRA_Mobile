package com.example.chad.sra_mobile;

import android.app.ActionBar;
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
import java.util.Arrays;

import LocalDatabase.ConsumedFood;
import LocalDatabase.Household;
import LocalDatabase.Interview;

public class NutritionTab extends Fragment {

    private TableLayout foodTable;
    Interview interview = null;

//    public class ConsumedFoodRow extends TableRow {
//        int databaseID = -1;
//        public ConsumedFoodRow(Context context) {
//            super(context);
//        }
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_nutrition_tab, container, false);
        foodTable = (TableLayout) view.findViewById(R.id.food_table);
        foodTable.setStretchAllColumns(true);

        InterviewActivity interviewActivity = (InterviewActivity) getActivity();
        interview = interviewActivity.getInterview();
        List<ConsumedFood> foods = ConsumedFood.getConsumedFoods(interview.getId());
        int numFoodItems = foods.size();
        for (int i = 0; i < numFoodItems; i++) {
            addFoodItemRow(foods.get(i));
        }

        TextView householdLabel = (TextView) view.findViewById(R.id.interview_household_label);
        householdLabel.setText(interview.household.name + ": Consumed Foods");

        Button addFoodButton = (Button) view.findViewById(R.id.add_food_button);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewFoodItem(view);
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
            int childCount = row.getChildCount();
            EditText enteredFood = (EditText) row.getChildAt(0);
            EditText servingSize = (EditText) row.getChildAt(1);
            Spinner  units       = (Spinner)  row.getChildAt(2);
            Spinner  quantity    = (Spinner)  row.getChildAt(3);
            Spinner  frequency   = (Spinner)  row.getChildAt(4);
            TextView idField     = (TextView) row.getChildAt(childCount - 1);

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
            if (servingSize.getText().length() > 0) {
                food.servings = Float.parseFloat(servingSize.getText().toString());
            }
            food.units        = (String)  units.getSelectedItem();
            food.quantity     = (Integer) quantity.getSelectedItem();
            food.frequency    = (String)  frequency.getSelectedItem();
            food.save();
            idField.setText(food.getId().toString());
        }
    }

    public void addNewFoodItem(View v) {
        addFoodItemRow(new ConsumedFood());
    }

    public void addFoodItemRow(ConsumedFood food) {
        final TableRow row = new TableRow(this.getActivity());

        // Entered food field
        EditText enteredFood = new EditText(this.getActivity());
        enteredFood.setHint(R.string.entered_food_hint);
        enteredFood.setText(food.entered_food);

        // Food serving size
        EditText servingSize = new EditText(this.getActivity());
        servingSize.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        servingSize.setHint(R.string.serving_size_hint);
        if (food.servings >= 0) {
            servingSize.setText("" + food.servings);
        }

        // Food item amount units
        Spinner servingUnits = new Spinner(this.getActivity());
        String[] unitsArray = getResources().getStringArray(R.array.serving_units);
        ArrayList<String> unitsArrayList = new ArrayList<String>(Arrays.asList(unitsArray));
        ArrayAdapter<String> unitsAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, unitsArrayList);
        servingUnits.setAdapter(unitsAdapter);
        if (!food.units.isEmpty()) {
            int indexOfUnits = unitsArrayList.indexOf(food.units);
            servingUnits.setSelection(indexOfUnits);
        }

        // Quantity - number of servings
        Spinner quantity = new Spinner(this.getActivity());
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= 15; i++) { list.add(i); }
        ArrayAdapter<Integer> quantityAdapter = new ArrayAdapter<Integer>(this.getActivity(),
                android.R.layout.simple_spinner_item, list);
        quantity.setAdapter(quantityAdapter);
        int quantityPosition = list.indexOf((Integer) food.quantity);
        quantity.setSelection(quantityPosition);

        // Consumption frequency
        Spinner consumptionFreq = new Spinner(this.getActivity());
        String[] freqArray = getResources().getStringArray(R.array.frequency);
        ArrayList<String> freqArrayList = new ArrayList<String>(Arrays.asList(freqArray));
        ArrayAdapter<String> freqAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, freqArrayList);
        consumptionFreq.setAdapter(freqAdapter);
        int freqPosition = freqArrayList.indexOf(food.frequency);
        consumptionFreq.setSelection(freqPosition);

        // Remove food item button
        Button removeFoodItem = new Button(this.getActivity());
        removeFoodItem.setText(R.string.remove_food_item_button);
        removeFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView idView = (TextView) row.getChildAt(row.getChildCount() - 1);
                if (idView.getText().toString().equals("-1")) { }
                else {
                    long id = Integer.parseInt(idView.getText().toString());
                    ConsumedFood.delete(ConsumedFood.class, id);
                }
                foodTable.removeView(row);
            }
        });

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

        // Add fields and spinners to row
        row.addView(enteredFood);
        row.addView(servingSize);
        row.addView(servingUnits);
        row.addView(quantity);
        row.addView(consumptionFreq);
        row.addView(removeFoodItem);
        row.addView(foodIDView);

        // Insert row into table
        int numChildren = foodTable.getChildCount();
        int insertionIndex = numChildren - 1;
        foodTable.addView(row, insertionIndex);
    }
}
