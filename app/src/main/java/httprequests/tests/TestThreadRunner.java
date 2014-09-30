package httprequests.tests;

import android.os.Handler;
import android.test.InstrumentationTestCase;
import android.util.Log;


import java.lang.ref.SoftReference;
import java.util.HashMap;

import httprequests.HttpThreadRunner;
import httprequests.Updateable;

/**
 * Created by Chad Carey on 9/22/2014.
 */
public class TestThreadRunner extends InstrumentationTestCase implements Updateable {

    private Handler handler;
    private SoftReference softy;
    private String address;
    private Updateable updater;
    private HashMap<String, Object> pokemonData;

    @Override
    public boolean update() {
        Log.d("Test ThreadRunner", "Updated");
        Log.d("Test ThreadRunner", "test data is now == " + pokemonData.get("name"));
        return true;
    }

    public void testThreadRunner() {

        updater = this; // this class is an Updateable class to be used in thread runner
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

                    HttpThreadRunner runner = new HttpThreadRunner();
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
