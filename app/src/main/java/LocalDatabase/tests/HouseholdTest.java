package LocalDatabase.tests;

import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import junit.framework.Assert;

import java.util.List;

import LocalDatabase.Area;
import LocalDatabase.Household;

/**
 * Created by Chad Carey on 10/1/2014.
 */
public class HouseholdTest extends AndroidTestCase {


    public void testHouseholdTestObjectCreation() {
        Household household = new Household();
        assertNotNull(household);
    }

    public void testSaveHousehold() {
        Household household = generateHousehold();
        long areaId = household.area.getId();
        Log.d("HouseholdTest: testSaveHousehold", "area id = " + Long.toString(areaId));
        long id = household.getId();
        Log.d("HouseholdTest: testSaveHousehold", "household id = " + Long.toString(id));
        assertTrue(id >= 0);
    }

    public void testLoadHousehold() {
        // add a house to the database for testing, save is and areaId
        Household household = generateHousehold();
        long id = household.getId();
        long areaId = household.area.getId();
        household = null;

        // now test load
        Log.d("HouseholdTest: testLoadHousehold", "household id = " + Long.toString(id));
        household = Household.load(Household.class, id);
        assertNotNull(household);
        assertTrue(household.name.equals("Johns"));
        assertTrue(household.created_at.equals("1980"));
        assertTrue(household.updated_at.equals("1981"));
        assertTrue(household.area.getId() == areaId);
    }

    public void testUpdate() {
        // create an item and get it's id
        long id = generateHousehold().getId();
        // the user would load an item by id
        Household household = Household.load(Household.class, id);
        // then the user makes a change and saves
        household.name = "another name";
        household.updated_at = "2000";
        household.save();
        household = null;
        // now reload the object  by id and check to see if it was changed
        household = Household.load(Household.class, id);
        assertTrue(household.name.equals("another name"));
        assertTrue(household.updated_at.equals("2000"));
        assertTrue(household.created_at.equals("1980"));
    }

    public void testDelete() {
        // create an item and get it's id
        long id = generateHousehold().getId();
        // the user would load an item by id usually in a use situation so we will here
        Household household = Household.load(Household.class, id);
        assertNotNull(household);
        // delete the object
        household.delete();
        household = null;
        // we should then try and reload the object and get null
        household = Household.load(Household.class, id);
        assertNull(household);
    }

    public void testGetHouseholdByArea() {
        Household household1 = generateHousehold();
        Area area = household1.area;
        long id = area.getId();
        Household household2 = generateHousehold();
        household2.area = area;
        household2.name = "Jimmy";
        household2.save();
        household1 = null;
        household2 = null;

        List<Household> list = Household.getHousehold(id);

        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertTrue(list.size() == 2);
        assertTrue((list.get(0).name).equals("Johns"));
        assertTrue(list.get(1).name.equals("Jimmy"));
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

    public Household generateHousehold() {
        Household household = new Household();
        household.area = generateArea();
        household.name = "Johns";
        household.created_at = "1980";
        household.updated_at = "1981";

        household.save();
        return household;
    }



}
