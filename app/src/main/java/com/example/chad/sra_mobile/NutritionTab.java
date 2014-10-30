package com.example.chad.sra_mobile;

import android.content.Context;
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
import LocalDatabase.Interview;

public class NutritionTab extends Fragment {

    private TableLayout foodTable;
    private TableRow columnHeaderRow;
    Interview interview = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition_tab, container, false);

        // Get interview object from main activity
        InterviewActivity interviewActivity = (InterviewActivity) getActivity();
        interview = interviewActivity.getInterview();

        // Header
        TextView householdLabel = (TextView) view.findViewById(R.id.interview_household_label);
        householdLabel.setText(interview.household.name + "'s Nutrition");

        // Set food table attributes
        foodTable = (TableLayout) view.findViewById(R.id.food_table);
        foodTable.setStretchAllColumns(true);
        foodTable.setColumnShrinkable(3, true);
        foodTable.setColumnShrinkable(4, true);
        foodTable.setColumnShrinkable(5, true);
        columnHeaderRow = (TableRow) view.findViewById(R.id.food_item_header_row);

        // Get all consumed foods associated with that interview and add them to the table
        List<ConsumedFood> foods = ConsumedFood.getConsumedFoods(interview.getId());
        int numFoodItems = foods.size();
        for (int i = 0; i < numFoodItems; i++) {
            addFoodToTable(foods.get(i));
        }

        // Setup the addFoodButton
        Button addFoodButton = (Button) view.findViewById(R.id.add_food_button);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFoodToTable(new ConsumedFood());
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
            ConsumedFoodRow row = (ConsumedFoodRow) foodTable.getChildAt(i);
            long databaseID = row.getDatabaseID();

            ConsumedFood food;
            if (databaseID == -1) {
                food = new ConsumedFood();
            }
            else {
                food = ConsumedFood.load(ConsumedFood.class, databaseID);
            }
            food.interview = interview;
            food.entered_food = row.enteredFood.getText().toString();
            if (row.servingSize.getText().length() > 0) {
                food.servings = Float.parseFloat(row.servingSize.getText().toString());
            }
            food.units = (String) row.servingUnits.getSelectedItem();
            food.quantity = (Integer) row.quantity.getSelectedItem();
            food.frequency = (String) row.frequency.getSelectedItem();
            food.post();
            row.setDatabaseID(food.getId());
        }
    }

    public void addFoodToTable(ConsumedFood food) {
        // Create new consumed food row given the ConsumedFood object
        ConsumedFoodRow row = new ConsumedFoodRow(this.getActivity(), food);

        // Find the correct place to insert into the table (right above the + button)
        int numChildren = foodTable.getChildCount();
        int insertionIndex = numChildren - 1;

        // Insert at that index
        foodTable.addView(row, insertionIndex);

        columnHeaderRow.setVisibility(View.VISIBLE);
    }

    /*
     * This class defines a custom table row for a ConsumedFood item.
    */
    public class ConsumedFoodRow extends TableRow {
        long databaseID = -1;
        public long getDatabaseID() { return databaseID; }
        public void setDatabaseID(long id) { databaseID = id; }

        public EditText enteredFood;
        public EditText servingSize;
        public Spinner  servingUnits;
        public Spinner  quantity;
        public Spinner  frequency;
        private Button removeButton;

        public void removeFromTableAndDeleteFromDatabase() {
            if (databaseID != -1) {
                ConsumedFood.delete(ConsumedFood.class, databaseID);
            }
            foodTable.removeView(this);
            if (foodTable.getChildCount() < 3) {
                columnHeaderRow.setVisibility(View.GONE);
            }
        }

        public ConsumedFoodRow(Context context, ConsumedFood food) {
            super(context);
            this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

            // Entered food field
            enteredFood = new EditText(context);
            enteredFood.setText(food.entered_food);

            // Food serving size
            servingSize = new EditText(context);
            servingSize.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            if (food.servings >= 0) {
                servingSize.setText("" + food.servings);
            }

            // Food item amount units
            servingUnits = new Spinner(context);
            String[] unitsArray = getResources().getStringArray(R.array.serving_units);
            ArrayList<String> unitsArrayList = new ArrayList<String>(Arrays.asList(unitsArray));
            ArrayAdapter<String> unitsAdapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, unitsArrayList);
            servingUnits.setAdapter(unitsAdapter);
            if (!food.units.isEmpty()) {
                int indexOfUnits = unitsArrayList.indexOf(food.units);
                servingUnits.setSelection(indexOfUnits);
            }

            // Quantity - number of servings
            quantity = new Spinner(context);
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 1; i <= 15; i++) { list.add(i); }
            ArrayAdapter<Integer> quantityAdapter = new ArrayAdapter<Integer>(context,
                    android.R.layout.simple_spinner_item, list);
            quantity.setAdapter(quantityAdapter);
            int quantityPosition = list.indexOf((Integer) food.quantity);
            quantity.setSelection(quantityPosition);

            // Consumption frequency
            frequency = new Spinner(context);
            String[] freqArray = getResources().getStringArray(R.array.frequency);
            ArrayList<String> freqArrayList = new ArrayList<String>(Arrays.asList(freqArray));
            ArrayAdapter<String> freqAdapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, freqArrayList);
            frequency.setAdapter(freqAdapter);
            int freqPosition = freqArrayList.indexOf(food.frequency);
            frequency.setSelection(freqPosition);

            // Remove food item button
            removeButton = new Button(context);
            removeButton.setText(R.string.remove_food_item_button);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeFromTableAndDeleteFromDatabase();
                }
            });

            enteredFood.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT));
            servingSize.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT));
            servingUnits.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT));
            quantity.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT));
            frequency.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT));
            removeButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT));

            // Add fields and spinners to row
            this.addView(enteredFood);
            this.addView(servingSize);
            this.addView(servingUnits);
            this.addView(quantity);
            this.addView(frequency);
            this.addView(removeButton);

            enteredFood.setMaxWidth(enteredFood.getWidth());
            servingSize.setMaxWidth(servingSize.getWidth());
            servingSize.setHorizontallyScrolling(true);

            // Get id from ConsumedFood object if it has been saved to the database already
            Long id = food.getId();
            if (id != null) {
                databaseID = food.getId();
            }
        }
    }
}
