package DataSync.httprequests.tests;

import android.os.Handler;
import android.test.InstrumentationTestCase;
import android.util.Log;


import java.lang.ref.SoftReference;
import java.util.HashMap;

import DataSync.httprequests.DataGetter;
import DataSync.httprequests.SyncTypes.Gettable;

/**
 * Created by Chad Carey on 9/22/2014.
 */
public class TestThreadRunner extends InstrumentationTestCase implements Gettable {

    private Handler handler;
    private SoftReference softy;
    private String address;
    private Gettable updater;
    private HashMap<String, Object> pokemonData;

    @Override
    public boolean update() {
        Log.d("Test ThreadRunner", "Updated");
        Log.d("Test ThreadRunner", "test data is now == " + pokemonData.get("name"));
        return true;
    }

    public void testThreadRunner() {

        updater = this; // this class is an Gettable class to be used in thread runner
        try {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("Test ThreadRunner", "Test started on ui thread");
                    //handler = new Handler();

                    pokemonData = new HashMap<String, Object>();
                    pokemonData.put("state", "unchanged");
                    //softy = new SoftReference(pokemonData);

                    address = "http://pokeapi.co/api/v1/pokemon/25/";

                    DataGetter runner = new DataGetter();
                    Thread t = runner.startThread(pokemonData, address, updater);
                    try {
                        t.join(); // makes test wait for the other thread to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.e("test ThreadRunner", "Failed to rejoin threads");
                        assertTrue(false);
                        return;
                    }
                }
            });
        } catch (Throwable e) {
            Log.e("Test ThreadRunner", "failed to start thread runner thread");
            assertTrue(false);
            return;
        }

        assertTrue( ((String)pokemonData.get("name")).equals("Pikachu") );
    }
}
