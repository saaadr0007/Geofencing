package lecture.experiments.roomdatabase.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import lecture.experiments.roomdatabase.repository.Places_Visited;

@Dao
public interface places {
        @Query("SELECT * FROM places_visited")
        List<Places_Visited> getAll();

        @Query("SELECT * FROM places_visited WHERE place_name LIKE :name LIMIT 1")
        Places_Visited findByName(String name);

        @Insert
        void insertAll(List<Places_Visited> products);

        @Insert
        void insertlocation(Places_Visited placesVisited);

        @Update
        void update(Places_Visited product);

        @Delete
        void delete(Places_Visited product);
    }