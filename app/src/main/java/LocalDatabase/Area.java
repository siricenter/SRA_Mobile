package LocalDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Chad Carey on 10/1/2014.
 */
@Table(name = "sra_areas")
public class Area extends Model {

    @Column(name = "name")
    public String name;
    @Column(name = "created_at")
    public String created_at;
    @Column(name = "updated_at")
    public String updated_at;

    public Area() {
        super();
    }

}