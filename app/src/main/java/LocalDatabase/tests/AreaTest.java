package LocalDatabase.tests;

import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.activeandroid.query.Select;

import junit.framework.Assert;

import java.util.HashMap;

import LocalDatabase.Area;

/**
 * Created by Chad Carey on 9/29/2014.
 */
public class AreaTest extends InstrumentationTestCase {

    public void testObjectCreation() {
        Area area = new Area();
        Assert.assertNotNull(area);
    }

    public void testSave() {
        long id = generateArea().getId();
        Assert.assertTrue(id >= 0);
    }

    public void testLoad() {
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


   public void testUpdate() {
        // create an item and get it's id
       long id = generateArea().getId();
        // the user would load an item by id
       Area area = Area.load(Area.class, id);
       assertNotNull(area);
        // then the user makes a change and saves
       area.name = "Alli";
       area.updated_at = "2001";
       area.save();
       area = null;
        // now reload the object  by id and check to see if it was changed
       area = Area.load(Area.class, id);
       assertTrue(area.name.equals("Alli"));
       assertTrue(area.updated_at.equals("2001"));
       assertTrue(area.created_at.equals("now"));
    }

   public void testDelete() {
            // create an item and get it's id
        long id = generateArea().getId();
        // the user would load an item by id usually in a use situation so we will here
        Area area = Area.load(Area.class, id);
        assertNotNull(area);
        // delete the object
        area.delete();
        area = null;
        // we should then try and reload the object and get null
       area = Area.load(Area.class, id);
       assertNull(area);
    }

    /****FUNCTIONS****/
    private Area generateArea() {
        Area area = new Area();
        area.name = "Jimmy";
        area.created_at = "now";
        area.updated_at = "then";
        area.save();

        return area;
    }

}
