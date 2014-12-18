package com.sra.helperClasses;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sra.objects.Region;

import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONUtilities;

import java.io.Serializable;

/**
 * Created by imac on 12/17/14.
 */
public class CRUDFlinger {

    private static CRUDFlinger instance = null;
    static SharedPreferences loader;
    static SharedPreferences.Editor saver;
    static Region region = null;

    protected CRUDFlinger(){

    }

    public static CRUDFlinger getInstance() {

        if(instance == null) {
            instance = new CRUDFlinger();
        }
        return instance;
    }

    public static void setPreferences(Activity activity){
        loader = activity.getApplication().getSharedPreferences("AppPrefs", activity.getApplication().MODE_PRIVATE);
        saver = activity.getApplication().getSharedPreferences("AppPrefs", activity.getApplication().MODE_PRIVATE).edit();
    }

    public static void save(String key,Serializable serializable){
        try{
            saver.putString(key,JSONUtilities.stringify(serializable));
            saver.commit();
        }catch (JSONException e){}
    }

    public static Object load(String key,Class className){
        String json = loader.getString(key,null);
        Gson gson = new GsonBuilder().create();
        Object object = gson.fromJson(json,className);

        return object;
    }

    public static Region loadRegion(){
        String json = loader.getString("Region",null);
        Gson gson = new GsonBuilder().create();
        Region region1 = gson.fromJson(json,Region.class);
        return region1;
    }

    public static void saveRegion(){
        if(region == null){
            loadRegion();
            return;
        }else{
            try{
                saver.putString("Region",JSONUtilities.stringify(region));
                saver.commit();
            }catch (JSONException e){}
        }
   }

    public static Region getRegion(){
        if(region == null){
            loadRegion();
        }
        return region;
    }


}
