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



public class NutritionTab extends Fragment {

    private TableLayout foodTable;
    private TableRow columnHeaderRow;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition_tab, container, false);

        // Get interview object from main activity
        InterviewActivity interviewActivity = (InterviewActivity) getActivity();


        // Header
        TextView householdLabel = (TextView) view.findViewById(R.id.interview_household_label);


        // Set food table attributes
        foodTable = (TableLayout) view.findViewById(R.id.food_table);
        foodTable.setStretchAllColumns(true);
        foodTable.setColumnShrinkable(3, true);
        foodTable.setColumnShrinkable(4, true);
        foodTable.setColumnShrinkable(5, true);
        columnHeaderRow = (TableRow) view.findViewById(R.id.food_item_header_row);



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


        }
    }

    public void addFoodToTable() {

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

            }
            foodTable.removeView(this);
            if (foodTable.getChildCount() < 3) {
                columnHeaderRow.setVisibility(View.GONE);
            }
        }

        public ConsumedFoodRow(Context context) {
            super(context);
            this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.FILL_PARENT));



            // Quantity - number of servings
            quantity = new Spinner(context);
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 1; i <= 15; i++) { list.add(i); }
            ArrayAdapter<Integer> quantityAdapter = new ArrayAdapter<Integer>(context,
                    android.R.layout.simple_spinner_item, list);
            quantity.setAdapter(quantityAdapter);



            // Consumption frequency
            frequency = new Spinner(context);
            String[] freqArray = getResources().getStringArray(R.array.frequency);
            ArrayList<String> freqArrayList = new ArrayList<String>(Arrays.asList(freqArray));
            ArrayAdapter<String> freqAdapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, freqArrayList);
            frequency.setAdapter(freqAdapter);


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

        }
    }
}
