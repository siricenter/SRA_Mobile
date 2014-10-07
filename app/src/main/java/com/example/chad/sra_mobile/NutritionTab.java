package com.example.jordanreed.interview;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class NutritionTab extends Fragment {

    private TableLayout foodTable;
    //private TableRow defaultFoodItemRow;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_nutrition_tab, container, false);
        //defaultFoodItemRow = (TableRow) inflater.inflate(R.layout.fragment_nutrition_row, container, false);

        Button addFoodButton = (Button) view.findViewById(R.id.add_food_button_id);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewFoodItem(view);
            }
        });
        foodTable = (TableLayout) view.findViewById(R.id.food_table);

        return view;
    }

    public void addNewFoodItem(View v) {
        final TableRow row = new TableRow(this.getActivity());

        // Food item name
        EditText foodName = new EditText(this.getActivity());
        foodName.setHint("food item");
        row.addView(foodName);

        // Food serving size
        EditText servingSize = new EditText(this.getActivity());
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

        // Cooked or raw
        Spinner cookedOrRaw = new Spinner(this.getActivity());
        ArrayAdapter<CharSequence> cookedAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.cooked_raw, android.R.layout.simple_spinner_item);
        cookedOrRaw.setAdapter(cookedAdapter);
        row.addView(cookedOrRaw);

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

        foodTable.addView(row, 0);
    }
}
