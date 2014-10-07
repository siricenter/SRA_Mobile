package DataSync;

import DataSync.httprequests.SyncTypes.DownloadAllAreas;
import DataSync.httprequests.SyncTypes.Gettable;

/**
 * Created by Chad Carey on 10/7/2014.
 */
public class GettableFactory {

    /**
     * returns a new DownloadAllAreas task (object)
     * this object will download every single area from the API
     * @return
     */
    public Gettable DownloadAllAreas() {
        return new DownloadAllAreas();
    }
}
