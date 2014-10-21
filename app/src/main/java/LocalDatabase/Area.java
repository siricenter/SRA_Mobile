package LocalDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Chad Carey on 10/1/2014.
 */
@Table(name = "sra_areas")
public class Area extends SRAModel {

    @Column(name = "name", unique = true,onUniqueConflict = Column.ConflictAction.IGNORE)
    public String name;
    @Column(name = "db_id")
    public long id;
    @Column(name = "created_at")
    public String created_at;
    @Column(name = "updated_at")
    public String updated_at;

    public Area() {
        super();
    }

    /**
     * post
     * This method will save the item to the database and also generate the correct created_at and
     * updated_at dates
     * @return
     */
    public long post() {
        String date = this.generateTimestamp();
        // check to see if a created_at date already exists
        if(created_at == null) {
            // if created_at date doesn't exist create it
            this.created_at = date;
        } else if(created_at.isEmpty()) {
            this.created_at = date;
        }
        // create updated_at date
        this.updated_at = date;
        return this.save();
    }

    public static List<Area> getAllAreas() {
        return new Select().from(Area.class).execute();
    }

}