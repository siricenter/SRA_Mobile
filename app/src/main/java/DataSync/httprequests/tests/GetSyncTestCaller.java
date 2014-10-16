package DataSync.httprequests.tests;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

import DataSync.GetSync;


/**
 * This class used for testing GetSync and AsyncGet requests
 * Created by Chad Carey on 10/15/2014.
 */
public class GetSyncTestCaller extends GetSync {

    private String syncAddress;
    private static String LOG_TAG = "GetSyncTestCaller";

    public GetSyncTestCaller() {
        super();
        syncAddress = "http://pokeapi.co/api/v1/pokemon/25/";
    }

    public GetSyncTestCaller(String id) {
        super();
        syncAddress = "http://pokeapi.co/api/v1/pokemon/" + id + "/";
    }

    @Override
    protected boolean syncLocalData(JSONObject json) {
        data = json;
        Log.d("preparing to sync local data", LOG_TAG);
        if(data != null) {
            try {
                Log.d("Pokemon name = ", (String) data.get("name"));
            } catch (JSONException e) {
                Log.d(LOG_TAG, "missing data in JSON object");
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String getSyncAddress() {
        return syncAddress;
    }

}