package com.sra.sliderFragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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


import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONUtilities;
import org.quickconnectfamily.kvkit.kv.KVStorageException;
import org.quickconnectfamily.kvkit.kv.KVStore;
import org.quickconnectfamily.kvkit.kv.KVStoreEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FragmentOne extends Fragment {

    private ArrayList<String> areasList;
    public Region regions;
    public SwipeListView swipelistview;
    public ItemAdapter adapter;
    List<ItemRow> itemData;
    int currentArea;
    int currentHousehold;
    View view;
    DeleteRecord markedForDeletion;
    public String navigationPosition;
    private float buttonWidth;
    public FragmentOne() {

    }


    public void deleteRow(int p){


        if(navigationPosition.equals("areas")){
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
            member.setCurrentArea(regions.getAreas().get(p).getAreaName());
            member.setCurrentHousehold(regions.getAreas().get(currentArea).getHouseholds().get(currentHousehold).getHouseholdName());
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
            KVStore.removeValue("Field");
            KVStore.storeValue("Field",regions);
        }catch (KVStorageException e){

        }
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
            KVStore.removeValue("Field");
            KVStore.storeValue("Field",regions);
        }catch (KVStorageException e){

        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        buttonWidth = 0;
        navigationPosition = "areas";
        areasList = new ArrayList<String>();

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
                System.out.println("Trying to Save " + key + " " + value);
            }

            @Override
            public void didStore(String key, Serializable value) {
                System.out.println(key + " " + value + " Saved");
            }

            @Override
            public boolean shouldDelete(String key) {
                return true;
            }

            @Override
            public void willDelete(String key) {
                System.out.println(key + " deleting");
            }

            @Override
            public void didDelete(String key) {
                System.out.println(key + " was deleted");
            }



        };

        //set the listener
        KVStore.setStoreEventListener(listener);

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




        KVStore.setActivity(getActivity().getApplication());
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


                for(String member : members){
                    itemData.clear();
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
        try {
            String json = JSONUtilities.stringify(KVStore.getValue("Field"));
            System.out.println("Loading Areas");
            System.out.println(json);
            Gson gson = new GsonBuilder().create();
            regions = gson.fromJson(json,Region.class);
            ArrayList<Areas> areas = regions.getAreas();
            for(Areas area : areas){
               areasList.add(area.getAreaName());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("Nothing Here");
        }
    }

    public void addArea(){
        final Areas area = new Areas();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter The Area Name");

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
             area.setAreaName(input.getText().toString());
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

        // Set up the input
        final EditText input = new EditText(getActivity());
        final EditText inputMember = new EditText(getActivity());
        input.setHint("Name");
        inputMember.setHint("Name of Head of household");
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        inputMember.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setView(inputMember);

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
            KVStore.removeValue("Field");
            KVStore.storeValue("Field",regions);
        }
        catch (KVStorageException e){

        }
    }

    public void reloadHouseholdsIntoView(){
        ArrayList<Households> households = regions.getAreas().get(currentArea).getHouseholds();
        itemData.clear();
        for(Households houses : households){
            itemData.add(new ItemRow(houses.getHouseholdName()));
        }
        adapter.notifyDataSetChanged();
        try{
            KVStore.removeValue("Field");
            KVStore.storeValue("Field",regions);
        }
        catch (KVStorageException e){

        }
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
            KVStore.removeValue("Field");
            KVStore.storeValue("Field",regions);
        }
        catch (KVStorageException e){

        }
    }
}

