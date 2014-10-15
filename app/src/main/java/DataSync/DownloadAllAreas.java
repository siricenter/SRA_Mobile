package DataSync;

import org.json.JSONObject;

import LocalDatabase.Area;

/**
 * Created by Chad Carey on 10/7/2014.
 */
public class DownloadAllAreas extends GetSync {

    private final String SYNC_ADDRESS = "";

    public DownloadAllAreas() {
        super();
    }

    @Override
    protected boolean syncLocalData(JSONObject json) {
        return false;
    }

    @Override
    public String getSyncAddress() {
        return SYNC_ADDRESS;
    }
}
