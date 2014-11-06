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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import DataSync.DataSync;
import LocalDatabase.Area;
import LocalDatabase.DatabasePopulator;
import LocalDatabase.Household;
import LocalDatabase.Person;

public class DashBoard extends Activity {

    public static final int STATE_AREA_SELECT = 0;
    public static final int STATE_INDIVIDUAL_HOUSEHOLD = 1;

    ListView listView;
    Spinner spinner;
    ArrayList<String> areaValues;
    Area area;

    private static DataSync dataSync = DataSync.getInstance();

    Household household;
    Person person;
    List<Area> areas;
    List<Household> households;
    List<Person> people;
    ArrayAdapter<String> spinnerAdapter;
    customList adapter;
    ArrayList<String> householdValues;
    ArrayList<String> percents;
    ArrayList<String> householdId;
    int navigationMarker;
    int flag;
    int numberOfMembers;
    int currentHousehold;
    int currentArea;
    Menu getMenu;
    Dialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        householdValues = new ArrayList<String>();
        areaValues = new ArrayList<String>();
        percents = new ArrayList<String>();
        householdId = new ArrayList<String>();
        person = new Person();
        areas = area.getAllAreas();

        listView = (ListView) findViewById(R.id.Interviews);
        spinner = (Spinner) findViewById(R.id.areaSpinner);

        flag = 0;

        areaValues.add("Select Area");
        householdValues.add("Households");
        percents.add("%Complete");

        for(Area area : areas) {
            areaValues.add(area.name);
        }

        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, areaValues);
        adapter = new customList(DashBoard.this,householdValues,percents);

        listView.setAdapter(adapter);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                areaWasSelected(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (position != 0) {
                if (navigationMarker == STATE_AREA_SELECT){
                    if (flag == 0) {

                        currentHousehold = Integer.parseInt(householdId.get(position));
                        Person p = new Person();

                        List<Person> getpeople = p.getMembers(currentHousehold);
                        numberOfMembers = getpeople.size();
                        System.out.println(numberOfMembers);
                        loadMembersIntoView();
                        addButton();
                        flag = 1;
                    }
                    navigationMarker = STATE_INDIVIDUAL_HOUSEHOLD;
                }
                else{
                }
            }
             adapter.notifyDataSetChanged();
         }

         });
    }

    public void areaWasSelected(int areaPosition) {
        if (areaPosition != 0) {
            if (navigationMarker == STATE_AREA_SELECT) {
                loadHouseholdsIntoView(areaPosition);
            }
        }
        else {
            getMenu.findItem(R.id.add_family).setEnabled(false);
            getMenu.findItem(R.id.add_area).setEnabled(true);
            clearHouseholdsFromView();
        }
        adapter.notifyDataSetChanged();
    }

    public void createArea(MenuItem item){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Name of new Area?");
        alert.setMessage("Create a new Area");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Editable newAreaName = input.getText();
                Area newArea = new Area();
                newArea.name = newAreaName.toString();
                newArea.post();
                loadAreasIntoSpinner();
                updateSpinner();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }

    public void goToInterview(){
        final Intent intent = new Intent(this, InterviewActivity.class);
        System.out.println(currentHousehold);
        intent.putExtra("household", currentHousehold);
        startActivity(intent);
    }

    public void addButton(){
        //the layout on which you are working

        LinearLayout layout = (LinearLayout) findViewById(R.id.dashboard);
        Spinner spin =(Spinner) findViewById(R.id.areaSpinner);
        layout.removeView(spin);
        //set the properties for button
        Button btnTag = new Button(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnTag.setLayoutParams(new ViewGroup.LayoutParams(params));
        btnTag.setText("Interview");
        btnTag.setTextSize(12);
        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToInterview();
            }
        });

        //add button to the layout
        layout.addView(btnTag,0);
    }

    public void clearHouseholdsFromView() {
        householdValues.clear();
        percents.clear();
        householdId.clear();
        householdValues.add("Households");
        percents.add("%Complete");
        householdId.add("0");
    }

    public void loadHouseholdsIntoView(int position){

        getMenu.findItem(R.id.add_family).setEnabled(true);
        getMenu.findItem(R.id.add_area).setEnabled(false);
        currentArea = position;
        households = household.getHousehold(position);
        clearHouseholdsFromView();

        for (Household household : households) {
            String housename = household.name;
            householdId.add("" + household.getId());
            percents.add("%" + household.percent);
            householdValues.add(housename);
        }
    }
    public void loadMembersIntoView(){
        getMenu.findItem(R.id.add_member).setEnabled(true);
        getMenu.findItem(R.id.add_family).setEnabled(false);
        final String House;
        try {
             Household h = household.load(Household.class,(long)currentHousehold);
             House = h.name;
             System.out.println(House);
             people = person.getMembers(h.getId());
        }catch (Exception e){
            return;
        }
        householdValues.clear();
        householdValues.add(House);
        percents.clear();
        percents.add(numberOfMembers + " Members");
        for(Person person : people){
            householdValues.add(person.given_name);
            percents.add("");
        }
    }

    public void goBack(MenuItem item){
        System.out.println(navigationMarker);
        if (navigationMarker == STATE_INDIVIDUAL_HOUSEHOLD) {
            Intent intent = getIntent();
            intent.putExtra("AreaID", currentArea);
            startActivity(intent);
            finish();
        }
    }
    public void createMember(MenuItem item){
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
                String selected = household.load(Household.class,currentHousehold).name;
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
                Person newPerson = new Person();
                newPerson.household_id = currentHousehold;
                newPerson.family_name = selected;
                System.out.println(newPerson.family_name);
                newPerson.is_alive = isalive;
                newPerson.in_school = inschool;
                newPerson.gender = issex;
                newPerson.education_level = edutop.toString();
                newPerson.family_relationship_id = 0;
                newPerson.birthday = memberbirthday.toString();
                newPerson.given_name = newMember.toString();
                newPerson.post();
                alert.dismiss();
                householdValues.clear();
                percents.clear();
                people =  person.getMembers(currentHousehold);
                numberOfMembers = people.size();
                householdValues.add(selected);
                percents.add( numberOfMembers + " Members");

                for(Person person : people){
                    householdValues.add(person.given_name);
                    percents.add("");
                }
                adapter.notifyDataSetChanged();
             }
        });


        alert.show();


    }
    public void loadAreasIntoSpinner(){
        areaValues.clear();
        areaValues.add("Select Area");
        areas = area.getAllAreas();
        for(Area area : areas){
            String item = area.name;
            areaValues.add(item);
        }
        spinnerAdapter.notifyDataSetChanged();
    }
    public void updateSpinner(){
        spinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        getMenu = menu;
        getMenu.findItem(R.id.add_family).setEnabled(false);
        getMenu.findItem(R.id.add_member).setEnabled(false);

        // Check to see if an AreaID is given in the Intent.
        // If so we need to load the households from that area.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("AreaID")) {
                int areaID = extras.getInt("AreaID");
                if (areaID != -1) {
                    spinner.setSelection(areaID);
                    areaWasSelected(areaID);
                }
            }
        }

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

    public void syncDatabase(MenuItem item){
        DatabasePopulator create = new DatabasePopulator();
        create.populate();

        // this is a test of the DataSync class, real usage will be similar
        dataSync.sync(); // performs a full database sync

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

    public void addHousehold(MenuItem item){
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
                newHouse.post();
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

    public void goToQuestionGen(MenuItem item){
        Intent intent = new Intent(this,QuestionSetList.class);
        startActivity(intent);
    }

}
