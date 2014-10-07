package com.example.chad.sra_mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;



public class login extends Activity {

    EditText usernameET;
    EditText passwordET;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editor = getSharedPreferences("login", MODE_PRIVATE).edit();
        usernameET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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


    public void login() {
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        if(validate(username, password)) {
            editor.putString("username", username.trim());
            editor.putString("password", password.trim());
            editor.commit();
            // forward to MyActivity
            Intent intent = new Intent(this, MyActivity.class);
            startActivity(intent);
        }
    }

    public boolean validate(String username, String password) {
        Log.d("Login : validate", "username==" + username + " password==" + password);
        if(username != null && password != null)
            if(!username.isEmpty() && !password.isEmpty())
                return true;
        return false;
    }
}
