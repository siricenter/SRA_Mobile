package DataSync.httprequests.tests;

import android.test.InstrumentationTestCase;


/**
 * Created by Chad Carey on 10/15/2014.
 */
public class TestSync extends InstrumentationTestCase {
    public void testSync() {

        new GetSyncTestCaller().startSync();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
