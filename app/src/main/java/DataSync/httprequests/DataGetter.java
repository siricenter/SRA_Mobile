package DataSync.httprequests;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import DataSync.GetSync;
import quickconnectfamily.json.JSONInputStream;


/**
 * Handles mutilthreaded Http get calls in a memory safe way. Returns the JSON object as a HashMap
 * of Strings and ArrayLists if there are multiple items under the same key
 * Created by Chad Carey on 9/22/2014.
 */
public class DataGetter implements Runnable {
    private SoftReference softData;
    private SoftReference softAddress;
    private SoftReference softCaller;
    private static DataGetter instance;
    private final Handler handler = new Handler();

    private DataGetter() {}

    public static DataGetter getInstance() {
        if (instance == null) {
            instance = new DataGetter();
        }
        return instance;
    }

    public Thread startThread(GetSync caller) {
        Log.d("ThreadRunner", "Creating handler on current thread");

        Log.d("ThreadRunner", "Loading soft references");
        this.softData = new SoftReference(caller.getReturnData());
        this.softAddress = new SoftReference(caller.getSyncAddress());
        this.softCaller = new SoftReference(caller);
        Log.d("ThreadRunner", "Running ThreadRunner::run() on back thread");
        Thread t = new Thread(this);
        t.start();
        return t;
    }


    @Override
    public void run() {
        // open http
        try {
            URL url = new URL( (String)softAddress.get() );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            readStream(connection.getInputStream());


        // change the data in the soft data reference
            //((HashMap<String,String>)softData.get()).put("state", "changed");

        // tell the caller that we have finished on this thread by calling update
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("ThreadRunner ", "posting runnable to ui thread");
                   // ((GetSync)softCaller.get()).update();
                }
            });
            connection.disconnect();
        } catch (MalformedURLException e) {
            Log.e("ThreadRunner", "failed to ope url connection");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ThreadRunner", "Failed to get input stream from connection");
        } catch (Exception e){
            Log.e("ThreadRunner ", "Failed to get data from the SoftReference");
        }
    }

    private void readStream(InputStream stream) {
        BufferedReader reader = null;
        try {
            JSONInputStream inputStream = new JSONInputStream(stream);
            Object o = inputStream.readObject();
            Log.d("ThreadRunner", "object class was = " + o.getClass());
            ((HashMap<String,String>)softData.get()).putAll((HashMap<String, String>) o);
        } catch (Exception e) {
            Log.e("ThreadRunner:read stream", "failed to read input stream");
        }
    }
}
