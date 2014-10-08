package DataSync;

import java.util.LinkedList;


public class DataSync {
    // IDEA: this class is singleton. We have a static queque of updateables, after each update the
    // a function is called to start the next item. Strategy pattern to allow different update types to be ran
    // example different function for update all areas. factory can create the kind of sync the user wants

    private static DataSync instance;
    private static LinkedList<Syncable> syncList;
    private boolean syncInProgress;


    private DataSync() {
        syncList = new LinkedList<Syncable>();
        syncInProgress = false;
    }

    /**
     * gets the instance of this class
     * @return
     */
    public static DataSync getInstance() {
        if(instance == null){
            instance = new DataSync();
        }
        return instance;
    }

    /**
     * nextSync: this method will start the listed sync tasks
     */
    void nextSync() {
        // if the list isn't empty we start the next sync task
        if(!syncList.isEmpty()) {
            syncList.pollFirst();
            Syncable sync = syncList.peekFirst();
            sync.startSync();
        } else {
            // we finished all of the sync tasks, set the busy flag to false
            syncInProgress = false;
        }
        return;
    }


    /**
     * This will start the sync process if it is not already in progress
     */
    public void startSync() {
        if(!syncInProgress) {
            syncInProgress = true;
            // prepare the syncPost list
            // prepare the syncGet task
            // start
            Syncable sync = syncList.peekFirst();
            sync.startSync();
        }
    }


    /**
     * this will add a sync task to the list and start the sync tasks
     * @param sync
     */
    public void sync(Syncable sync) {
        this.pushBack(sync);
        startSync();
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
