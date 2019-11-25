package lecture.experiments.roomdatabase.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import lecture.experiments.roomdatabase.repository.Location_Tracking;

@Dao
public interface location_interface {

    @Query("SELECT * FROM Location_Tracking")
    List<Location_Tracking> getAlllocations();

    @Query("SELECT * FROM Location_Tracking WHERE uid LIKE :locationid LIMIT 1")
    Location_Tracking findByID(int locationid);
//
//    @Insert
//    void insertAll(List<Location_Tracking> products);

    @Insert
    void insertlocation(Location_Tracking placesVisited);

//    @Update
//    void update(Location_Tracking product);
//
//    @Delete
//    void delete(Location_Tracking product);
}
