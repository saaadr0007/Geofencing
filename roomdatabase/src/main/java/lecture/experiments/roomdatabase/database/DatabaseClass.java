package lecture.experiments.roomdatabase.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import lecture.experiments.roomdatabase.ScreenTime;
import lecture.experiments.roomdatabase.dao.DaoAccess;
import lecture.experiments.roomdatabase.dao.location_interface;
import lecture.experiments.roomdatabase.dao.places;

@Database(entities = {ScreenTime.class}, version = 1, exportSchema = false)
public abstract class DatabaseClass extends RoomDatabase {

    public abstract DaoAccess daoAccess();
    public abstract location_interface location();
    public abstract places places();
}