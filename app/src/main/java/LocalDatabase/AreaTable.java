package LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.util.HashMap;

/**
 * Created by Chad Carey on 9/29/2014.
 */
public class AreaTable {

    SRADatabase database;

    public AreaTable(Context context) {
        this.database = SRADatabase.getInstance(context);
    }

    public AreaTable(SRADatabase database) {
        this.database = database;
    }

    /****GETTERS****/
    public void getArea(int id) {

    }

    /**
     * returns a hashmap of the area object
     * @param name
     * @return
     */
    public HashMap <String,String> getArea(String name) {
        HashMap <String, String> map = new HashMap<String, String>();
        String query = "SELECT * FROM areas WHERE name='" + name + "'";

        SQLiteDatabase d = database.getWritableDatabase();

        Cursor cursor = d.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                // Store the key / value pairs in a HashMap
                // Access the Cursor data by index that is in the same order
                // as used when creating the table
                map.put("id", cursor.getString(0));
                map.put("name", cursor.getString(1));
                map.put("created_at", cursor.getString(2));
                map.put("updated_at", cursor.getString(3));
            }
        } catch (Exception e) {
            Log.e("AreaTavle: getArea()", "Failed to load cursor");
            map = null;
        } finally {
            d.close();
            return map;
        }
    }

    public String getAreaName(int id) {
        return null;
    }

    public void getAll() {

    }

    /****SETTERS / INSERT ****/
    /**
     *
     * @param name
     * @return areaId
     */
    public long insertArea(String name) {
        long id = 0;
        SQLiteDatabase d = database.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);


        id = d.insert("areas", null, values);
        d.close();
        return id;
    }

    /**
     * Inserts and area into the database, allows override of the created_at and updated_at values
     * @param name
     * @return areaId
     */
    public int insertArea(String name, String created_at, String updated_at) {
        int id = 0;

        return id;
    }

}
