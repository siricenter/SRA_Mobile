package com.sra.helperClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sra.objects.Areas;
import com.sra.objects.Datapoint;
import com.sra.objects.Households;
import com.sra.objects.Interviews;
import com.sra.objects.Question;
import com.sra.objects.QuestionSet;
import com.sra.objects.Region;
import com.sra.objects.loginObject;

import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONUtilities;
import org.quickconnectfamily.kvkit.kv.KVStore;

/**
 * Created by jakobhartman on 12/10/14.
 */
public class SyncDownlaod  {
    Activity activity;
    Context context;
    Toast toast;
    int passes;
    private  Region region;
    loginObject user;
    static String organization = "SRA";

    public SyncDownlaod(Activity activity){
        this.activity = activity;
    }

    public void beginSync() {
        context = activity.getBaseContext();
        CharSequence text = "Beginning Download";
        int duration = Toast.LENGTH_SHORT;

        toast = Toast.makeText(context, text, duration);
        toast.show();

        //Start a reference to the base firebase
        Firebase.setAndroidContext(activity);
        final Firebase ref = new Firebase("https://intense-inferno-7741.firebaseio.com/Organization");
        //authenticate user using firebase.
        try{
            String json = JSONUtilities.stringify(KVStore.getValue("User"));
            Gson gson = new GsonBuilder().create();
            user = gson.fromJson(json,loginObject.class);
        }catch (JSONException e){

        }


        final

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Enter Password for " + user.getUsername());

        // Set up the input
        final EditText input = new EditText(activity);
        input.setHint("Password");
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_MASK_VARIATION);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = input.getText().toString();


                final String username = user.getUsername().toLowerCase();
                toast.show();

                ref.authWithPassword(username, password, new Firebase.AuthResultHandler() {
                    //Success
                    @Override
                    public void onAuthenticated(final AuthData authData) {

                        toast.setText("Authenticating....");
                        toast.show();

                        //Get reference to User Tree
                        final String Node = username.split("@")[0];
                        Firebase users = new Firebase("https://intense-inferno-7741.firebaseio.com/Users/" + Node);
                        //Start download from firebase, once
                        users.addListenerForSingleValueEvent(new ValueEventListener() {

                            //Success
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //loop through Users
                                initialDownload(dataSnapshot);
                            }

                            //Fail
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                toast.setText(firebaseError.getMessage());
                                toast.show();
                            }
                        });
                    }

                    //Fail
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        //Hide progress wheel
                        //display Error
                        toast.setText(firebaseError.getMessage());
                        toast.show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public void initialDownload(final DataSnapshot data){
        //Show status
        toast.setText("Saving User For Offline Use");
        toast.show();

        // storing user data
        System.out.println(data.getValue().toString());
        String username = data.child("Email").getValue().toString();
        DataSnapshot ld = data.child("Organizations").child(organization);
        DataSnapshot role = ld.child("Roles");
        DataSnapshot area = ld.child("Regions");

        //create loginInfo Object
        final loginObject info = new loginObject(username);
        info.setLoggedIn(true);

        toast.setText("Loading User Areas");
        toast.show();

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
            KVStore.storeValue("User", info);
        }
        catch (Exception e){

        }

        //set status to download
        toast.setText("Downloading Region");
        toast.show();
        final Region usersRegion = new Region();

        for(final String rg : info.getRegions()) {
            toast.setText("Downloading " + rg);
            toast.show();
            for (String ar : info.getAreaNames()) {
                toast.setText("Downloading area " + ar);
                toast.show();
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
                                            for(DataSnapshot answer :datapoints.child("Answers").getChildren())
                                            {
                                                newDatapoint.addAnswer(answer.child("Value").getValue().toString());
                                            }
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
                            region = usersRegion;
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        toast.setText(firebaseError.getMessage());
                        toast.show();
                    }
                });
            }
        }
    }

    public Region getRegion() {
        return region;
    }
}
