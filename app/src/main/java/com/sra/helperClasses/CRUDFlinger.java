package com.sra.helperClasses;

import android.app.Activity;
import android.content.SharedPreferences;

import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONUtilities;

import java.io.Serializable;

/**
 * Created by imac on 12/17/14.
 */
public class CRUDFlinger {

    private static CRUDFlinger instance = null;
    SharedPreferences loader;
    SharedPreferences.Editor saver;

    protected CRUDFlinger(){

    }

    public static CRUDFlinger getInstance() {

        if(instance == null) {
            instance = new CRUDFlinger();
        }
        return instance;
    }

    public void setPreferences(Activity activity){
        loader = activity.getApplication().getSharedPreferences("AppPrefs", activity.getApplication().MODE_PRIVATE);
        saver = activity.getApplication().getSharedPreferences("AppPrefs", activity.getApplication().MODE_PRIVATE).edit();
    }

    public void save(Serializable serializable, String key){
        try{
            saver.putString(key,JSONUtilities.stringify(serializable));
            saver.commit();
        }catch (JSONException e){}
    }
}
