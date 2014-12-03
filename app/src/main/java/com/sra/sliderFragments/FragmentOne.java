package com.sra.sliderFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.chad.sraMobile.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sra.objects.Areas;
import com.sra.objects.Households;
import com.sra.objects.Region;


import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONUtilities;
import org.quickconnectfamily.kvkit.kv.KVStore;
import org.quickconnectfamily.kvkit.kv.KVStoreEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class FragmentOne extends Fragment {

    private ArrayAdapter list;
    private ListView listView;
    private ArrayList<String> areasList;
    private ArrayList<String> householdsList;
    public Region regions;
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
                System.out.println("Trying to Save" + key + " " + value);
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
        listView = (ListView) view.findViewById(R.id.ListView01);


        KVStore.setActivity(getActivity().getApplication());

        buildRegion();
        loadAreasIntoView();

        return view;
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

    public void loadAreasIntoView(){
        list = new ArrayAdapter <String>(getActivity().getBaseContext(),android.R.layout.simple_list_item_1,areasList);
        listView.setAdapter(list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loadhouseholdsIntoView(view,i);
            }
        });
    }

    public void loadhouseholdsIntoView(View v, int position){
        ArrayList<Areas> areas = regions.getAreas();
        final ArrayList<Households> houses = areas.get(position).getHouseholds();

        for (Households household : houses){
            householdsList.add(household.getHouseholdName());
        }

        list = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_list_item_1,householdsList);
        listView.setAdapter(list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loadMembersIntoView(view,i,houses);
            }
        });
    }

    public void loadMembersIntoView(View v, int position,ArrayList<Households> houses){
       ArrayList<String> members = houses.get(position).getMembers();
       list = new ArrayAdapter(getActivity().getBaseContext(),android.R.layout.simple_list_item_1,members);
       listView.setAdapter(list);
    }

    public void addArea(View v){

    }


}

