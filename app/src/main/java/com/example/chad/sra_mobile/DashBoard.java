package com.example.chad.sra_mobile;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.sra.objects.Areas;
import com.sra.objects.Households;

import java.util.ArrayList;
import java.util.List;

import DataSync.DataSync;


public class DashBoard extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);




    }

    public void areaWasSelected() {

    }

    public void createArea(MenuItem item){
    }

    public void goToInterview(){
    }

    public void addButton(){
    }

    public void clearHouseholdsFromView() {
    }

    public void loadHouseholdsIntoView(){
    }
    public void loadMembersIntoView(){
    }

    public void goBack(MenuItem item){

    }
    public void createMember(MenuItem item){
    }
    public void loadAreasIntoSpinner(){
    }


    public void syncDatabase(MenuItem item){
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



    public void addHousehold(MenuItem item){

    }

    public void goToQuestionGen(MenuItem item){
        Intent intent = new Intent(this,QuestionSetList.class);
        startActivity(intent);
    }

}
