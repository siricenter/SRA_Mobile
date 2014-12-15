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
import com.sra.objects.Question;
import com.sra.objects.QuestionSet;
import com.sra.objects.Region;
import com.sra.objects.loginObject;

import org.quickconnectfamily.kvkit.kv.KVStore;
import org.quickconnectfamily.kvkit.kv.KVStoreEventListener;

import java.io.Serializable;



public class login extends Activity {

    EditText usernameET;
    EditText passwordET;
    SharedPreferences.Editor editor;
    private boolean status;
    TextView textview;
    ProgressBar progress;
    int passes = 0;
    private String organization = "SRA";

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
        Firebase ref = new Firebase("https://intense-inferno-7741.firebaseio.com/Organization");
                //authenticate user using firebase.
                ref.authWithPassword(username, password, new Firebase.AuthResultHandler() {
                    //Success
                    @Override
                    public void onAuthenticated(final AuthData authData) {
                        status = true;
                        textview.setText("Authenticating....");
                        //Get reference to User Tree
                        final String Node = username.split("@")[0];
                        Firebase users = new Firebase("https://intense-inferno-7741.firebaseio.com/Users/" + Node);
                        //Start download from firebase, once
                         users.addListenerForSingleValueEvent(new ValueEventListener() {

                             //Success
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 //loop through Users
                                 System.out.println("https://intense-inferno-7741.firebaseio.com/Users/" + Node);
                                        initialDownload(dataSnapshot);
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
            System.out.println(data.getValue().toString());
            String username = data.child("Email").getValue().toString();
            DataSnapshot ld = data.child("Organizations").child(organization);
            DataSnapshot role = ld.child("Roles");
            DataSnapshot area = ld.child("Regions");

            //create loginInfo Object
            final loginObject info = new loginObject(username);
            info.setLoggedIn(true);

            textview.setText("Loading User Areas");

            for(DataSnapshot rs : area.getChildren()){
                info.addRegion(rs.getName());
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
            final Region usersRegion = new Region();

            for(final String rg : info.getRegions()) {
                textview.setText("Downloading " + rg);
                for (String ar : info.getAreaNames()) {
                    textview.setText("Downloading area " + ar);
                    //create reference to organization name
                    String urlref = "https://intense-inferno-7741.firebaseio.com/Organizations/" + organization + "/Regions/" + rg + "/Areas/" + ar;
                    Firebase areaData = new Firebase(urlref);

                    areaData.addListenerForSingleValueEvent(new ValueEventListener() {
                        //Success

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Areas area = new Areas();
                                  area.setAreaName(dataSnapshot.getName());
                                  area.setRegion(rg);
                                  area.setRef(dataSnapshot.getRef().toString());
                            DataSnapshot resources = dataSnapshot.child("Resources");
                            for(DataSnapshot household : resources.getChildren()){
                               Households households = new Households();
                                          households.setHouseholdName(household.getName());
                               for(DataSnapshot members : household.child("Members").getChildren() ){
                                   households.addMember(members.getName());
                               }
                               for(DataSnapshot interviews : household.child("Interviews").getChildren()){
                                   Interviews interview = new Interviews();
                                              interview.setCreatedDate(interviews.child("Date Created").getValue().toString());
                                   for(DataSnapshot qs : interviews.child("Question Sets").getChildren()){
                                       QuestionSet questionSet = new QuestionSet(qs.child("Name").getValue().toString(),qs.getRef().toString());
                                       for(DataSnapshot q: qs.child("Questions").getChildren()){
                                           Question questions = new Question(q.getRef().toString());
                                                     questions.setName(q.child("Name").getValue().toString());
                                                      questions.setMultiUse(true);
                                           for (DataSnapshot datapoints : q.child("Data Points").getChildren()) {
                                               Datapoint newDatapoint = new Datapoint();
                                               newDatapoint.addAnswer(datapoints.child("Answer").getValue().toString());
                                               newDatapoint.setDataType(datapoints.child("Type").getValue().toString());
                                               newDatapoint.setLabel(datapoints.child("Label").getValue().toString());
                                               questions.addDataPoint(newDatapoint);
                                           }
                                           questionSet.addQuestion(questions);
                                       }
                                       interview.addQuestionSets(questionSet);
                                   }
                                   households.addInterview(interview);
                               }
                               area.addHousehold(households);
                            }
                            usersRegion.addArea(area);
                            passes++;
                            if(passes == info.getRegions().size()){
                                try {
                                    KVStore.removeValue("Field");
                                    KVStore.storeValue("Field", usersRegion);
                                    Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                                    startActivity(intent);
                                } catch (Exception e) {
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            textview.setText(firebaseError.getMessage());
                        }
                    });
                }
            }
        }
    }

