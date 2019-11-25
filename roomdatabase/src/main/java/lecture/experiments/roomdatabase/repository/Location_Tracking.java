package lecture.experiments.roomdatabase.repository;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Location_Tracking")
public class Location_Tracking {
   //location_tracking (id, latitude, longitude, date, time, accuracy)
    public Location_Tracking (String latitude, String longitude, String date, String time, int accuracy)
    {
        this.latitude=latitude;

        this.date=date;
        this.accuracy=accuracy;
        this.longitude=longitude;
        this.time=time;
    }

        @PrimaryKey(autoGenerate = true)
        private int uid;

        @ColumnInfo(name = "latitide")
        private String latitude;

        @ColumnInfo(name = "longitude")
        private String longitude;


        @ColumnInfo(name = "date")
        private String date;

        @ColumnInfo(name = "time")
        private String time;

        @ColumnInfo(name = "accuracy")
        private int accuracy;



        public String getLatitude() {
        return latitude;
    }

        public String getLongitude() {
        return longitude;
    }

        public String getTime() {
        return time;
    }

        public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

        public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

        public void setTime(String time) {
        this.time = time;
       }

        public int getUid() {
            return uid;
        }
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }
}

