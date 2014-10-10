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

}
