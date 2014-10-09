package LocalDatabase;

import android.util.Log;

import com.activeandroid.query.Delete;

import java.util.List;

import LocalDatabase.Area;

/**
 * Created by Chad Carey on 10/6/2014.
 */
public class DatabasePopulator {

    final public static String[] AREA_NAMES = {
        "Africa", "South America", "Central America", "Asia", "Europe", "North America"
    };

    final public static String[] HOUSEHOLD_NAMES = {
            "Katana", "Applesmith", "Johnson", "Gonzalez", "Hartman", ""
    };

    public void populate() {
        populateAreas();
        populateHouseholds();
        //populateInterviews();
    }

    public void deleteAll() {
        new Delete().from(Interview.class).execute();
        new Delete().from(Household.class).execute();
        new Delete().from(Area.class).execute();
    }

    public void dropAll() {
        
    }

    public void populateAreas() {
        for(String areaName : AREA_NAMES) {
            Area area = new Area();
            area.name = areaName;
            area.created_at = "now";
            area.updated_at = "now";
            try {
                area.save();
            } catch (Exception e) {
                Log.d("DatabasePopulator: populateAreas()","Already In the Database");
            }
        }
    }

    public void populateHouseholds() {
        List<Area> areaList = Area.getAllAreas();
        for(String householdName : HOUSEHOLD_NAMES) {
            Household household = new Household();
            household.name = householdName;
            household.area = areaList.get((int)(Math.random() * (areaList.size()-1)));
            household.created_at = "now";
            household.updated_at = "now";
            household.percent = 0;
            household.save();
        }
    }

    private void populateInterviews(){
        // to be created
    }
}
