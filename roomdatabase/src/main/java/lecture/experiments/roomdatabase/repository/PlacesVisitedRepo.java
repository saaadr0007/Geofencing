package lecture.experiments.roomdatabase.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import java.util.List;

import lecture.experiments.roomdatabase.ScreenTime;
import lecture.experiments.roomdatabase.database.DatabaseClass;

public class PlacesVisitedRepo {

    private String DB_NAME = "app_core_db";
    private DatabaseClass placesvisitedDatabase;

    public PlacesVisitedRepo(Context context) {
        getInstance(context);
    }

    private DatabaseClass getInstance(Context context){
        if (placesvisitedDatabase!=null){
            return placesvisitedDatabase;
        }else{
            placesvisitedDatabase = Room.databaseBuilder(context, DatabaseClass.class, DB_NAME).build();
        }
        return placesvisitedDatabase;
    }

    public void insertTask(String place_name, String date, String state, String custom_text) {
        Places_Visited places_visited = new Places_Visited(place_name,date,state,custom_text);

        insertTask(places_visited);
    }

    @SuppressLint("StaticFieldLeak")
    public void insertTask(final Places_Visited places_visited) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.e("screentimeRepo", "Inserting!!!! from Background Thread: " + Thread.currentThread().getId());
                placesvisitedDatabase.places().insertlocation(places_visited);
                return null;
            }
        }.execute();
    }



}
