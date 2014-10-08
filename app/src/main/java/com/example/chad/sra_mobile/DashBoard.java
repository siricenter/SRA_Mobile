package com.example.chad.sra_mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import LocalDatabase.Area;
import LocalDatabase.DatabasePopulator;
import LocalDatabase.Household;


public class DashBoard extends Activity {

    ListView listView;
    Spinner spinner;
    ArrayList<String> areaValues;
    Area area;
    Household household;
    List<Area> areas;
    List<Household> households;
    ArrayAdapter<String> spinnerAdapter;
    ArrayAdapter<String> adapter;
    DatabasePopulator create;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        create = new DatabasePopulator();
        areaValues = new ArrayList<String>();
        area = new Area();
        household = new Household();
        final ArrayList<String> householdValues = new ArrayList<String>();
        householdValues.add("Households");

        areas = area.getAllAreas();

        spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, areaValues);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, householdValues);

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

        final Intent intent = new Intent(this, Interview.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemP = position;

                intent.putExtra("household",itemP);



                /* ListView Clicked item value */
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert

                }

        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                households = household.getHousehold(i);
                householdValues.clear();
                householdValues.add("Households");
                for(int p = 0;p < households.size();++p){
                    String item = households.get(p).name;
                    householdValues.add(item);
                }
                System.out.println(householdValues);
                adapter.notifyDataSetChanged();
                System.out.println("Finished");
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
        create.deleteAll();
        create.populate();
        areaValues.clear();
        areaValues.add("Select Area");
        for(int i = 0;i < areas.size();i++){
            String item = areas.get(i).name;
            areaValues.add(item);
        }
        spinnerAdapter.notifyDataSetChanged();

    }
}
