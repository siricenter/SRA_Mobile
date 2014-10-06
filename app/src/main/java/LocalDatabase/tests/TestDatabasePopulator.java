package LocalDatabase.tests;

import android.test.InstrumentationTestCase;
import android.util.Log;

import java.util.List;

import LocalDatabase.Area;
import LocalDatabase.DatabasePopulator;

/**
 * Created by Chad Carey on 10/6/2014.
 */
public class TestDatabasePopulator extends InstrumentationTestCase {

    DatabasePopulator populator = new DatabasePopulator();

    public void testAreaPopulator() {
        populator.populateAreas();
        List<Area> list = Area.getAllAreas();
        for(Area area : list) {
            Log.d("TestDatabasePopulator : areaPopulator", "Included in area list: " + area.name);
        }
    }
}
