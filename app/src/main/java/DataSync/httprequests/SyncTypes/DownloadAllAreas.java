package DataSync.httprequests.SyncTypes;

/**
 * Created by Chad Carey on 10/7/2014.
 */
public class DownloadAllAreas extends Syncable {

    private final String SYNC_ADDRESS = "";

    DownloadAllAreas() {
        super();
    }

    @Override
    public boolean syncData() {
        // put the correct data into area objects and save them to the database
        return false;
    }

    @Override
    public String getSyncAddress() {
        return SYNC_ADDRESS;
    }
}
