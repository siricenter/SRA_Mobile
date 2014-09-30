package LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Time;

/**
 * This is an object representing the SRADatabase
 * NOTE: this is a singleton. use getInstance(Context appContext) to get and instance of this class
 * Created by Chad Carey on 9/29/2014.
 */
public class SRADatabase extends SQLiteOpenHelper {

    private static SRADatabase instance = null;

    public static SRADatabase getInstance(Context appContext) {

        if(instance == null) {
            instance = new SRADatabase(appContext);
        }

        return instance;

    }

    private SRADatabase(Context appContext) {
        super(appContext, "sra.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        if( !database.isOpen() ) {
            Log.d( "SRADatabase: onCreate", "database was not open");
        }

        /*
            Table: areas
            Values: name TEXT, created_at DATETIME, updated_at DATETIME
         */
        String query = "CREATE TABLE areas ( id INTEGER PRIMARY KEY, name TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME DATETIME DEFAULT CURRENT_TIMESTAMP )";
        database.execSQL(query);

        /*
            Table:
            Values:
         */

        /*
            Table:
            Values:
         */
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i2) {

    }

    /****GETTERS****/

    /**
     * returns an object representing the area table
     */
    public AreaTable getAreaTable() {
        AreaTable table = new AreaTable(this);
        return table;
    }


    /****SETTERS / INSERTS****/
    public void insertArea(String areaName) {
        Log.d("SRADatabase: tableExists()", "getting writable database");
        SQLiteDatabase database = this.getWritableDatabase();

        if (database != null) {
            ContentValues values = new ContentValues();

            values.put("name", areaName);
           // values.put("created_at", );
           // values.put("updated_at", );
        }
        database.close();
    }

    /****MODIFIES****/

    /****OTHER FUNCTIONS****/
    public Boolean tableExists(String table){
        Log.d("SRADatabase: tableExists()", "getting writable database");
        SQLiteDatabase database = this.getWritableDatabase();
        if (database == null) {
            Log.e("SRADatabase: tableExists()", "database == null");
            return false;
        }
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + table + "'";

        Cursor cursor = database.rawQuery(query, null);
        try {
            cursor.moveToFirst();
            String name = cursor.getString(0);

            if (name != null) {
                Log.d("SRADatabase: tableExists()", "Table exists");
                database.close();
                cursor.close();
                return true;
            } else {
                database.close();
                cursor.close();
                return false;
            }
        } catch (Exception e) {
            database.close();
            cursor.close();
            return false;
        }
    }

}
