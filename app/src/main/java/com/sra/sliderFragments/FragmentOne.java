package com.sra.sliderFragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.sra.objects.Households;
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

    private ArrayAdapter list;
    private ListView listView;
    private ArrayList<String> areasList;
    private ArrayList<String> householdsList;
    public Region regions;
    public SwipeListView swipelistview;
    public ItemAdapter adapter;
    List<ItemRow> itemData;

    public FragmentOne() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        areasList = new ArrayList<String>();
        householdsList = new ArrayList<String>();

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

        View view = inflater.inflate(R.layout.button1slide, container,false);
        // listView = (ListView) view.findViewById(R.id.ListView01);
        swipelistview = (SwipeListView)view.findViewById(R.id.example_swipe_lv_list);
        itemData = new ArrayList<ItemRow>();

        adapter=new ItemAdapter(getActivity(),R.layout.custom_row,itemData);

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
        //loadAreasIntoView();



        return view;
    }


    public void setListener(){
        //These are the swipe listview settings. you can change these
        //setting as your requrement
        swipelistview.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT); // there are five swiping modes
        swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL); //there are four swipe actions
        swipelistview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
        swipelistview.setOffsetLeft(convertDpToPixel(210f)); // left side offset
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

    }

    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    public void buildRegion(){
        try {
            String json = JSONUtilities.stringify(KVStore.getValue("Field"));
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

    public void reloadAreasIntoView(){
        ArrayList<Areas> areas = regions.getAreas();
        areasList.clear();
        for(Areas area : areas){
            areasList.add(area.getAreaName());
        }
        //list.notifyDataSetChanged();
        try{
            KVStore.storeValue("Field",regions);
        }
        catch (KVStorageException e){

        }

    }

    public void loadAreasIntoView(){
       // list = new ArrayAdapter <String>(getActivity().getBaseContext(),android.R.layout.simple_list_item_1,areasList);
        //listView.setAdapter(list);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                loadhouseholdsIntoView(view,i);
//            }
//        });
    }

    public void loadhouseholdsIntoView(View v, int position){
        ArrayList<Areas> areas = regions.getAreas();
        final ArrayList<Households> houses = areas.get(position).getHouseholds();

        for (Households household : houses){
            householdsList.add(household.getHouseholdName());
        }

//        list = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_list_item_1,householdsList);
//        listView.setAdapter(list);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                loadMembersIntoView(view,i,houses);
//            }
//        });
    }

    public void loadMembersIntoView(View v, int position,ArrayList<Households> houses){
       ArrayList<String> members = houses.get(position).getMembers();
//       list = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_list_item_1,members);
//       listView.setAdapter(list);
    }



}

