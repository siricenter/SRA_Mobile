package LocalDatabase.tests;

import android.test.InstrumentationTestCase;
import android.util.Log;

import LocalDatabase.SRAModel;
import LocalDatabase.SyncDate;

/**
 * Created by Chad Carey on 10/14/2014.
 */
public class SyncDatesTest extends InstrumentationTestCase {

    public void testTableCreation() {
        SyncDate syncDate = new SyncDate();
        assertNotNull(syncDate);
    }

    public void testDateGenerator() {
        String date = SRAModel.generateTimestamp();
        Log.d("SyncDatesTest : testDateGenerator()", "date = " + date);
        assertNotNull(date);
    }

    public void testSyncDateSave() {
        SyncDate syncDate = generateSyncDate();
        assertTrue(syncDate.getId() >= 0);
    }

    public void testDateLoad() {
        long id = generateSyncDate().getId();
        SyncDate syncDate = SyncDate.load(SyncDate.class, id);
        assertNotNull(syncDate);
    }

    public void testDataUpdate() {
        SyncDate syncDate = generateSyncDate();
        long id = syncDate.getId();

        syncDate.tableName = "people";
        String time1 = syncDate.generateTimestamp();
        syncDate.pullDate = time1;
        String time2 = syncDate.generateTimestamp();
        syncDate.pushDate = time2;
        syncDate.save();

        SyncDate syncDate2 = SyncDate.load(SyncDate.class, id);
        assertTrue(syncDate2.tableName.equals("people"));
        assertTrue(syncDate2.pullDate.equals(time1));
        assertTrue(syncDate2.pushDate.equals(time2));
    }

    public SyncDate generateSyncDate() {
        SyncDate syncDate = new SyncDate();
        syncDate.tableName = "areas" + Math.random();
        syncDate.pullDate = syncDate.generateTimestamp();
        syncDate.pushDate = syncDate.generateTimestamp();
        syncDate.save();
        return syncDate;
    }
}
