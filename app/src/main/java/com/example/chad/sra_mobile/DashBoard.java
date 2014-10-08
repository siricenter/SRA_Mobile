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


public class DashBoard extends Activity {

    ListView listView;
    Spinner spinner;
    ArrayList<String> areaValues;
    Area area;
    List<Area> areas;
    String[] values;
    ArrayAdapter<String> spinnerAdapter;
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        areaValues = new ArrayList<String>();
        area = new Area();
        values = new String[] {
                "Households"
        };

        areas = area.getAllAreas();

        spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, areaValues);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

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
        create.deleteAll();
        create.populate();

        for(int i = 0;i < areas.size();i++){
            String item = areas.get(i).name;
            areaValues.add(item);
        }
        spinnerAdapter.notifyDataSetChanged();

    }
}
