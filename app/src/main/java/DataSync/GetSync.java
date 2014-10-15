package DataSync;

import android.util.Log;

import java.util.HashMap;

import DataSync.httprequests.AsyncGetter;
import DataSync.httprequests.DataGetter;

/**
 * This is an interface for updateable objects
 * Created by Chad Carey on 9/22/2014.
 */
public abstract class GetSync implements Syncable {

    private int syncFails;
    private AsyncGetter dataGetter;
    protected HashMap<String, Object> data;
    private DataSync dataSync;

    public GetSync() {
        dataGetter = new AsyncGetter();
        dataSync = DataSync.getInstance();
        syncFails = 0;
    }

    @Override
    public void startSync() {
        dataGetter.start(this);
    }

    /**
     * syncData handles how the data is synced to the database after the data has been retrieved
     * @return
     */
    protected abstract boolean syncLocalData();
    public abstract String getSyncAddress();

    /**
     * This is a callback function that is called once the data HashMap is finished being downloaded
     * If the sync to the local database fails the program will leave this task at the top of the
     * list so it can try to retrieve the data again. This will happen 3 times then assume the task
     * is malformed and remove it from the list.
     */
    public final void update() {
        if(syncLocalData()) {
            dataSync.nextSync();
        } else if(syncFails > 3) {
            Log.w("GetSync : update() ", "failed to sync an item to the local database. Removing it from the list");
            dataSync.nextSync();
        } else {
            ++syncFails;
            startSync();
        }
    }

    public final HashMap<String, Object> getReturnData() {
        data = new HashMap<String, Object>();
        return data;
    }
}
