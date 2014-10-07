package DataSync.httprequests.SyncTypes;

import android.util.Log;

import java.util.HashMap;

import DataSync.DataSync;

/**
 * This is an interface for updateable objects
 * Created by Chad Carey on 9/22/2014.
 */
public abstract class Gettable {

    private int syncFails = 0;

    private HashMap<String, Object> data;

    /**
     * syncData handles how the data is synced to the database after the data has been retrieved
     * @return
     */
    public abstract boolean syncData();
    public abstract String getSyncAddress();

    /**
     * This is a callback function that is called once the data HashMap is finished being downloaded
     * If the sync to the local database fails the program will leave this task at the top of the
     * list so it can try to retrieve the data again. This will happen 3 times then assume the task
     * is malformed and remove it from the list.
     */
    public final void update() {
        if(syncData()) {
            DataSync.getInstance().popTop();
        } else if(syncFails > 3) {
            Log.w("Gettable : update() ", "failed to sync an item to the local database. Removing it from the list");
            DataSync.getInstance().popTop();
        } else {
            ++syncFails;
        }

        // start next sync
        DataSync.getInstance().nextGetSync();
        return;
    }

    public final HashMap<String, Object> getReturnData() {
        data = new HashMap<String, Object>();
        return data;
    }
}
