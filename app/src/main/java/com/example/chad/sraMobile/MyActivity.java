package com.example.chad.sraMobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sra.helperClasses.CRUDFlinger;
import com.sra.objects.loginObject;

public class MyActivity extends Activity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        CRUDFlinger.setApplication(getApplication());
        loginObject login = CRUDFlinger.load("User",loginObject.class);
        if(login.isLoggedIn()){
            goToDashboard();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

    public void goToLogin(View v) { goToLogin(); }

    public void goToLogin(){
        Intent intent0 = new Intent(this, login.class);
        startActivity(intent0);
    }

    private void goToDashboard(View v) { goToDashboard(); }

    public void goToDashboard(){
        Intent intent0 = new Intent(this, DashBoard.class);
        startActivity(intent0);
    }


}

