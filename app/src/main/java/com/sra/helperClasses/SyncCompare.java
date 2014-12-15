package com.sra.helperClasses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sra.objects.Areas;
import com.sra.objects.Region;

import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONUtilities;
import org.quickconnectfamily.kvkit.kv.KVStore;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jakobhartman on 12/10/14.
 */
public class SyncCompare {
    private Region newRegion;
    private Region currentRegion;
    private DataTransfer transfer;



    public SyncCompare(Region region){
        this.transfer = new DataTransfer();
        this.newRegion = region;
        try{
            String json = JSONUtilities.stringify(KVStore.getValue("Field"));
            System.out.println("Loading Areas");
            System.out.println(json);
            Gson gson = new GsonBuilder().create();
            this.currentRegion = gson.fromJson(json,Region.class);
        }catch (JSONException e){}
        catch (NullPointerException e){}
    }

    public void startCompare(){
        if(newRegion != null && currentRegion != null){
            transfer.setAreasToLocal((ArrayList)Subtract(currentRegion.getAreas(),newRegion.getAreas()));
            transfer.setAreasToFirebase((ArrayList)Subtract(newRegion.getAreas(),currentRegion.getAreas()));
        }
        else{
            throw new NullPointerException();
        }
    }

    public Region getCurrentRegion() {
        return currentRegion;
    }

    public static Collection Subtract(Collection coll1, Collection coll2)
    {
        Collection result = new ArrayList(coll2);
        result.removeAll(coll1);
        return result;
    }
}
