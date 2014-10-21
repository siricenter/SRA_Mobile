package LocalDatabase;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by jordanreed on 10/2/14.
 */
@Table(name = "people")
public class Person extends SRAModel {

    @Column(name = "given_name")
    public String given_name;
    @Column(name = "family_name")
    public String family_name;
    @Column(name = "family_relationship_id")
    public int family_relationship_id;
    @Column(name = "birthday")
    public String birthday;
    @Column(name = "education_level")
    public String education_level;
    @Column(name = "gender")
    public String gender;
    @Column(name = "in_school")
    public Boolean in_school;
    @Column(name = "is_alive")
    public Boolean is_alive;
    @Column(name = "created_at")
    public String created_at;
    @Column(name = "updated_at")
    public String updated_at;
    @Column(name = "household_id")
    public int household_id;
    @Column(name = "db_id")
    public long id;


    public Person() {
        super();
    }

    public Person(String given_name, String family_name) {
        super();
        this.given_name = given_name;
        this.family_name = family_name;
    }

    public static List <Person> getByName(String name) {
        return new Select().from(Person.class).where("name='" + name+ "'").execute();
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

    public static List<Person> getMembers(long id){
        return new Select().from(Person.class).where("household_id='" + id + "'").execute();
    }

}
