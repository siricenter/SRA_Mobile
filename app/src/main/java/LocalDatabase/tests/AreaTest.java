package LocalDatabase.tests;

import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import java.util.List;

import LocalDatabase.Area;

/**
 * Created by Chad Carey on 10/3/2014.
 */
public class AreaTest extends AndroidTestCase {


    public void testObjectCreation() {
        Area area = new Area();
        assertNotNull(area);
    }

    public void testSave() {
        Area area = generateArea();
        assertNotNull(area);
        long id = area.getId();
        assertTrue(id >= 0);
    }

    public void testLoad() {
        // add a house to the database for testing, save is and areaId
        Area area = generateArea();
        assertNotNull(area);
        long id = area.getId();
        area = null;

        // now test load
        area = Area.load(Area.class, id);
        assertNotNull(area);
        assertTrue(area.name.split(" ")[0].equals("Jimmy"));
        assertTrue(area.created_at.equals("now"));
        assertTrue(area.updated_at.equals("then"));
    }

    public void testUpdate() {
        // create an item and get it's id
        long id = generateArea().getId();
        // the user would load an item by id
        Area area = Area.load(Area.class, id);
        // then the user makes a change and saves
        String saveName = "another name " + Math.random();
        area.name = saveName;
        area.updated_at = "2000";
        area.save();
        area = null;
        // now reload the object  by id and check to see if it was changed
        area = Area.load(Area.class, id);
        assertTrue(area.name.equals(saveName));
        assertTrue(area.updated_at.equals("2000"));
        assertTrue(area.created_at.equals("now"));
    }

    public void testDelete() {

        Area area = generateArea();
        assertNotNull(area);
        long id = area.getId();
        assertTrue(id >=0);
        // delete the object
        try {
        area.delete();
        } catch (Exception e) {
            area = null;
            Log.e("AreaTest : test Delete", "failed to delete area");
            assertTrue(false);
            return;
        }
        // we should then try and reload the object and get null
        try {
            area = Area.load(Area.class, id);
        } catch (Exception e) {
            area = null;
            Log.d("AreaTest : test Delete", "Area was not found after delete");
        }
        assertNull(area);
    }

    public void testGetAllAreas() {
        generateArea();
        List<Area> list = Area.getAllAreas();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertTrue(list.size() > 0);
    }

    public void testPsot() {
        Area area = new Area();
        area.name = "Andrew";
        long id = area.post();
        assertTrue(id >=0);
        Log.d("AreaTest : testPost", "created_at = " + area.created_at);
        Log.d("AreaTest : testPost", "updated_at = " + area.updated_at);
    }

    /****FUNCTIONS****/
    public Area generateArea() {
        try {
            Area area = new Area();
            area.name = "Jimmy " + Math.random();
            area.created_at = "now";
            area.updated_at = "then";
            area.save();
            long areaId = area.getId();
            Log.d("AreaTest: generateArea", "area id = " + Long.toString(areaId));
            return area;
        } catch (Exception e) {
            Log.e("AreaTest : generateArea","Failed to create area");
            return null;
        }
    }

}
