package LocalDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by jakobhartman on 10/1/14.
 */
@Table(name = "Areas")
public class Area extends SRAModel {

    public Area() {
        super();
    }

    public static Area getAreaById(long id) {
        return new Select().from(Area.class).where("id=" + Long.toString(id)).executeSingle();
    }

    public static Area getAreaByName(String name) {
        return new Select().from(Area.class).where("name='" + name+ "'").executeSingle();
    }

    @Column(name = "name")
    public String name;
    @Column(name = "created_at")
    public String created_at;
    @Column(name = "updated_at")
    public String updated_at;
}
