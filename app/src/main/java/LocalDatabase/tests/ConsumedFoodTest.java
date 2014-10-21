package LocalDatabase.tests;

import android.test.AndroidTestCase;
import android.util.Log;

import LocalDatabase.ConsumedFood;
import LocalDatabase.DatabasePopulator;

/**
 * Created by Chad Carey on 10/1/2014.
 */
public class ConsumedFoodTest extends AndroidTestCase {

    DatabasePopulator populator = new DatabasePopulator();

    public void testObjectCreation() {
        ConsumedFood food = new ConsumedFood();
        assertNotNull(food);
    }
    public void testSave() {
        ConsumedFood food = populator.generateDummyFood();
        assertNotNull(food);
    }

    public void testLoad() {
        ConsumedFood food = populator.generateDummyFood();
        assertNotNull(food);
        long id = food.getId();
        food = null;
        try {
            food = ConsumedFood.load(ConsumedFood.class, id);
        } catch (Exception e) {
            Log.e("ConsumedFood : testLoad", "failed to load food");
            food = null;
        }
        assertNotNull(food);

        assertTrue(food.servings == 2);
        assertTrue(food.frequency.equals("daily"));
        assertTrue(food.created_at.equals("now"));
        assertTrue(food.updated_at.equals("now"));
    }

    public void testUpdate() {
        ConsumedFood food = populator.generateDummyFood();
        long id = food.getId();
        food.servings = 100;
        food.updated_at = "2001";
        food.frequency = "once";
        food.save();
        food = null;

        try {
            food = ConsumedFood.load(ConsumedFood.class, id);
        } catch (Exception e) {
            Log.e("ConsumedFood : testLoad", "failed to load food");
            food = null;
        }
        assertNotNull(food);

        assertTrue(food.servings == 100);
        assertTrue(food.frequency.equals("once"));
        assertTrue(food.created_at.equals("now"));
        assertTrue(food.updated_at.equals("2001"));
    }

    public void testDelete() {
        ConsumedFood food = populator.generateDummyFood();
        long id = food.getId();

        try {
            food.delete();
        } catch (Exception e) {
            Log.e("ConsumedFood: testDelete", "failed to delete food");
            assertTrue(false);
            return;
        }

        try {
            food = ConsumedFood.load(ConsumedFood.class, id);
        } catch (Exception e) {
            Log.d("ConsumedFood: testDelete", "failed to load food after delete");
            assertTrue(true);
        }
    }

    public void testPost() {
        ConsumedFood subject = new ConsumedFood();

        subject.entered_food = "carrot";

        long id = subject.post();
        assertTrue(id >=0 );
        Log.d("ConsumedFoodTest : testPost", "created_at = " + subject.created_at);
        assertNotNull(subject.created_at);
        assertFalse(subject.created_at.isEmpty());
        Log.d("ConsumedFoodTest : testPost", "updated_at = " + subject.updated_at);
        assertNotNull(subject.updated_at);
        assertFalse(subject.updated_at.isEmpty());
        // test second post to be sure it only changes updated_at
        String oldDate = subject.created_at;
        subject = null;
        subject = ConsumedFood.load(ConsumedFood.class, id);
        assertNotNull(subject);

        // re-save (post)
        subject.entered_food = "spin";
        subject.post();

        Log.d("ConsumedFoodTest : testPost", "created_at2 = " + subject.created_at);
        assertNotNull(subject.created_at);
        assertFalse(subject.created_at.isEmpty());
        Log.d("ConsumedFoodTest : testPost", "updated_at2 = " + subject.updated_at);
        assertNotNull(subject.updated_at);
        assertFalse(subject.updated_at.isEmpty());

        assertTrue(subject.created_at.equals(oldDate));
        assertFalse(subject.updated_at.equals(oldDate));
    }

}
