package lecture.experiments.modularization;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.location.LocationCheck;

import lecture.experiments.screentime.ScreenTimeFunctionality;


public class MainActivity extends AppCompatActivity {
    ScreenTimeFunctionality screenTimeFunctionality;
    LocationCheck locationCheck;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenTimeFunctionality = new ScreenTimeFunctionality();
        locationCheck=new LocationCheck();
    }

    public void startButton(View view) {
        screenTimeFunctionality.registerEvents(this);
       // locationCheck.registerEvents(this);
    }

    public void stopButton(View view) {
        screenTimeFunctionality.unregisterEvents(this);
    }
}
