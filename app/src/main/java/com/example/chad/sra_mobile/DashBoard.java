package com.example.chad.sra_mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import LocalDatabase.Area;
import LocalDatabase.DatabasePopulator;
import LocalDatabase.Household;
import LocalDatabase.SRAModel;

public class DashBoard extends Activity {
    SRAModel model = new SRAModel();
    ListView listView;
    Spinner spinner;
    ArrayList<String> areaValues;
    Area area;
    Household household;
    List<Area> areas;
    List<Household> households;
    ArrayAdapter<String> spinnerAdapter;
    customList adapter;
    ArrayList<String> householdValues;
    ArrayList<String> percents;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        areaValues = new ArrayList<String>();
        percents = new ArrayList<String>();
        area = new Area();
        household = new Household();
          householdValues = new ArrayList<String>();
        householdValues.add("Households");
        percents.add("%Complete");
        areas = area.getAllAreas();

        spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, areaValues);

        adapter = new customList(DashBoard.this,householdValues,percents);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        listView = (ListView) findViewById(R.id.Interviews);

        spinner = (Spinner) findViewById(R.id.areaSpinner);



        areaValues.add("Select Area");
        for(int i = 0;i < areas.size();i++){
            String item = areas.get(i).name;
            areaValues.add(item);
        }




        listView.setAdapter(adapter);
        spinner.setAdapter(spinnerAdapter);

        final Intent intent = new Intent(this, InterviewActivity.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position != 0){
                    int areaSpinner = spinner.getSelectedItemPosition();
                    // ListView Clicked item index
                    intent.putExtra("household", position);
                    intent.putExtra("area",areaSpinner);
                    startActivity(intent);
                }
            }

        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                households = household.getHousehold(i);
                householdValues.clear();
                percents.clear();
                householdValues.add("Households");
                percents.add("%Complete");
                for(int p = 0;p < households.size();++p){
                    int percent = households.get(p).percent;
                    String showPercent = "%" + percent;
                    String item = households.get(p).name;
                    percents.add(showPercent);
                    householdValues.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void syncDatabase(View v){

        DatabasePopulator create = new DatabasePopulator();
        create.populate();
        areaValues.clear();
        areaValues.add("Select Area");
        for(int i = 0;i < areas.size();i++){
            String item = areas.get(i).name;
            areaValues.add(item);
        }
        spinnerAdapter.notifyDataSetChanged();

    }

    public void addHousehold(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Name of new household?");
        alert.setMessage("Create a new Household");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               Editable newHousehold = input.getText();
                int position = spinner.getSelectedItemPosition();
                Household newHouse = new Household();
                        newHouse.percent = 0;
                        newHouse.area = Area.load(Area.class,position);
                        newHouse.name = newHousehold.toString();
                        newHouse.created_at = model.generateTimestamp();
                        newHouse.updated_at = model.generateTimestamp();
                        newHouse.save();

                households = household.getHousehold(position);
                householdValues.clear();
                percents.clear();
                percents.add("%Complete");
                householdValues.add("Households");
                for(int p = 0;p < households.size();++p){
                    int percent = households.get(p).percent;
                    String showPercent = "%" + percent;
                    String item = households.get(p).name;
                    householdValues.add(item);
                    percents.add(showPercent);
                }
                adapter.notifyDataSetChanged();
                System.out.println(newHousehold.toString() + "created");
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

}
