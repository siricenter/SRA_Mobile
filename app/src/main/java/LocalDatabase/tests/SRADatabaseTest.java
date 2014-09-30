package LocalDatabase.tests;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.example.chad.sra_mobile.MyActivity;

import junit.framework.Assert;

import LocalDatabase.AreaTable;
import LocalDatabase.SRADatabase;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Chad Carey on 9/29/2014.
 */
public class SRADatabaseTest extends AndroidTestCase {

    SRADatabase database = null;
    Context context = null;

    public void setUp() {
        database = SRADatabase.getInstance(getContext());
    }

    public void testDatabaseCreation() {
        Assert.assertNotNull(database);
    }

    public void testTableExistsFunctionWhenFalse() {
        Assert.assertFalse("SRADatabaseTest: testTableCreation: invalid table was found", database.tableExists("crazytable"));
    }

    public void testAreaTableCreation() {
        if(database != null) {
            Log.d("SRADatabaseTest: testTableCreation", "table was != null");
            Assert.assertTrue("SRADatabaseTest: testTableCreation: area table", database.tableExists("areas"));
        } else {
            Assert.assertTrue(false);
        }
    }

    public void testGetArea() {
        AreaTable table = database.getAreaTable();
        Assert.assertNotNull(table);
    }

    public String generateTimestamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
}
