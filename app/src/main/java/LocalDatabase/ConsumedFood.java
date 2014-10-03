package LocalDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Chad Carey on 10/1/2014.
 */
@Table(name = "consumed_foods")
public class ConsumedFood extends Model {

    @Column(name = "servings")
    public int servings;
    @Column(name = "frequency")
    public String frequency;
    @Column (name = "created_at")
    public String created_at;
    @Column (name = "updated_at")
    public String updated_at;


    public ConsumedFood() {
        super();
    }
}
