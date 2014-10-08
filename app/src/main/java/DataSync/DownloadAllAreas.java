package DataSync;

/**
 * Created by Chad Carey on 10/7/2014.
 */
public class DownloadAllAreas extends GetSync {

    private final String SYNC_ADDRESS = "";

    public DownloadAllAreas() {
        super();
    }

    @Override
    public boolean syncLocalData() {

        return false;
    }

    @Override
    public String getSyncAddress() {
        return SYNC_ADDRESS;
    }
}
