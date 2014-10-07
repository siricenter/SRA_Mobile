package DataSync;
 import DataSync.httprequests.DataGetter;
 import DataSync.httprequests.SyncTypes.Gettable;

 import java.util.LinkedList;


public class DataSync {
    // IDEA: this class is singleton. We have a static queque of updateables, after each update the
    // a function is called to start the next item. Strategy pattern to allow different update types to be ran
    // example different function for update all areas. factory can create the kind of sync the user wants

    private static LinkedList<Gettable> getList;
    private DataGetter dataGetter;
    private static DataSync instance;
    private boolean syncInProgress;


    private DataSync() {
        getList = new LinkedList<Gettable>();
        dataGetter = new DataGetter();
        syncInProgress = false;
    }

    public static DataSync getInstance() {
        if(instance == null){
            instance = new DataSync();
        }
        return instance;
    }

    /**
     * nextSync: this method will start the listed sync tasks
     */
    public void nextGetSync() {
        // if the list isn't empty we start the next sync task
        if(!getList.isEmpty()) {
            Gettable sync = getList.peekFirst();
            dataGetter.startThread(sync);
        } else {
            // we finished all of the sync tasks, set the busy flag to false
            syncInProgress = false;
        }
    }

    /**
     * This will start the sync process if it is not already in progress
     */
    public void startSync() {
        if(!syncInProgress) {
            syncInProgress = true;
            // prepare the syncPost list
            //nextPostSync();
            // prepare the syncGet task
            nextGetSync();
        }
    }

    /**
     * this will add a sync task to the list and start the sync tasks
     * @param sync
     */
    public void sync(Gettable sync) {
        this.pushBack(sync);
        startSync();
    }

    /**
     * Removes the first element from the top of the list
     */
    public void popTop() {
        getList.pollFirst();
    }

    /**
     * adds an item to the back of the sync list
     * @param sync
     */
    public void pushBack(Gettable sync) {
        if (sync != null)
            getList.addLast(sync);
    }
}
