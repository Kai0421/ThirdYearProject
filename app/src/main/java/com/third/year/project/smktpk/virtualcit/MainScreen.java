package com.third.year.project.smktpk.virtualcit;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import facebook.FacebookMain;
import societytimetable.SocietyTimetable;
import studentservices.StudentServices;
import timetable.TimeTableSelection;
import tour.CollegeTour;
import twitter.TwitterMain;

/**
 * Note the package sliding_tab_lay contains two java files used to give effects
 * in the sliding tabs/view pager implemented by the Timetable feature
 * This code is provided by Google at the following URL
 * http://developer.android.com/samples/SlidingTabsBasic/project.html
 */


public class MainScreen extends ActionBarActivity {

    //private ListView list;
    //private ArrayAdapter<String> adapter;
    //private ArrayList<String> menu;
    //private Resources res;
    private Intent intent;
    //private String[] main_list_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Set the ActionBar background colour
        super.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d11748")));

        Resources res = this.getResources();

        String[] main_list_items = res.getStringArray(R.array.main_items);

        ListView list = (ListView) findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.main_list_content, main_list_items);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClick, int position, long id) {


                switch (position) {
                    case 0:
                        intent = new Intent(MainScreen.this, StudentServices.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(MainScreen.this, StudentHandbook.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MainScreen.this, TimeTableSelection.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MainScreen.this, CollegeTour.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MainScreen.this, IndoorMaps.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(MainScreen.this, SocietyTimetable.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(MainScreen.this, FacebookMain.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(MainScreen.this, TwitterMain.class);
                        startActivity(intent);
                        break;

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
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
