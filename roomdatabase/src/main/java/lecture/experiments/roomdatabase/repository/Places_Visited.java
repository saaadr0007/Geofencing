package lecture.experiments.roomdatabase.repository;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Places_Visited")
public class Places_Visited {
    //(id, place_name, date, time, checkin/ checkout, custom_text)
    public Places_Visited(String place_name, String date, String state, String custom_text)
    {
        this.place_name=place_name;
        this.date=date;
        this.state=state;
        this.custom_text=custom_text;
    }
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "place_name")
    private String place_name;

    @ColumnInfo(name = "date")
    private String date;


    @ColumnInfo(name = "state")
    private String state;

    @ColumnInfo(name = "custom_text")
    private String custom_text;

    public int getUid() {
        return uid;
    }

    public String getDate() {
        return date;
    }

    public String getCustom_text() {
        return custom_text;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getState() {
        return state;
    }

    public void setCustom_text(String custom_text) {
        this.custom_text = custom_text;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

}
