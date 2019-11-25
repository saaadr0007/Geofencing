package lecture.experiments.roomdatabase.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import lecture.experiments.roomdatabase.database.DatabaseClass;

public class LocTrackingRepo {

        private String DB_NAME = "app_core_db";
        private DatabaseClass loctrackDatabase;

        public LocTrackingRepo(Context context) {
            getInstance(context);
        }

        private DatabaseClass getInstance(Context context){
            if (loctrackDatabase!=null){
                return loctrackDatabase;
            }else{
                loctrackDatabase = Room.databaseBuilder(context, DatabaseClass.class, DB_NAME).build();
            }
            return loctrackDatabase;
        }

        public void insertTask(String latitude, String longitude, String date, String time, int accuracy) {
            Location_Tracking location_tracking = new Location_Tracking(latitude, longitude, date, time, accuracy);
            insertTask(location_tracking);
        }

        @SuppressLint("StaticFieldLeak")
        public void insertTask(final Location_Tracking places_visited) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Log.e("screentimeRepo", "Inserting!!!! from Background Thread: " + Thread.currentThread().getId());
                    loctrackDatabase.location().insertlocation(places_visited);
                    return null;
                }
            }.execute();
        }



    }

