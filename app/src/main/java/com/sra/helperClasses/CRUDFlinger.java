package com.sra.helperClasses;

import android.app.Application;
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
    private static SharedPreferences loader = null;
    private static SharedPreferences.Editor saver = null;
    private static Region region = null;
    private static Application application = null;

    protected CRUDFlinger(){

    }

    public static CRUDFlinger getInstance() {

        if(instance == null) {
            instance = new CRUDFlinger();
        }
        return instance;
    }

    public static void setApplication(Application applicationPassed){
        application = applicationPassed;
    }

    private static void setPreferences(){
        if(application == null){
            throw new NullPointerException();
        }
        else{
            loader = application.getSharedPreferences("AppPrefs", application.MODE_PRIVATE);
            saver = application.getSharedPreferences("AppPrefs", application.MODE_PRIVATE).edit();
        }

    }

    public static void save(String key,Serializable serializable){
        setPreferences();
        try{
            saver.putString(key,JSONUtilities.stringify(serializable));
            saver.commit();
        }catch (JSONException e){}
    }

    public static Object load(String key,Class className){
        setPreferences();
        String json = loader.getString(key,null);
        Gson gson = new GsonBuilder().create();
        Object object = gson.fromJson(json,className);

        return object;
    }

    private static void loadRegion(){
        setPreferences();
        String json = loader.getString("Region",null);
        Gson gson = new GsonBuilder().create();
        Region region1 = gson.fromJson(json,Region.class);
        region = region1;
    }

    public static void saveRegion(){
        setPreferences();
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
