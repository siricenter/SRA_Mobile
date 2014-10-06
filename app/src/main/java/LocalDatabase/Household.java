package LocalDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Chad Carey on 10/1/2014.
 */
@Table(name = "households")
public class Household extends Model {

	@Column(name = "name")
    public String name;
    @Column(name = "created_at")
    public String created_at;
    @Column(name = "updated_at")
    public String updated_at;
    @Column(name = "area")
    public Area area;

    public Household() {
        super();
    }

    public static List<Household> getHousehold(long areaId) {
        List<Household> list = new Select("name").from(Model.class).where("area="+areaId).execute();
        return list;
    }

}