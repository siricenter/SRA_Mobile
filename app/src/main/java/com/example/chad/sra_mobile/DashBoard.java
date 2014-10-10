package com.example.chad.sra_mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.activeandroid.annotation.Table;

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

    Button syncButton;
    ScrollView scrollView;
    TableLayout innerTableLayout;
    View.OnClickListener householdClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        areas = area.getAllAreas();

        //listView = (ListView) findViewById(R.id.Interviews);

        spinner = (Spinner) findViewById(R.id.areaSpinner);

        areaValues.add("Select Area");
        for(int i = 0;i < areas.size();i++){
            String item = areas.get(i).name;
            areaValues.add(item);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fillScrollView(Household.getHousehold(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                innerTableLayout.removeAllViews();
            }
        });

        /****/

        syncButton = (Button) findViewById(R.id.button2);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncDatabase();
            }
        });

        // = (ScrollView) findViewById(R.id.interviewScrollView);
        innerTableLayout = (TableLayout) findViewById(R.id.innerTable);

        // listeners
        householdClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = (Long) view.getTag();
                Log.d("householdClickListener got value", "id=" + id);
            }
        };

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

    public void syncDatabase(){
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

    public void householdClick(View v) {

    }


    public void fillScrollView(List<Household> households) {
        for(Household household : households) {
            addRow(household);
        }
    }

    /**
     * Ths will add a row to the scroll view when givien a household
     * @param household
     */
    public void addRow(Household household) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRow = (TableRow) inflater.inflate(R.layout.row, null);
        TextView newNameTextView = (TextView) newRow.findViewById(R.id.txt1);
        TextView newPercentTextView = (TextView) newRow.findViewById(R.id.txt2);

        newNameTextView.setText(household.name);
        newPercentTextView.setText(household.percent);

        newRow.setOnClickListener(householdClickListener);
        newRow.setTag(household.getId());

        innerTableLayout.addView(newRow);
    }

}
