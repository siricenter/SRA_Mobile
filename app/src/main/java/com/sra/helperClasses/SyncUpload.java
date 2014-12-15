package com.sra.helperClasses;

import com.firebase.client.Firebase;
import com.sra.objects.Areas;
import com.sra.objects.DeleteRecord;
import com.sra.objects.Region;

import org.json.JSONObject;
import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONUtilities;
import org.quickconnectfamily.kvkit.kv.SearchRunnable;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by jakobhartman on 12/10/14.
 */
public class SyncUpload {
    Region region;
    DeleteRecord deleteRecord;
    String organization;

    public SyncUpload(Region region,DeleteRecord deleted,String org){
                this.region = region;
                this.deleteRecord = deleted;
                this.organization = org;
    }

    public void startUpload(){
        Firebase base;
        for(Areas area : region.getAreas()){
            System.out.println(area);
            base = new Firebase(area.getRef());
            try{
               HashMap map = jsonToMap(JSONUtilities.stringify(area));
                base.setValue(map);
            } catch (JSONException e){}
        }
    }

    public Region getRegion() {
        return region;
    }

    public static HashMap jsonToMap(String t) throws JSONException {

        HashMap<String, String> map = new HashMap<String, String>();
        try {
            JSONObject jObject = new JSONObject(t);
            Iterator<?> keys = jObject.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = jObject.getString(key);
                map.put(key, value);

            }

        }catch (org.json.JSONException e){

        }
        return map;
    }
}
