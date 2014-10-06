package com.example.chad.sra_mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        // if username and password are entered then forward to MyActivity
        String username = preferences.getString("username", null);
        String password = preferences.getString("password", null);
        if(!validate(username, password)) {
            goToLogin();
        } else {
            // set up the activity
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


    public void goToLogin(){
        Intent intent0 = new Intent(this, login.class);
        startActivity(intent0);
    }

    public boolean validate(String username, String password) {
        if(username != null && password != null)
            if(!username.isEmpty() && !password.isEmpty())
                return true;

        return false;
    }

}

