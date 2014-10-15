package DataSync;

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
    protected boolean syncLocalData() {
        // break up into areas and send to Area.parse(area)
        //return Area.parse(area);
        return  false;
    }

    @Override
    public String getSyncAddress() {
        return SYNC_ADDRESS;
    }
}
