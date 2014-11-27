package com.sra.sliderFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chad.sra_mobile.R;
import com.sra.objects.Region;

import org.json.JSONObject;
import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONUtilities;
import org.quickconnectfamily.kvkit.kv.KVStore;
import org.quickconnectfamily.kvkit.kv.KVStoreEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Objects;

public class FragmentOne extends Fragment {

    private ArrayAdapter list;
    private ListView listView;
    private ArrayList<String> areasList;

    public FragmentOne() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        KVStore.setActivity(getActivity().getApplication());
        HashMap area = (HashMap)KVStore.getValue("Field");
        Region region = buildRegion(area);
        loadAreasIntoView(region);

        return view;
    }

    public Region buildRegion(HashMap region){
        Region object = new Region();
               object.setRegionName(region.get("regionName").toString());
        for(Object area : region.keySet()){
            System.out.println(area.toString());
        }
        return object;
    }

    public void loadAreasIntoView(Region area){


        list = new ArrayAdapter <String>(getActivity().getBaseContext(),android.R.layout.simple_list_item_1,areasList);
    }

}

