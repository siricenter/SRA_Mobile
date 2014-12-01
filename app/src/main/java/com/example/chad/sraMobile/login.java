package com.example.chad.sraMobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

        //Set the activity for the firebase
        Firebase.setAndroidContext(this);

        //Retrieve the UI Elements from xml
        usernameET = (EditText) findViewById(R.id.usernameET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        Button loginButton = (Button) findViewById(R.id.loginButton);

        //set onlcick for button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        //set the application for KVKit
        KVStore.setActivity(getApplication());
        KVStore.setInMemoryStorageCount(10);

        //Listenter for KVKit
        KVStoreEventListener listener = new KVStoreEventListener() {
            @Override
            public void errorHappened(String key, Serializable value, Exception e) {
                System.out.println(key + " " + value + " " + e.getLocalizedMessage());
            }

            @Override
            public boolean shouldStore(String key, Serializable value) {
                return true;
            }

            @Override
            public void willStore(String key, Serializable value) {
                System.out.println(key + " " + value + "Trying to Save");
            }

            @Override
            public void didStore(String key, Serializable value) {
                System.out.println(key + " " + value + "Saved");
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

        //set the listener
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
        //Store the text from edittexts
        final String username = usernameET.getText().toString();
        final String password = passwordET.getText().toString();

        //Unhide the progress wheel and text
            progress = (ProgressBar)findViewById(R.id.progressBar);
            progress.setVisibility(View.VISIBLE);
            textview = (TextView)findViewById(R.id.textView3);
            textview.setVisibility(View.VISIBLE);
            textview.setText("Logging In....");

        //Start a reference to the base firebase
        Firebase ref = new Firebase("https://intense-inferno-7741.firebaseio.com");
                //authenticate user using firebase.
                ref.authWithPassword(username, password, new Firebase.AuthResultHandler() {
                    //Success
                    @Override
                    public void onAuthenticated(final AuthData authData) {
                        status = true;
                        textview.setText("Authenticating....");
                        //Get referance to User Tree
                        Firebase users = new Firebase("https://intense-inferno-7741.firebaseio.com/Users");
                        //Start download from firebase, once
                         users.addListenerForSingleValueEvent(new ValueEventListener() {

                             //Success
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 //loop through Users
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

                             //Fail
                             @Override
                             public void onCancelled(FirebaseError firebaseError) {
                                textview.setText(firebaseError.getMessage());
                             }
                         });
                    }

                    //Fail
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        //Hide progress wheel
                        progress.setVisibility(View.INVISIBLE);
                        //display Error
                        textview.setText(firebaseError.getMessage());
                        status = false;
                    }
                });
            }


        public void initialDownload(final DataSnapshot data){
            //Show status
            textview.setText("Saving User For Offline Use");

            // storing user data
            String username = data.child("Email").getValue().toString();
            DataSnapshot ld = data.child("Organizations").child("Organization");
            DataSnapshot role = ld.child("Roles");
            DataSnapshot area = ld.child("Region");



            //create loginInfo Object
            final loginObject info = new loginObject(username);
            info.setLoggedIn(true);
            textview.setText("Loading User Areas");
            for(DataSnapshot rs : area.getChildren()){
                for(DataSnapshot as : rs.child("Areas").getChildren()){
                    String areaName = as.child("Name").getValue().toString();
                    System.out.println(areaName);
                    info.addToAreas(areaName);
                }
            }

            //add roles to loginInfo
            for(DataSnapshot roles:role.getChildren()){
                String Roles = roles.getValue().toString();
                info.addToRoles(Roles);
            }



            //Store User data into the file system
            try{
                KVStore.storeValue("User",info);
            }
            catch (Exception e){

            }

            //set status to download

            textview.setText("Downloading Region");

            //get the name of Organizations
            for(DataSnapshot rf : ld.child("Region").getChildren()){
                place = rf.child("Name").getValue().toString();
            }

            //get the organization name
            final String orgName = ld.child("Name").getValue().toString();

            //create reference to organization name
            String urlref = "https://intense-inferno-7741.firebaseio.com/Organizations/";
            Firebase areaData = new Firebase(urlref);


            areaData.addListenerForSingleValueEvent(new ValueEventListener() {
                //Success
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     //Start loop from the Top getting the correct organization
                     Region usersRegion = new Region();
                     for(DataSnapshot org :dataSnapshot.getChildren()){
                         String orgsName = org.child("Name").getValue().toString();

                         //When find a match
                         if(orgsName.equals(orgName)){

                             //loops through the regions
                             for(DataSnapshot data :org.child("Regions").getChildren()){

                                 String regionName = data.child("Name").getValue().toString();

                                 //When find a region match
                                 if(regionName.equals(place)){

                                     //creates a region object

                                     //store the region name
                                     usersRegion.setRegionName(data.child("Name").getValue().toString());
                                     textview.setText("Finished Downloading " + data.child("Name").getValue().toString());
                                      System.out.println(data.getValue().toString());
                                     for(DataSnapshot areas : data.child("Areas").getChildren()) {

                                         String name = areas.child("Name").getValue().toString();
                                         System.out.println(name);
                                         textview.setText("Loading " + name);
                                         ArrayList<String> ars = info.getAreaNames();
                                         System.out.println(ars);
                                         for (String items : ars) {

                                             if (items.equals(name)) {

                                                 Areas area = new Areas();
                                                 area.setRef(areas.getRef().toString());
                                                 area.setAreaName(name);
                                                 long h = 1;
                                                 for (DataSnapshot houses : areas.child("Resources").getChildren()) {
                                                     String hName = houses.child("Name").getValue().toString();
                                                     Households household = new Households();
                                                     household.setHouseholdName(hName);
                                                     household.setId(h);
                                                     for (DataSnapshot members : houses.child("Members").getChildren()) {
                                                         household.addMember(members.child("Name").getValue().toString());
                                                     }
                                                     for (DataSnapshot interview : houses.child("Interviews").getChildren()) {
                                                         Interviews newInterview = new Interviews();
                                                         newInterview.setCreatedDate(interview.child("Date Created").getValue().toString());
                                                         for (DataSnapshot questionSets : interview.child("QuestionSets").getChildren()) {
                                                             QuestionSet qSet = new QuestionSet(questionSets.child("Name").getValue().toString(), questionSets.getRef().toString());
                                                             for (DataSnapshot questions : questionSets.child("Questions").getChildren()) {
                                                                 Questions question = new Questions(questions.getRef().toString());
                                                                 for (DataSnapshot datapoints : questions.child("Data Points").getChildren()) {
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
                                                 System.out.println("Food " + area);
                                                 usersRegion.addArea(area);
                                             }
                                         }
                                     }
                                 }
                             }
                         }
                     }
                     try{
                         KVStore.storeValue("Field",usersRegion);
                         Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                         startActivity(intent);
                     } catch(Exception e){}
                 }

                 @Override
                 public void onCancelled(FirebaseError firebaseError) {
                    textview.setText(firebaseError.getMessage());
                 }
            });

        }
    }

