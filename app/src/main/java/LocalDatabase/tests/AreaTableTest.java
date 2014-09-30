package LocalDatabase.tests;

import android.test.AndroidTestCase;
import android.util.Log;

import junit.framework.Assert;

import java.util.HashMap;

import LocalDatabase.AreaTable;

/**
 * Created by Chad Carey on 9/29/2014.
 */
public class AreaTableTest extends AndroidTestCase {

    AreaTable table;

    public void setUp() {
        table = new AreaTable(getContext());
    }

    public void testTableObjectCreation() {
        Assert.assertNotNull(table);
    }

    public void testInsertItem() {
        long id = table.insertArea("Jimmy");
        Log.d("AreaTableTest: testInsertItem()", "Id ==" + Long.toString(id));
        Assert.assertFalse(id < 0);
        //HashMap <String, String> area = table.getArea("Jimmy");
        //Assert.assertNotNull(area);
        //Assert.assertTrue(area.get("name").equals("Jimmy"));
    }

    public void testGetItemByString() {
        Assert.assertFalse(table.insertArea("Jimmy") < 0);
        HashMap <String, String> area = table.getArea("Jimmy");
        Assert.assertNotNull(area);
        Log.d("AreaTableTest: testGetItemByString()", "area.get(id) == " + area.get("id"));
        Log.d("AreaTableTest: testGetItemByString()", "area.get(name) == " + area.get("name"));
        Log.d("AreaTableTest: testGetItemByString()", "area.get(created_at) == " + area.get("created_at"));
        Log.d("AreaTableTest: testGetItemByString()", "area.get(updated_at) == " + area.get("updated_at"));
        Assert.assertTrue(area.get("id").equals("1"));
        Assert.assertTrue(area.get("name").equals("Jimmy"));
        Assert.assertNotNull(area.get("created_at"));
        Assert.assertNotNull(area.get("updated_at"));
    }

    public void testInsertItemWithDATETIME() {

    }
}
