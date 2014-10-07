package DataSync;
 import DataSync.httprequests.DataGetter;
 import DataSync.httprequests.SyncTypes.Syncable;

 import java.util.LinkedList;


public class DataSync {
    // IDEA: this class is singleton. We have a static queque of updateables, after each update the
    // a function is called to start the next item. Strategy pattern to allow different update types to be ran
    // example different function for update all areas. factory can create the kind of sync the user wants

    private static LinkedList<Syncable> syncList;
    private DataGetter dataGetter;
    private static DataSync instance;
    private boolean syncInProgress;


    private DataSync() {
        syncList = new LinkedList<Syncable>();
        dataGetter = new DataGetter();
        syncInProgress = false;
    }

    public static DataSync getInstance() {
        if(instance == null){
            instance = new DataSync();
        }
        return instance;
    }

    public void nextSync() {
        if(!syncList.isEmpty()) {
            Syncable sync = syncList.peekFirst();
            dataGetter.startThread(sync);
        } else {
            syncInProgress = false;
        }
    }

    public void startSync() {
        if(!syncInProgress) {
            syncInProgress = true;
            // populate items into syncList
            // download all areas
            // start the first item
        }
    }

    /**
     * Removes the first element from the top of the list
     */
    public void popTop() {
        syncList.pollFirst();
    }

    /**
     * adds an item to the back of the sync list
     * @param sync
     */
    public void pushBack(Syncable sync) {
        if (sync != null)
            syncList.addLast(sync);
    }
}
