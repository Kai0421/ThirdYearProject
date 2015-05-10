package timetable;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.third.year.project.smktpk.virtualcit.R;

import java.util.ArrayList;

import sliding_tab_layout.activities.SampleActivityBase;


// Fragment class used to begin/initialize fragment activity
// Loads TimeTableDisplay class
public class TimeTableMain extends SampleActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_main);

        // Set the ActionBar background colour
        //super.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d11748")));

        ArrayList<TimeTableDay> timetableInfo;

        if (savedInstanceState == null) {

            Bundle bundle = getIntent().getExtras();
            timetableInfo = (ArrayList<TimeTableDay>) bundle.get(getString(R.string.timetable_bundle_key));

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            TimeTableDisplay fragment = new TimeTableDisplay();
            fragment.setArguments(bundle);

            transaction.replace(R.id.timetable_frame, fragment);
            transaction.commit();
        }

    }


}
