package LocalDatabase.tests;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.activeandroid.query.Select;

import java.util.List;

import LocalDatabase.Area;
import LocalDatabase.DatabasePopulator;
import LocalDatabase.Household;

/**
 * Created by Chad Carey on 10/6/2014.
 */
public class TestDatabasePopulator extends InstrumentationTestCase {

    DatabasePopulator populator;

    @Override
    public void setUp() {
        populator = new DatabasePopulator();
        populator.populate();
    }

    public void testAreaPopulator() {
        DatabasePopulator populator = new DatabasePopulator();
        populator.populate();
        List<Area> list = Area.getAllAreas();
        assertNotNull(list);
        assertTrue(list.size() > 0);
        for(Area area : list) {
            Log.d("TestDatabasePopulator : areaPopulator", "Included in area list: " + area.name);
        }
    }

    public void testHouseholdPopulator() {
        List<Household> householdList = new Select().from(Household.class).execute();
        assertNotNull(householdList);
        assertTrue(householdList.size() > 0);
        for(Household household : householdList) {
            Log.d("TestDatabasePopulator : householdPopulator", "\n\thousehold name:" + household.name +
            "\n\thousehold area name:" + household.area.name);
        }
    }
}
