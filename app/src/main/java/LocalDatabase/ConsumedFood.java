package LocalDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Chad Carey on 10/1/2014.
 */
@Table(name = "consumed_foods")
public class ConsumedFood extends SRAModel {

    @Column(name = "n_id")
    public String n_id;
    @Column(name = "db_id")
    public long id;
    @Column(name = "entered_food")
    public String entered_food;
    @Column(name = "interview")
    public Interview interview;
    @Column(name = "servings")
    public float servings;
    @Column(name = "units")
    public String units;
    @Column(name = "quantity")
    public int quantity;
    @Column(name = "frequency")
    public String frequency;
    @Column (name = "created_at")
    public String created_at;
    @Column (name = "updated_at")
    public String updated_at;

    public ConsumedFood() {
        super();
        n_id = "";
        entered_food = "";
        interview = null;
        servings = -1;
        units = "";
        quantity = 1;
        frequency = "";
        created_at = "";
        updated_at = "";
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

    public static List<ConsumedFood> getConsumedFoods(long interviewID){
        return new Select().from(ConsumedFood.class).where("interview=" + interviewID).execute();
    }
}
