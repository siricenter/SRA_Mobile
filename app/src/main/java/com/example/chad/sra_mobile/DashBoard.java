package com.example.chad.sra_mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;


import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;
import LocalDatabase.Area;
import LocalDatabase.DatabasePopulator;
import LocalDatabase.Household;
import LocalDatabase.Person;
import LocalDatabase.SRAModel;

public class DashBoard extends Activity {
    SRAModel model = new SRAModel();
    ListView listView;
    Spinner spinner;
    ArrayList<String> areaValues;
    Area area;
    Household household;
    Person person;
    List<Area> areas;
    List<Household> households;
    List<Person> people;
    ArrayAdapter<String> spinnerAdapter;
    customList adapter;
    ArrayList<String> householdValues;
    ArrayList<String> peopleValues;
    ArrayList<String> percents;
    TableLayout innerTableLayout;
    int navigationMarker;
    int flag;
    Dialog alert;
    int areaPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        person = new Person();

        areaValues = new ArrayList<String>();
        areas = area.getAllAreas();
        percents = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.Interviews);
        householdValues = new ArrayList<String>();
        spinner = (Spinner) findViewById(R.id.areaSpinner);
        flag = 0;
        areaValues.add("Select Area");
        householdValues.add("Households");
        percents.add("#Members");

        for(Area area : areas) {
            areaValues.add(area.name);
        }

        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, areaValues);
        adapter = new customList(DashBoard.this,householdValues,percents);

        listView.setAdapter(adapter);
        spinner.setAdapter(spinnerAdapter);

        final Intent intent = new Intent(this, InterviewActivity.class);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    if (navigationMarker == 0){
                        loadHouseholdsIntoView(i);
                    }
                    else{
                        loadMembersIntoViewFromSpinner();
                    }
                    adapter.notifyDataSetChanged();
                    spinnerAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                innerTableLayout.removeAllViews();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(position != 0){
                if(navigationMarker == 0){
                    if(flag == 0){
                        loadMembersIntoView(position);
                        flag = 1;
                    }
                    loadHouseholdsIntoSpinner();
                    navigationMarker = 1;
                }
                else{
                    Person member = new Person();
                    for(Person person : people){
                        if (householdValues.get(position) == person.given_name ){
                            member = person;
                        }
                    }
                    intent.putExtra("HouseHold", member.household_id);
                    intent.putExtra("Person",member.getId());
                    startActivity(intent);
                }
                adapter.notifyDataSetChanged();
                spinnerAdapter.notifyDataSetChanged();
            }
         }

         });
    }

    public void loadHouseholdsIntoView(int position){
        households = household.getHousehold(position);
        people =  person.getMembers(position);
        householdValues.clear();
        percents.clear();
        householdValues.add("Households");
        percents.add("#Members");

        for (Household household : households) {
            String housename = household.name;
            String numberOfMembers = Integer.toString(people.size());
            percents.add(numberOfMembers);
            householdValues.add(housename);
        }
    }

    public void loadMembersIntoViewFromSpinner(){
        int pos = spinner.getSelectedItemPosition();
        String h = areaValues.get(pos);
        householdValues.clear();
        percents.clear();
        householdValues.add(h);
        percents.add("%Complete");
        people =  new Select().from(Person.class).where("family_name='" + h + "'").execute();
        for(Person person : people){
            householdValues.add(person.given_name);
            int percent = person.percent;
            String showPercent = "%" + percent;
            percents.add(showPercent);
        }
        adapter.notifyDataSetChanged();

    }

    public void loadMembersIntoView(final int position){

        final String House;
        try {
             House = householdValues.get(position);
            System.out.println(House);
             people =  new Select().from(Person.class).where("family_name='" + House + "'").execute();
        }catch (Exception e){
            return;
        }
        householdValues.clear();
        householdValues.add(House);
        percents.clear();
        percents.add("%Complete");
        for(Person person : people){
            householdValues.add(person.given_name);
            int percent = person.percent;
            String showPercent = "%" + percent;
            percents.add(showPercent);
        }
        TextView view1 = (TextView) findViewById(R.id.button);
        TextView view2 = (TextView) findViewById(R.id.button2);
                view1.setText("+ Member");
                view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int place = position;
                        createMember(House);
                    }
                });
                view2.setText("Back");
                view2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goBack();
                    }
                });
    }

    public void goBack(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void createMember(final String position){
        alert = new Dialog(this);
        alert.setContentView(R.layout.newmember);
        alert.setCancelable(false);
        Button save = (Button) alert.findViewById(R.id.button);
        Button cancel = (Button) alert.findViewById(R.id.button2);

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                alert.dismiss();
            }
        });



        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String selected = position;
                EditText name = (EditText) alert.findViewById(R.id.editText);
                EditText birthday = (EditText) alert.findViewById(R.id.editText2);
                EditText edu = (EditText) alert.findViewById(R.id.editText3);

                RadioGroup sex = (RadioGroup) alert.findViewById(R.id.sex);
                int selectedSex = sex.getCheckedRadioButtonId();



                RadioGroup living = (RadioGroup) alert.findViewById(R.id.living);
                int selectedType = living.getCheckedRadioButtonId();



                RadioGroup isinschool = (RadioGroup) alert.findViewById(R.id.inschool);
                int selectedSchool = isinschool.getCheckedRadioButtonId();


                Boolean isalive;
                Boolean inschool;
                String issex;

                if(selectedType == 0){
                    isalive = true;
                }
                else{
                    isalive = false;
                }

                if(selectedSchool == 0){
                    inschool = true;
                }
                else{
                    inschool = false;
                }

                if(selectedSex == 0){
                    issex = "Male";
                }
                else{
                    issex = "Female";
                }

                Editable newMember = name.getText();
                Editable memberbirthday = birthday.getText();
                Editable edutop = edu.getText();
                Household current = new Household();
                Person newPerson = new Person();
                newPerson.household_id = 1;
                newPerson.family_name = selected;
                System.out.println(newPerson.family_name);
                newPerson.percent = 0;
                newPerson.is_alive = isalive;
                newPerson.in_school = inschool;
                newPerson.gender = issex;
                newPerson.education_level = edutop.toString();
                newPerson.family_relationship_id = 0;
                newPerson.birthday = memberbirthday.toString();
                newPerson.given_name = newMember.toString();
                newPerson.created_at = model.generateTimestamp();
                newPerson.updated_at = model.generateTimestamp();
                newPerson.save();
                alert.dismiss();
                householdValues.clear();
                percents.clear();
                householdValues.add(selected);
                percents.add("%Complete");
                people =  new Select().from(Person.class).where("family_name='" + selected + "'").execute();
                for(Person person : people){
                    householdValues.add(person.given_name);
                    int percent = person.percent;
                    String showPercent = "%" + percent;
                    percents.add(showPercent);
                }
                adapter.notifyDataSetChanged();

            }
        });


        alert.show();


    }

    public void loadAreasIntoSpinner(){
        areaValues.clear();
        areaValues.add("Select Area");
        for(Area area : areas){
            String item = area.name;
            areaValues.add(item);
        }
    }

    public void loadHouseholdsIntoSpinner(){
        areaValues.clear();
        areaValues.add("Select Household");
        for (Household household : households){
            String item = household.name;
            areaValues.add(item);
        }
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
        loadAreasIntoSpinner();
        spinnerAdapter.notifyDataSetChanged();
        AlertDialog.Builder complete = new AlertDialog.Builder(this);
        complete.setTitle("Sync Complete");
        complete.setMessage("Data has been loaded into the database");
        complete.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            }
            });
        complete.show();
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
                        newHouse.area = Area.load(Area.class,position);
                        newHouse.name = newHousehold.toString();
                        newHouse.created_at = model.generateTimestamp();
                        newHouse.updated_at = model.generateTimestamp();
                        newHouse.save();
                loadHouseholdsIntoView(position);
                adapter.notifyDataSetChanged();
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
