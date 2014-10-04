package LocalDatabase.tests;

import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;

import junit.framework.Assert;

import LocalDatabase.Area;
import LocalDatabase.Household;
import LocalDatabase.Interview;

/**
 * Created by Chad Carey on 10/1/2014.
 */
public class InterviewTest extends InstrumentationTestCase {


    public void testInterviewObjectCreation() {
        Interview interview = new Interview();
        assertNotNull(interview);
    }

    public void testSave() {
        long id = generateInterview().getId();
        assertTrue(id >= 0);
    }

    public void testLoad() {
        Interview interview = generateInterview();
        long householdId = interview.household.getId();
        long id = interview.getId();

        interview = null;

        interview = Interview.load(Interview.class, id);

        Assert.assertTrue(interview.household.getId() == householdId);
        Assert.assertTrue(interview.roof.equals("none"));
        Assert.assertTrue(interview.wall.equals("wood"));
        Assert.assertTrue(interview.floor.equals("gold"));
        Assert.assertTrue(interview.bedroomCount.equals("200"));
        Assert.assertTrue(interview.separateKitchen.equals("many"));
        Assert.assertTrue(interview.light.equals("the sun"));
        Assert.assertTrue(interview.fuelType.equals("potato chips"));
        Assert.assertTrue(interview.waterSource.equals("the sky"));
        Assert.assertTrue(interview.waterChlorinated.equals("very"));
        Assert.assertTrue(interview.bathroom.equals("a pit"));
        Assert.assertTrue(interview.sewage.equals("lots of it"));
        Assert.assertTrue(interview.personCount.equals("1"));
        Assert.assertTrue(interview.totalIncome.equals( "100"));
        Assert.assertTrue(interview.incomeUnit.equals("2"));
        Assert.assertTrue(interview.shoeCost.equals("20"));
        Assert.assertTrue(interview.shoeUnit.equals("20"));
        Assert.assertTrue(interview.medicineCost.equals("30"));
        Assert.assertTrue(interview.medicineUnit.equals("30"));
        Assert.assertTrue(interview.schoolCost.equals("5"));
        Assert.assertTrue(interview.schoolUnit.equals("5"));
        Assert.assertTrue(interview.collegeCost.equals("4"));
        Assert.assertTrue(interview.collegeUnit.equals("4"));
        Assert.assertTrue(interview.waterElectricCost.equals("3"));
        Assert.assertTrue(interview.waterElectricUnit.equals("3"));
        Assert.assertTrue(interview.misc_cost.equals("2"));
        Assert.assertTrue(interview.miscUnit.equals("1"));
        Assert.assertTrue(interview.radio.equals("7"));
        Assert.assertTrue(interview.tv.equals("6"));
        Assert.assertTrue(interview.refrigerator.equals("9"));
        Assert.assertTrue(interview.created_at.equals("now"));
        Assert.assertTrue(interview.updated_at.equals("then"));
    }

    public void testUpdate() {
        Interview interview = generateInterview();
        long id = interview.getId();

        interview.household = generateHousehold();
        long householdId = interview.household.getId();
        interview.roof = "silver";
        interview.wall = "gold";
        interview.floor = "brick";
        interview.bedroomCount = "1000";
        interview.separateKitchen = "fewer";
        interview.light = "spot lights";
        interview.fuelType = "carrots";
        interview.waterSource = "rain";
        interview.waterChlorinated = "90%";
        interview.bathroom = "1";
        interview.sewage = "toilet";
        interview.personCount = "20";
        interview.totalIncome = "1000";
        interview.incomeUnit = "10";
        interview.shoeCost = "50";
        interview.shoeUnit = "50";
        interview.medicineCost = "100";
        interview.medicineUnit = "200";
        interview.schoolCost = "500";
        interview.schoolUnit = "50";
        interview.collegeCost = "400";
        interview.collegeUnit = "40";
        interview.waterElectricCost = "300";
        interview.waterElectricUnit = "30";
        interview.misc_cost = "20";
        interview.miscUnit = "10";
        interview.radio = "70";
        interview.tv = "60";
        interview.refrigerator = "90";

        interview.updated_at = "2001";

        interview.save();

        interview = null;

        // reload the interview
        interview = Interview.load(Interview.class, id);
        // check for the new values
        assertTrue(interview.household.getId() == householdId);
        assertTrue(interview.roof.equals("silver"));
        assertTrue(interview.wall.equals("gold"));
        assertTrue(interview.floor.equals("brick"));
        assertTrue(interview.bedroomCount.equals("1000"));
        assertTrue(interview.separateKitchen.equals("fewer"));
        assertTrue(interview.light.equals("spot lights"));
        assertTrue(interview.fuelType.equals("carrots"));
        assertTrue(interview.waterSource.equals("rain"));
        assertTrue(interview.waterChlorinated.equals("90%"));
        assertTrue(interview.bathroom.equals("1"));
        assertTrue(interview.sewage.equals("toilet"));
        assertTrue(interview.personCount.equals("20"));
        assertTrue(interview.totalIncome.equals("1000"));
        assertTrue(interview.incomeUnit.equals("10"));
        assertTrue(interview.shoeCost.equals("50"));
        assertTrue(interview.shoeUnit.equals("50"));
        assertTrue(interview.medicineCost.equals("100"));
        assertTrue(interview.medicineUnit.equals("200"));
        assertTrue(interview.schoolCost.equals("500"));
        assertTrue(interview.schoolUnit.equals("50"));
        assertTrue(interview.collegeCost.equals("400"));
        assertTrue(interview.collegeUnit.equals("40"));
        assertTrue(interview.waterElectricCost.equals("300"));
        assertTrue(interview.waterElectricUnit.equals("30"));
        assertTrue(interview.misc_cost.equals("20"));
        assertTrue(interview.miscUnit.equals("10"));
        assertTrue(interview.radio.equals("70"));
        assertTrue(interview.tv.equals("60"));
        assertTrue(interview.refrigerator.equals("90"));

        assertTrue(interview.updated_at.equals("2001"));
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

    public Household generateHousehold() {
        Household household = new Household();
        household.area = generateArea();
        household.name = "Johns";
        household.created_at = "1980";
        household.updated_at = "1981";

        household.save();
        return household;
    }

    public Interview generateInterview() {
        Interview interview = new Interview();
        interview.household = generateHousehold();
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

    public void testDelete() {
        // create an item and get it's id
        long id = generateInterview().getId();
        // the user would load an item by id usually in a use situation so we will here
        Interview interview = Interview.load(Interview.class, id);
        assertNotNull(interview);
        // delete the object
        interview.delete();;
        interview = null;
        // we should then try and reload the object and get null
        interview = Interview.load(Interview.class, id);
        assertNull(interview);
    }

}
