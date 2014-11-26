package com.sra.sliderFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chad.sra_mobile.R;

import org.json.JSONObject;
import org.quickconnectfamily.kvkit.kv.KVStore;
import org.quickconnectfamily.kvkit.kv.KVStoreEventListener;

import java.io.Serializable;
import java.util.EventListener;
import java.util.HashMap;

public class FragmentOne extends Fragment {


    HashMap area;


    public FragmentOne() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        View view = inflater.inflate(R.layout.button1slide, container,false);

        KVStore.setActivity(getActivity().getApplication());
        HashMap hashMap =(HashMap)KVStore.getValue("Field");

        loadAreasIntoView(area);

        return view;
    }

    public void loadAreasIntoView(HashMap area){

    }

}

