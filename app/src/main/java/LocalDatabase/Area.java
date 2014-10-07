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
public class Area extends Model {

    @Column(name = "name", unique = true)
    public String name;
    @Column(name = "created_at")
    public String created_at;
    @Column(name = "updated_at")
    public String updated_at;

    public Area() {
        super();
    }

    public static List<Area> getAllAreas() {
        return new Select().from(Area.class).execute();
    }

}