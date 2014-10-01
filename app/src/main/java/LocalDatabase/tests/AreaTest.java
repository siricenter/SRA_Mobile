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
    private Area area = null;
    private long id;

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
        Assert.assertTrue(id > 0);

        Area a = Area.getById(id);
        Log.d("testAreaSave", "ID=" + Long.toString(id));
        Assert.assertNotNull(a);
        Assert.assertTrue(a.name.equals("Jimmy"));
        Assert.assertTrue(a.created_at.equals("now"));
        Assert.assertTrue(a.updated_at.equals("then"));
    }

    public void testAreaLoad() {
        Area b = Area.getByName("Jimmy");
        Assert.assertNotNull(b);
        Assert.assertTrue(b.name.equals("Jimmy"));
        Assert.assertTrue(b.created_at.equals("now"));
        Assert.assertTrue(b.updated_at.equals("then"));

    }
}
