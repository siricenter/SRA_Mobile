package LocalDatabase;

import LocalDatabase.Area;

/**
 * Created by Chad Carey on 10/6/2014.
 */
public class DatabasePopulator {

    final public static String[] AREA_NAMES = {
        "Africa", "South America", "Central America", "Asia", "Europe", "North America"
    };

    public void populateAreas() {
        for(String areaName : AREA_NAMES) {
            Area area = new Area();
            area.name = areaName;
            area.created_at = "now";
            area.updated_at = "now";
            area.save();
        }
    }
}
