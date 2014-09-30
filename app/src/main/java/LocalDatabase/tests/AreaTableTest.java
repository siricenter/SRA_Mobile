package LocalDatabase.tests;

import android.test.AndroidTestCase;

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
        Assert.assertFalse(table.insertArea("Jimmy") < 0);
        //HashMap <String, String> area = table.getArea("Jimmy");
        //Assert.assertNotNull(area);
        //Assert.assertTrue(area.get("name").equals("Jimmy"));
    }

    public  void testGetItemByString() {
        Assert.assertFalse(table.insertArea("Jimmy") < 0);
        HashMap <String, String> area = table.getArea("Jimmy");
        Assert.assertNotNull(area);
        Assert.assertTrue(area.get("name").equals("Jimmy"));
    }

    public void testInsertItemWithDATETIME() {

    }
}
