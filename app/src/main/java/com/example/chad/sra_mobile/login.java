package com.example.chad.sra_mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.sra.objects.Areas;
import com.sra.objects.Datapoint;
import com.sra.objects.Households;
import com.sra.objects.Interviews;
import com.sra.objects.QuestionSet;
import com.sra.objects.Questions;
import com.sra.objects.Region;
import com.sra.objects.loginObject;

import org.quickconnectfamily.kvkit.kv.KVStore;
import org.quickconnectfamily.kvkit.kv.KVStoreEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;


public class login extends Activity {

    EditText usernameET;
    EditText passwordET;
    SharedPreferences.Editor editor;
    private boolean status;
    TextView textview;
    ProgressBar progress;
    String place;

    public boolean getStatus(){
        return status;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
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

        KVStore.setActivity(getApplication());
        KVStore.setInMemoryStorageCount(10);
        KVStoreEventListener listener = new KVStoreEventListener() {
            @Override
            public void errorHappened(String key, Serializable value, Exception e) {

            }

            @Override
            public boolean shouldStore(String key, Serializable value) {
                return false;
            }

            @Override
            public void willStore(String key, Serializable value) {

            }

            @Override
            public void didStore(String key, Serializable value) {

            }

            @Override
            public boolean shouldDelete(String key) {
                return false;
            }

            @Override
            public void willDelete(String key) {

            }

            @Override
            public void didDelete(String key) {

            }
        };
        KVStore.setStoreEventListener(listener);

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
        final String username = usernameET.getText().toString();
        final String password = passwordET.getText().toString();
            progress = (ProgressBar)findViewById(R.id.progressBar);
            progress.setVisibility(View.VISIBLE);
            textview = (TextView)findViewById(R.id.textView3);
            textview.setVisibility(View.VISIBLE);
            textview.setText("Logging In....");

        Firebase ref = new Firebase("https://intense-inferno-7741.firebaseio.com");

                ref.authWithPassword(username, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(final AuthData authData) {
                        status = true;
                        textview.setText("Authenticating....");
                        Firebase users = new Firebase("https://intense-inferno-7741.firebaseio.com/Users");
                         users.addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 System.out.println("Flag");
                                for(DataSnapshot data: dataSnapshot.getChildren()){
                                    String currentLogged = data.child("Email").getValue().toString();
                                           currentLogged = currentLogged.toLowerCase();

                                    Map map = authData.getProviderData();
                                    String sanUsername = map.get("email").toString();
                                           sanUsername = sanUsername.toLowerCase();
                                    if(currentLogged.equals(sanUsername)){
                                        textview.setText("Authentication Complete, Starting Initial Sync");
                                        initialDownload(data);
                                    }
                                }

                             }

                             @Override
                             public void onCancelled(FirebaseError firebaseError) {
                                textview.setText(firebaseError.getMessage());
                             }
                         });
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        progress.setVisibility(View.INVISIBLE);
                        textview.setText(firebaseError.getMessage());
                        status = false;
                    }
                });
            }


        public void initialDownload(final DataSnapshot data){
            textview.setText("Saving User For Offline Use");
            String username = data.child("Email").getValue().toString();
            DataSnapshot ld = data.child("Organizations").child("Organization");


            DataSnapshot role = ld.child("Roles");

            loginObject info = new loginObject(username);
            for(DataSnapshot roles:role.getChildren()){
                String Roles = roles.getValue().toString();
                info.addToRoles(Roles);
            }
            try{
                KVStore.storeValue("User",info);
            }
            catch (Exception e){

            }

            textview.setText("Downloading Region");
            for(DataSnapshot rf : ld.child("Region").getChildren()){
                place = rf.child("Name").getValue().toString();
            }
            final String orgName = ld.child("Name").getValue().toString();
            String urlref = "https://intense-inferno-7741.firebaseio.com/Organizations/";
            Firebase areaData = new Firebase(urlref);
            System.out.println(urlref);
            areaData.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     for(DataSnapshot org :dataSnapshot.getChildren()){
                         String orgsName = org.child("Name").getValue().toString();
                         System.out.println(orgsName + " " + orgName);
                         if(orgsName.equals(orgName)){
                             for(DataSnapshot data :org.child("Regions").getChildren()){
                                 String regionName = data.child("Name").getValue().toString();
                                 System.out.println(place + " " + regionName);
                                 if(regionName.equals(place)){
                                     Region usersRegion = new Region();
                                     usersRegion.setRegionName(data.child("Name").getValue().toString());
                                     textview.setText("Finished Downloading " + data.child("Name").getValue().toString());

                                     for(DataSnapshot areas : data.child("Areas").getChildren()){
                                         String name = areas.child("Name").getValue().toString();
                                         textview.setText("Loading " + name);
                                         Areas area = new Areas();
                                         area.setAreaName(name);
                                         long h = 1;
                                         for(DataSnapshot houses : areas.child("Resources").getChildren()){
                                            String hName = houses.child("Name").getValue().toString();
                                             Households household = new Households();
                                                        household.setHouseholdName(hName);
                                                        household.setId(h);
                                             for(DataSnapshot members:houses.child("Members").getChildren()){
                                                household.addMember(members.child("Name").getValue().toString());
                                             }
                                             for(DataSnapshot interview : houses.child("Interviews").getChildren()){
                                                Interviews newInterview = new Interviews();
                                                           newInterview.setCreatedDate(interview.child("Date Created").getValue().toString());
                                                for(DataSnapshot questionSets:interview.child("QuestionSets").getChildren()){
                                                    QuestionSet qSet = new QuestionSet(questionSets.child("Name").getValue().toString(),questionSets.getRef().toString());
                                                        for(DataSnapshot questions:questionSets.child("Questions").getChildren() ){
                                                            Questions question = new Questions(questions.getRef().toString());
                                                            for(DataSnapshot datapoints:questions.child("Data Points").getChildren()){
                                                                Datapoint newDatapoint = new Datapoint();
                                                                newDatapoint.setAnswer(datapoints.child("Answer").getValue().toString());
                                                                newDatapoint.setDataType(datapoints.child("Type").getValue().toString());
                                                                newDatapoint.setLabel(datapoints.child("Label").getValue().toString());
                                                                question.addDataPoint(newDatapoint);
                                                            }
                                                            qSet.addQuestion(question);
                                                        }
                                                    newInterview.addQuestionSets(qSet);
                                                }
                                                household.addInterview(newInterview);
                                             }
                                             h++;
                                         }
                                         usersRegion.addArea(area);
                                     }
                                 }
                             }
                         }
                     }
//                     Intent intent = new Intent(getApplicationContext(), DashBoard.class);
//                     startActivity(intent);
                 }

                 @Override
                 public void onCancelled(FirebaseError firebaseError) {
                    textview.setText(firebaseError.getMessage());
                 }
            });

        }
    }

