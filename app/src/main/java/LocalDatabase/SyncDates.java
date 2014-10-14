package LocalDatabase;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Chad Carey on 10/14/2014.
 */
@Table(name = "sync_dates")
public class SyncDates extends SRAModel{

    @Column(name = "table_name")
    public String tableName;
    @Column(name = "pull_date")
    public String pullDate;
    @Column(name = "push_date")
    public String pushDate;

}
