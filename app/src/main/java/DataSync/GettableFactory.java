package DataSync;

/**
 * Created by Chad Carey on 10/7/2014.
 */
public class GettableFactory {

    /**
     * returns a new DownloadAllAreas task (object)
     * this object will download every single area from the API
     * @return
     */
    public GetSync DownloadAllAreas() {
        return new DownloadAllAreas();
    }
}
