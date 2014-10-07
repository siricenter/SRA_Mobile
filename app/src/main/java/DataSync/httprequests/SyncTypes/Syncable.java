package DataSync.httprequests.SyncTypes;

import java.util.HashMap;

import DataSync.DataSync;

/**
 * This is an interface for updateable objects
 * Created by Chad Carey on 9/22/2014.
 */
public abstract class Syncable {


    private HashMap<String, Object> data;

    /**
     * syncData handles how the data is synced to the database after the data has been retrieved
     * @return
     */
    public abstract boolean syncData();
    public abstract String getSyncAddress();

    /**
     * This is a callback function that is called once the data HashMap is finished being downloaded
     */
    public final void update() {
        if(syncData()) {
            DataSync.getInstance().popTop();
        }

        // start next sync
        DataSync.getInstance().nextSync();
        return;
    }

    public final HashMap<String, Object> getReturnData() {
        data = new HashMap<String, Object>();
        return data;
    }
}
