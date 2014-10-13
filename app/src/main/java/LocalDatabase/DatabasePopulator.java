package LocalDatabase;

import android.util.Log;

import com.activeandroid.query.Delete;
import java.util.List;

import LocalDatabase.Area;

/**
 * Created by Chad Carey on 10/6/2014.
 */
public class DatabasePopulator {
    SRAModel model = new SRAModel();

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
            area.created_at = model.generateTimestamp();
            area.updated_at = model.generateTimestamp();
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
            household.percent = 0;
            household.created_at = model.generateTimestamp();
            household.updated_at = model.generateTimestamp();
            household.save();
        }
    }

    public Area generateDummyArea() {
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

    public Household generateDummyHousehold() {
        Household household = new Household();
        household.area = generateDummyArea();
        household.name = "Johns";
        household.created_at = "1980";
        household.updated_at = "1981";

        household.save();
        return household;
    }

    public Interview generateDummyInterview() {
        Interview interview = new Interview();
        interview.household = generateDummyHousehold();
        interview.roof = "none";
        interview.wall = "wood";
        interview.floor = "gold";
        interview.bedroomCount = "200";
        interview.separateKitchen = "many";
        interview.light = "the sun";
        interview.fuelType = "potato chips";
        interview.waterSource = "the sky";
        interview.waterChlorinated = "very";
        interview.bathroom = "a pit";
        interview.sewage = "lots of it";
        interview.personCount = "1";
        interview.totalIncome = "100";
        interview.incomeUnit = "2";
        interview.shoeCost = "20";
        interview.shoeUnit = "20";
        interview.medicineCost = "30";
        interview.medicineUnit = "30";
        interview.schoolCost = "5";
        interview.schoolUnit = "5";
        interview.collegeCost = "4";
        interview.collegeUnit = "4";
        interview.waterElectricCost = "3";
        interview.waterElectricUnit = "3";
        interview.misc_cost = "2";
        interview.miscUnit = "1";
        interview.radio = "7";
        interview.tv = "6";
        interview.refrigerator = "9";
        interview.created_at = "now";
        interview.updated_at = "then";

        interview.save();

        return interview;
    }

    public ConsumedFood generateDummyFood() {
        try {
            ConsumedFood food = new ConsumedFood();
            food.frequency = "daily";
            food.servings = 2;
            food.created_at = "now";
            food.updated_at = "now";
            food.interview = generateDummyInterview();
            food.save();
            Log.d("ConsumedFood: generateFood", "Id = " + food.getId());
            return food;
        } catch (Exception e) {
            Log.e("ConsumedFood: generateFood", "Failed to create object");
            return null;
        }

    }

    private void populateInterviews(){
        // to be created
    }
}
