package DataSync.httprequests.tests;

import android.util.Log;

import java.util.logging.Handler;

import DataSync.GetSync;


/**
 * Created by Chad Carey on 10/15/2014.
 */
public class SyncTestCaller extends GetSync {

    private final String SYNC_ADDRESS = "http://pokeapi.co/api/v1/pokemon/25/";

    public SyncTestCaller() {
        super();
    }

    @Override
    protected boolean syncLocalData() {
        Log.d("preparing to sync local data", "");
        if(data != null)
            if(!data.isEmpty()){
                Log.d("Pokemon name = ", (String) data.get("name"));
                return true;
            }
        return false;
    }

    @Override
    public String getSyncAddress() {
        return SYNC_ADDRESS;
    }

}