package LocalDatabase;

import com.activeandroid.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the base Model class for the SRA database
 * It include methods that will be needed by all of the SRA tables
 * Created by Chad Carey on 10/1/2014.
 */
public abstract class SRAModel extends Model {

    public SRAModel() {
        super();
    }

    /**
     * Generate Timestamp will generate a current timestamp for tables
     * @return
     */
    public static String generateTimestamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSS");
        Date date = new Date();
        String formattedDate = dateFormat.format(date);
        formattedDate = formattedDate.replace("/", "-");
        return formattedDate;
    }
}
