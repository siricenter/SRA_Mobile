package LocalDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Chad Carey on 10/1/2014.
 */
@Table(name = "interviews")
public class Interview extends SRAModel {
    @Column(name = "household")
    public Household household;
    @Column(name = "roof")
    public String roof;
    @Column(name = "wall")
    public String wall;
    @Column(name = "floor")
    public String floor;
    @Column(name = "bedroom_count")
    public String bedroomCount;
    @Column(name = "separate_kitchen")
    public String separateKitchen;
    @Column(name = "light")
    public String light;
    @Column(name = "fuel_type")
    public String fuelType;
    @Column(name = "water_source")
    public String waterSource;
    @Column(name = "water_chlorinated")
    public String waterChlorinated;
    @Column(name = "bathroom")
    public String bathroom;
    @Column(name = "sewage")
    public String sewage;
    @Column(name = "person_count")
    public String personCount;
    @Column(name = "total_income")
    public String totalIncome;
    @Column(name = "income_unit")
    public String incomeUnit;
    @Column(name = "shoe_cost")
    public String shoeCost;
    @Column(name = "shoe_unit")
    public String shoeUnit;
    @Column(name = "medicine_cost")
    public String medicineCost;
    @Column(name = "medicine_unit")
    public String medicineUnit;
    @Column(name = "school_cost")
    public String schoolCost;
    @Column(name = "school_unit")
    public String schoolUnit;
    @Column(name = "college_cost")
    public String collegeCost;
    @Column(name = "college_unit")
    public String collegeUnit;
    @Column(name = "water_electric_cost")
    public String waterElectricCost;
    @Column(name = "water_electric_unit")
    public String waterElectricUnit;
    @Column(name = "misc_cost")
    public String misc_cost;
    @Column(name = "misc_unit")
    public String miscUnit;
    @Column(name = "radio")
    public String radio;
    @Column(name = "tv")
    public String tv;
    @Column(name = "refrigerator")
    public String refrigerator;
    @Column(name = "db_id")
    public long id;

    @Column(name = "created_at")
    public String created_at;
    @Column(name = "updated_at")
    public String updated_at;

    public Interview() {
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

    public static List<Interview> getHouseholdInterviews(long householdID){
        return new Select().from(Interview.class).where("household=" + householdID).execute();
    }
}
