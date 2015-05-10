package societytimetable;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.third.year.project.smktpk.virtualcit.R;


public class ActivityDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_details);

        // Set the ActionBar background colour
        super.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d11748")));

        //Variable to get string from previous Activity
        String st = "", s = "", e = "", desc = "", l = "", f = "";

        //Components to set Text on screen
        TextView societyTitle, sTime, eTime, descpt, loctn, fees;
        societyTitle = (TextView) findViewById(R.id.title_of_activity);
        sTime = (TextView) findViewById(R.id.start_time_text);
        eTime = (TextView) findViewById(R.id.end_time_text);
        descpt = (TextView) findViewById(R.id.description_text);
        loctn = (TextView) findViewById(R.id.location_text);
        fees = (TextView) findViewById(R.id.fee_text);

        //Get Bundle data from the previous activity
        Intent intent = getIntent();
        st = intent.getStringExtra("name");
        desc = intent.getStringExtra("description");
        l = intent.getStringExtra("location");
        s = intent.getStringExtra("startTime");
        e = intent.getStringExtra("endTime");
        f = intent.getStringExtra("fee");

        //Check If the data Parsing work
        System.out.println("Name :" + st + "\nDesc :" + desc + "\nLocation :" + l + "\nStart Time: " + s + "\nEnd Time :" + e + "\nfees" + f);

        //Set String to Text View
        societyTitle.setText(st);
        sTime.setText(s);
        eTime.setText(e);
        descpt.setText(desc);
        loctn.setText(l);
        fees.setText(" € " + f);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
