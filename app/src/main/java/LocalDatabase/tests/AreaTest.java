package LocalDatabase.tests;

import android.test.AndroidTestCase;
import android.util.Log;

import com.activeandroid.query.Select;

import junit.framework.Assert;

import java.util.HashMap;

import LocalDatabase.Area;

/**
 * Created by Chad Carey on 9/29/2014.
 */
public class AreaTest extends AndroidTestCase {
    Area area = null;
    long id = -1;

    public void setUp() {
        area = new Area();
    }

    public void testObjectCreation() {
        Assert.assertNotNull(area);
    }

    public void testAreaSave() {
        area.name = "Jimmy";
        area.created_at = "now";
        area.updated_at = "then";
        area.save();
        id = area.getId();
        Assert.assertFalse(id < 0);
    }

    public void testAreaLoadByName() {
        Area a = Area.getAreaByName("Jimmy");
        Assert.assertNotNull(a);
        Assert.assertTrue(a.name.equals("Jimmy"));
        Assert.assertTrue(a.created_at.equals("now"));
        Assert.assertTrue(a.updated_at.equals("then"));
    }

    public void testAreaLoadById() {
        Area a = Area.getAreaById(id);
        Assert.assertNotNull(a);
        Assert.assertTrue(a.name.equals("Jimmy"));
        Assert.assertTrue(a.created_at.equals("now"));
        Assert.assertTrue(a.updated_at.equals("then"));
    }
}
