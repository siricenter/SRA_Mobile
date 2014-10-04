package LocalDatabase.tests;

import android.test.AndroidTestCase;
import android.util.Log;

import junit.framework.Assert;

import java.util.Calendar;

import LocalDatabase.Person;

/**
 * Created by jordanreed on 10/2/14.
 */
public class PersonTest extends AndroidTestCase {
    private Person person = null;
    private long id;
    private String given_name;
    private String family_name;

    public void setUp() {
        given_name = "Jimmy";
        family_name = "Ivanovich";
        person = new Person(given_name, family_name);
    }

    public void testCreate() {
        Assert.assertNotNull(person);
        person.save();
        id = person.getId();
        Assert.assertTrue(id > 0);

        Person queriedPerson = Person.load(Person.class, id);
        Assert.assertNotNull(queriedPerson);
        Assert.assertEquals(queriedPerson.given_name, given_name);
        Assert.assertEquals(queriedPerson.family_name, family_name);
    }

    public void testUpdate() {
        given_name = "Jordan";
        family_name = "Reed";
        int familyRelationshipId = 1;
        String birthday = "7/7/7";
        String educationLevel = "some college";
        String gender = "male";
        Boolean inSchool = true;
        Boolean isAlive = true;
        String createdAt = "12:00";
        String updatedAt = "13:00";
        int household = 1;

        person.given_name = given_name;
        person.family_name = family_name;
        person.family_relationship_id = familyRelationshipId;
        person.birthday = birthday;
        person.education_level = educationLevel;
        person.gender = gender;
        person.in_school = inSchool;
        person.is_alive = true;
        person.created_at = createdAt;
        person.updated_at = updatedAt;
        person.household_id = household;
        person.save();
        id = person.getId();
        Assert.assertTrue(id > 0);

        Person queriedPerson = Person.load(Person.class, id);
        Assert.assertNotNull(queriedPerson);
        Assert.assertEquals(queriedPerson.given_name, given_name);
        Assert.assertEquals(queriedPerson.family_name, family_name);
        Assert.assertEquals(queriedPerson.family_relationship_id, familyRelationshipId);
        Assert.assertEquals(queriedPerson.birthday, birthday);
        Assert.assertEquals(queriedPerson.education_level, educationLevel);
        Assert.assertEquals(queriedPerson.gender, gender);
        Assert.assertEquals(queriedPerson.in_school, inSchool);
        Assert.assertEquals(queriedPerson.is_alive, isAlive);
        Assert.assertEquals(queriedPerson.created_at, createdAt);
        Assert.assertEquals(queriedPerson.updated_at, updatedAt);
        Assert.assertEquals(queriedPerson.household_id, household);
    }
}