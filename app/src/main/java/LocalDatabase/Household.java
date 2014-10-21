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
public class Household extends SRAModel {

	@Column(name = "name")
    public String name;
    @Column(name = "created_at")
    public String created_at;
    @Column(name = "updated_at")
    public String updated_at;
    @Column(name = "area")
    public Area area;
    @Column(name = "percent")
    public int percent;
    @Column(name = "db_id")
    public long id;
    /**
     * IMPORTANT INFO READ THIS!!!
     * This percentage is the percentage of the household's interview.
     * For the prototype the household only has one interview and we need the percentage
     * of the household's single interview available in the view along with the interview name.
     * In the future we will tie these percentages to each interview  itself and pull up
     * all interviews associated with the household with these percentages.
     * For the prototype it needs to be here.
     */

    public Household() {
        super();
    }

    public static List<Household> getHousehold(long areaId) {
        List<Household> list = new Select().from(Household.class).where("area="+areaId).execute();
        return list;
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

    public  static List<Household> getHouseholdByName(String newname){
        return new Select().from(Household.class).where("name='" + newname +"'").execute();
    }


}