package LocalDatabase;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by jakobhartman on 10/1/14.
 */
@Table(name = "areas")
public class Area extends SRAModel {
    @Column(name = "name")
    public String name;
    @Column(name = "created_at")
    public String created_at;
    @Column(name = "updated_at")
    public String updated_at;

    public Area() {
        super();
    }


    public static Area getByName(String name) {
        return new Select().from(Area.class).where("name='" + name+ "'").executeSingle();
    }

}
