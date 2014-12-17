package com.sra.sliderFragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.chad.sraMobile.DashBoard;
import com.example.chad.sraMobile.InterviewActivity;
import com.example.chad.sraMobile.R;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sra.listViewSlider.ItemAdapter;
import com.sra.listViewSlider.ItemRow;
import com.sra.objects.Areas;
import com.sra.objects.DeleteRecord;
import com.sra.objects.Households;
import com.sra.objects.Member;
import com.sra.objects.Region;
import com.sra.objects.loginObject;

import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONUtilities;

import java.util.ArrayList;
import java.util.List;

public class FragmentOne extends Fragment {

    private ArrayList<String> areasList;
    public Region regions;
    public SwipeListView swipelistview;
    public ItemAdapter adapter;
    List<ItemRow> itemData;
    public int currentArea;
    public int currentHousehold;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    View view;
    public DeleteRecord markedForDeletion;
    public String navigationPosition;
    private float buttonWidth;

    public FragmentOne() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prefs = getActivity().getApplication().getSharedPreferences("AppPrefs", getActivity().getApplication().MODE_PRIVATE);
        editor = getActivity().getApplication().getSharedPreferences("AppPrefs",getActivity().getApplication().MODE_PRIVATE).edit();

        buttonWidth = 0;
        navigationPosition = "areas";
        areasList = new ArrayList<String>();
        markedForDeletion = new DeleteRecord();

        view = inflater.inflate(R.layout.button1slide, container,false);
        // listView = (ListView) view.findViewById(R.id.ListView01);
        swipelistview = (SwipeListView)view.findViewById(R.id.example_swipe_lv_list);
        itemData = new ArrayList<ItemRow>();

        adapter=new ItemAdapter(getActivity(),R.layout.custom_row,itemData,FragmentOne.this);

        Button button = (Button)view.findViewById(R.id.button3);
               button.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       addArea();
                   }
               });

        buildRegion();
        setListener();


        return view;
    }
    public void setListener(){

        WindowManager wm = (WindowManager) getActivity().getBaseContext().getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;




        //These are the swipe listview settings. you can change these
        //setting as your requrement
        swipelistview.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT); // there are five swiping modes
        swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL); //there are four swipe actions
        swipelistview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);

        swipelistview.setOffsetRight(convertDpToPixel(0f)); // right side offset
        swipelistview.setAnimationTime(60); // animarion time
        swipelistview.setSwipeOpenOnLongPress(true); // enable or disable SwipeOpenOnLongPress


        swipelistview.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
                swipelistview.closeOpenedItems();
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(final int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));

                //swipelistview.openAnimate(position); //when you touch front view it will open
                navigationPosition = "households";
                currentArea = position;
                ArrayList<Areas> areas = regions.getAreas();
                ArrayList<Households> households = areas.get(position).getHouseholds();
                itemData.clear();
                for(Households houses : households){
                    itemData.add(new ItemRow(houses.getHouseholdName()));
                }

                Button button = (Button)view.findViewById(R.id.button3);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addHousehold();
                    }
                });
                button.setText("Add Household");

                adapter.notifyDataSetChanged();
                changeListener(position);

            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));

                swipelistview.closeAnimate(position);//when you touch back view it will close
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {

            }



        });

        swipelistview.setAdapter(adapter);

        for(String item : areasList)
        {
            itemData.add(new ItemRow(item));
        }

        adapter.notifyDataSetChanged();

        ItemAdapter itemAdapter = (ItemAdapter) swipelistview.getAdapter();
        System.out.println(itemAdapter);
        View child = itemAdapter.getView(0,null,swipelistview);
        child.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        buttonWidth = child.getMeasuredWidth();
        System.out.println(buttonWidth);
        float space = (float) ((width - convertDpToPixel(buttonWidth)));
        swipelistview.setOffsetLeft(space); // left side offset

    }

    public void changeListener(final int i){
        swipelistview.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }


            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));

                //swipelistview.openAnimate(position); //when you touch front view it will open
                navigationPosition = "members";
                currentHousehold = position;
                ArrayList<Areas> areas = regions.getAreas();
                ArrayList<String> members = areas.get(i).getHouseholds().get(position).getMembers();

                itemData.clear();
                for(String member : members){

                    itemData.add(new ItemRow(member));
                }

                Button button = (Button)view.findViewById(R.id.button3);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addMember();
                    }
                });
                       button.setText("Add Member");

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));

                swipelistview.closeAnimate(position);//when you touch back view it will close
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {

            }
        });
    }

    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    public void buildRegion(){

            String json = prefs.getString("Region",null);
            System.out.println("Loading Areas");
            System.out.println(json);
            Gson gson = new GsonBuilder().create();
            regions = gson.fromJson(json,Region.class);
            ArrayList<Areas> areas = regions.getAreas();
            for(Areas area : areas){
               areasList.add(area.getAreaName());
            }
    }

    public void addArea(){
        final Areas area = new Areas();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter The Area Name");
        LinearLayout layout = new LinearLayout(getActivity());


        // Set up the input
        final EditText input = new EditText(getActivity());
        final EditText inputRegion = new EditText(getActivity());

        input.setHint("Name");
        inputRegion.setHint("Region Name");
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        inputRegion.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input);
        layout.addView(inputRegion);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                area.setAreaName(input.getText().toString());

                area.setRef("https://intense-inferno-7741.firebaseio.com/Organizations/SRA/Regions/" + inputRegion.getText().toString() + "/Areas/" + area.getAreaName() + "/");
                try{
                    String json = prefs.getString("User",null);
                    Gson gson = new GsonBuilder().create();
                    loginObject login = gson.fromJson(json,loginObject.class);
                                login.addToAreas(input.getText().toString());
                    editor.putString("User", JSONUtilities.stringify(login));

                }catch (JSONException e){

                }
                regions.addArea(area);
                reloadAreasIntoView();
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

    public void addHousehold(){
        final Households household = new Households();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter The Household Name");
        LinearLayout layout = new LinearLayout(getActivity());


        // Set up the input
        final EditText input = new EditText(getActivity());
        final EditText inputMember = new EditText(getActivity());

        input.setHint("Name");
        inputMember.setHint("Name of Head of household");
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        inputMember.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input);
        layout.addView(inputMember);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                household.setHouseholdName(input.getText().toString());
                household.addMember(inputMember.getText().toString());
                regions.getAreas().get(currentArea).addHousehold(household);
                reloadHouseholdsIntoView();
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

    public void addMember(){


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter The Member's Name");

        // Set up the input
        final EditText input = new EditText(getActivity());
        input.setHint("Name");
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                regions.getAreas().get(currentArea).getHouseholds().get(currentHousehold).addMember(input.getText().toString());
                reloadMembersIntoView();
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

    public void reloadMembersIntoView(){
        ArrayList<String> members = regions.getAreas().get(currentArea).getHouseholds().get(currentHousehold).getMembers();
        itemData.clear();
        for(String member : members){
            itemData.add(new ItemRow(member));
        }
        adapter.notifyDataSetChanged();
        try{
            editor.putString("Region",JSONUtilities.stringify(regions));
        }
        catch (JSONException e){

        }
    }

    public void loadHouseholdsIntoView(){
        ArrayList<Households> households = regions.getAreas().get(currentArea).getHouseholds();
        itemData.clear();
        for(Households houses : households){
            itemData.add(new ItemRow(houses.getHouseholdName()));
        }
        adapter.notifyDataSetChanged();
    }

    public void loadAreasIntoView(){
        ArrayList<Areas> areas = regions.getAreas();
        areasList.clear();
        itemData.clear();
        for(Areas area : areas){
            itemData.add( new ItemRow(area.getAreaName()));
        }
        adapter.notifyDataSetChanged();
    }

    public void reloadHouseholdsIntoView(){
        ArrayList<Households> households = regions.getAreas().get(currentArea).getHouseholds();
        itemData.clear();
        for(Households houses : households){
            itemData.add(new ItemRow(houses.getHouseholdName()));
        }
        adapter.notifyDataSetChanged();
        try{
            editor.putString("Region",JSONUtilities.stringify(regions));
        }catch (JSONException e){}
    }

    public void reloadAreasIntoView(){
        ArrayList<Areas> areas = regions.getAreas();
        areasList.clear();
        itemData.clear();
        for(Areas area : areas){
            itemData.add( new ItemRow(area.getAreaName()));
        }
        adapter.notifyDataSetChanged();
        try{
            editor.putString("Region",JSONUtilities.stringify(regions));
        }catch (JSONException e){}
    }

    public void setAreaListeners(){
        Button button = (Button)view.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArea();
            }
        });
        button.setText("Add Area");
        navigationPosition = "areas";

        swipelistview.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
                swipelistview.closeOpenedItems();
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(final int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));

                //swipelistview.openAnimate(position); //when you touch front view it will open
                navigationPosition = "households";
                currentArea = position;
                ArrayList<Areas> areas = regions.getAreas();
                ArrayList<Households> households = areas.get(position).getHouseholds();
                itemData.clear();
                for(Households houses : households){
                    itemData.add(new ItemRow(houses.getHouseholdName()));
                }

                Button button = (Button)view.findViewById(R.id.button3);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addHousehold();
                    }
                });
                button.setText("Add Household");

                adapter.notifyDataSetChanged();
                changeListener(position);

            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));

                swipelistview.closeAnimate(position);//when you touch back view it will close
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {

            }



        });
    }

    public void setHouseholdListeners(){
        Button button = (Button)view.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArea();
            }
        });
        button.setText("Add Household");
        navigationPosition = "households";

        swipelistview.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }


            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));

                //swipelistview.openAnimate(position); //when you touch front view it will open
                navigationPosition = "members";
                currentHousehold = position;
                ArrayList<Areas> areas = regions.getAreas();
                ArrayList<String> members = areas.get(currentArea).getHouseholds().get(position).getMembers();

                itemData.clear();
                for(String member : members){
                    itemData.add(new ItemRow(member));
                }

                Button button = (Button)view.findViewById(R.id.button3);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addMember();
                    }
                });
                button.setText("Add Member");

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));

                swipelistview.closeAnimate(position);//when you touch back view it will close
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {

            }
        });
    }

    public void deleteRow(int p){


        if(navigationPosition.equals("areas")){
            System.out.println(markedForDeletion);
            markedForDeletion.addArea(regions.getAreas().get(p));
            regions.getAreas().remove(p);
            ArrayList<Areas> areas = regions.getAreas();
            itemData.clear();
            for(Areas area : areas){
                itemData.add(new ItemRow(area.getAreaName()));
            }
        } else if(navigationPosition.equals("households")){
            markedForDeletion.addhousehold(regions.getAreas().get(currentArea).getHouseholds().get(p));
            regions.getAreas().get(currentArea).getHouseholds().remove(p);
            ArrayList<Areas> areas = regions.getAreas();
            ArrayList<Households> households = areas.get(currentArea).getHouseholds();
            itemData.clear();
            for(Households household : households){
                itemData.add(new ItemRow(household.getHouseholdName()));
            }
        } else if(navigationPosition.equals("members")){
            Member member = new Member();
            member.setMemberName(regions.getAreas().get(currentArea).getHouseholds().get(currentHousehold).getMembers().get(p));
            String url = regions.getAreas().get(currentArea).getHouseholds().get(currentHousehold).getRef();
                   url = url + "/Members/" + member.getMemberName();
            member.setRef(url);
            markedForDeletion.addMember(member);
            regions.getAreas().get(currentArea).getHouseholds().get(currentHousehold).getMembers().remove(p);
            ArrayList<Areas> areas = regions.getAreas();
            ArrayList<Households> households = areas.get(currentArea).getHouseholds();
            ArrayList<String> members = households.get(currentHousehold).getMembers();
            itemData.clear();
            for(String mb : members){
                itemData.add(new ItemRow(mb));
            }
        }
        try{
            editor.putString("Delete",JSONUtilities.stringify(markedForDeletion));
            editor.putString("Region",JSONUtilities.stringify(regions));
        }catch (JSONException e){}

        DashBoard dashBoard = (DashBoard)getActivity();
                  dashBoard.deleteRecord = markedForDeletion;
        adapter.notifyDataSetChanged();
    }

    public void editRow(int p){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        final int pos = p;
        input.setHint("Name");
        builder.setView(input);
        if(navigationPosition.equals("areas")){
            builder.setTitle("Edit The Area Name");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    regions.getAreas().get(pos).setAreaName(input.getText().toString());
                    ArrayList<Areas> areas = regions.getAreas();
                    itemData.clear();
                    for(Areas area : areas){
                        itemData.add(new ItemRow(area.getAreaName()));
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        } else if(navigationPosition.equals("households")){
            builder.setTitle("Edit The Household Name");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    regions.getAreas().get(currentArea).getHouseholds().get(pos).setHouseholdName(input.getText().toString());
                    ArrayList<Areas> areas = regions.getAreas();
                    ArrayList<Households> households = areas.get(currentArea).getHouseholds();
                    itemData.clear();
                    for(Households household : households){
                        itemData.add(new ItemRow(household.getHouseholdName()));
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        } else if(navigationPosition.equals("members")){
            regions.getAreas().get(currentArea).getHouseholds().get(currentHousehold).getMembers().set(pos, input.getText().toString());
            ArrayList<Areas> areas = regions.getAreas();
            ArrayList<Households> households = areas.get(currentArea).getHouseholds();
            ArrayList<String> members = households.get(currentHousehold).getMembers();
            itemData.clear();
            for(String mb : members){
                itemData.add(new ItemRow(mb));
            }
        }
        try{
            editor.putString("Region",JSONUtilities.stringify(regions));
        }catch (JSONException e){}
        adapter.notifyDataSetChanged();
    }

    public void goToInterview() {
        Intent intent = new Intent(getActivity(), InterviewActivity.class);
        intent.putExtra("area", currentArea);
        intent.putExtra("household", currentHousehold);
        intent.putExtra("interviewType", navigationPosition);
        startActivity(intent);
    }
}

