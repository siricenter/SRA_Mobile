package LocalDatabase.tests;

import android.test.InstrumentationTestCase;

import junit.framework.Assert;

import LocalDatabase.Household;
import LocalDatabase.Interview;

/**
 * Created by Chad Carey on 10/1/2014.
 */
public class InterviewTest extends InstrumentationTestCase {

    Interview interview;

    public void setUp() {
        interview = new Interview();
    }
    public void testInterviewObjectCreation() {
        assertNotNull(interview);
    }

    public void testSave() {
        Household household = new Household();
        household.save();

        interview.household = household;
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
        long id = interview.getId();
        assertFalse(id < 0);

        interview = null;
        interview = Interview.getById(id);


        Assert.assertTrue(interview.household.getId() == household.getId());
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

}
