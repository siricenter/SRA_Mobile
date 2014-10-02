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

    public void testObjectCreation() {
        Area area = new Area();
        Assert.assertNotNull(area);
    }

    private Area generateArea() {
        Area area = new Area();
        area.name = "Jimmy";
        area.created_at = "now";
        area.updated_at = "then";
        area.save();

        return area;
    }

    public void testAreaSave() {
        long id = generateArea().getId();
        Assert.assertTrue(id >= 0);
    }

    public void testLoadById() {
        long id = generateArea().getId();

        Area a = Area.load(Area.class, id);

        Assert.assertNotNull(a);
        Assert.assertTrue(a.name.equals("Jimmy"));
        Assert.assertTrue(a.created_at.equals("now"));
        Assert.assertTrue(a.updated_at.equals("then"));
    }

    public void testLoadByName() {
        Area area = Area.getByName("Jimmy");
        Assert.assertNotNull(area);
        Assert.assertTrue(area.name.equals("Jimmy"));
        Assert.assertTrue(area.created_at.equals("now"));
        Assert.assertTrue(area.updated_at.equals("then"));
    }
}
