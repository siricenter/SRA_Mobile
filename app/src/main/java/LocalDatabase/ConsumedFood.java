package LocalDatabase;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by Chad Carey on 10/1/2014.
 */
@Table(name = "consumed_foods")
public class ConsumedFood extends SRAModel {

    //@Column(name = "")
    //Interview interview;
    //@Column(name = "")
    //n_id ??
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

    public static ConsumedFood getFoodById(long id) {
        return new Select().from(ConsumedFood.class).where("id=" + Long.toString(id)).executeSingle();
    }

    /*public static ConsumedFood getFoodsByInterview(Interview in) {
        return new Select().from(ConsumedFood.class).where("id=" + Long.toString(id)).execute();
    }*/
}
