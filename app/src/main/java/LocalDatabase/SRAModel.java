package LocalDatabase;

import com.activeandroid.Model;

/**
 * This is the base Model class for the SRA database
 * It include methods that will be needed by all of the SRA tables
 * Created by Chad Carey on 10/1/2014.
 */
public class SRAModel extends Model {

    public SRAModel() {
        super();
    }

    /**
     * Generate Timestamp will generate a current timestamp for tables
     * @return
     */
    public String generateTimestamp() {
        return null;
    }
}
