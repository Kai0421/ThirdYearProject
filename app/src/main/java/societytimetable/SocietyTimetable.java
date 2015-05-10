package societytimetable;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.third.year.project.smktpk.virtualcit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SocietyTimetable extends ActionBarActivity{

    //Constants value
    private final int COLUMNS = 2, DIVIDED_SIZE = 4;

    //private components
    private TextView societyTime, societyName, dayLabel;
    private TableLayout societyTable;
    private ArrayList<Society> listOfSociety = new ArrayList<Society>();
    private ArrayList<TodaysActivity> todaysActivities = new ArrayList<TodaysActivity>();

    //Private Variables
    private String day;
    private boolean addedSociety = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_society_timetable);

        // Set the ActionBar background colour
        super.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d11748")));

        //Each get using the ASync task to retrieve Society Information
        new GetAllSocietyTask().execute(new ApiConnectorSociety());
        new GetAllTimeTask().execute(new ApiConnector());
        getDay();
        dayLabel = (TextView) findViewById(R.id.day_textview);
        dayLabel.setText(day);

        //create and link the components
        societyTable = (TableLayout) findViewById(R.id.society_timetable);
    }


    //Create Table rows
    private void createRows(int rows){
        View divider;
        TextView tvT, tvN;
        TableRow row;

        for(int i = 1; i <= rows; i++)
        {
            row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for(int a = 1; a <= COLUMNS; a++)
            {
                if((a % 3) == 0)
                {
                    divider = new View(this);
                    divider.setLayoutParams(new TableLayout.LayoutParams(DIVIDED_SIZE, TableLayout.LayoutParams.MATCH_PARENT));
                    row.addView(divider, new TableRow.LayoutParams(DIVIDED_SIZE, TableRow.LayoutParams.MATCH_PARENT));

                    continue;
                }
            }

            //Create Component
            tvT= new TextView(this);
            tvN = new TextView(this);
            tvT.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvN.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tvT.setPadding(10,10,10,10);
            tvN.setPadding(10,10,10,10);

            //Set Textview
            tvT.setText(todaysActivities.get(i-1).getDay().getsTime() + "\n" + todaysActivities.get(i-1).getDay().geteTime());
            tvN.setText(todaysActivities.get(i-1).getName());

            //set Text Size
            tvT.setTextSize(getResources().getDimension(R.dimen.table_text_size));
            tvN.setTextSize(getResources().getDimension(R.dimen.table_text_size));

            row.addView(tvT);
            row.addView(tvN);

            //ID Start From 0
            row.setId(i-1);

            divider = new View(this);
            divider.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, DIVIDED_SIZE));
            divider.setBackgroundColor(Color.BLACK);

            if(i == 0)
            {
                societyTable.addView(divider);
                divider = new View(this);
                divider.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, DIVIDED_SIZE));
                divider.setBackgroundColor(Color.BLACK);
            }

            // Add Row and divider to the table
            societyTable.addView(row);
            societyTable.addView(divider);

            // Set To be Clickable
            row.setClickable(true);
            final int id = row.getId();
            System.out.println("ID :" + id + "\n Activity name :" + todaysActivities.get(id).getName());

            row.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
                    Intent intent = new Intent(SocietyTimetable.this, ActivityDetails.class);
                    Bundle bundle = new Bundle();

                   // bundle.putString("id", todaysActivities.get(id).getId());
                    bundle.putString("name", todaysActivities.get(id).getName());
                    bundle.putString("description", todaysActivities.get(id).getDiscription());
                    bundle.putString("location", todaysActivities.get(id).getLocation());
                    bundle.putString("startTime", todaysActivities.get(id).getDay().getsTime());
                    bundle.putString("endTime", todaysActivities.get(id).getDay().geteTime());
                    bundle.putString("fee", todaysActivities.get(id).getFees()  + "");
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });
        }
    }

    //set Todays Activity into an arraylist
    private void setTodaysActivity(){

        //Show time
        for (int b = 0; b < listOfSociety.size(); b++)
        {
            for (int c = 0; c < listOfSociety.get(b).getDayNTime().size(); c++)
            {
                //If is Equal to today add into the array
                if(listOfSociety.get(b).getDayNTime().get(c).getDay().equals(day))
                {
                    todaysActivities.add(new TodaysActivity(listOfSociety.get(b).getId(),listOfSociety.get(b).getName(), listOfSociety.get(b).getDiscription(), listOfSociety.get(b).getLocation(), listOfSociety.get(b).getDayNTime().get(c)));
                }
            }
        }
    }

    //Get today in the system
    private void getDay(){
        SimpleDateFormat df = new SimpleDateFormat("EEEE");
        Calendar calander = Calendar.getInstance();

        //Get today
        day = df.format(calander.getTime());
    }

    //Add Society into the array List
    private void parseSociety(JSONArray jsonArray) {

        String id = "", name = "", description = "", location = "";
        double fees = 0;

        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i);

                //Get Each Column on the database
                id = json.getString("id");
                name = json.getString("name");
                description = json.getString("description");
                location = json.getString("location");
                fees = json.getDouble("fees");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Create A List of Society
            listOfSociety.add(new Society(id, name, description, location, fees));
        }

        //Set This to allow to add schedule into each activity
        addedSociety = true;
    }

    //Add time into Society
    private void parseTime(JSONArray jsonArray) {

        String id = "", day = "", startTime = "", endTime = "";
        boolean foundSociety = false;
        //System.out.println("json len" + jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i);

                //Get Each Column on the database
                id = json.getString("id");
                day = json.getString("day");
                startTime = json.getString("startTime");
                endTime = json.getString("endTime");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // This make sure that it will make a new Society after it the society is existed
            for(int a = 0; a < listOfSociety.size(); a++)
            {
                if (id.equals(listOfSociety.get(a).getId()))
                {
                    listOfSociety.get(a).setDayNTime(new Day(day, startTime, endTime));
                    break;
                }
            }
        }

        // print society for Debugging purpose
        /*for (int i = 0; i < listOfSociety.size(); i++)
        {
            System.out.println("ID: " + listOfSociety.get(i).getId() + "\t name :" + listOfSociety.get(i).getName());
        }*/

        // Set Todays Actvities
        setTodaysActivity();

        //Create Tables
        createRows(todaysActivities.size());
    }

    /*************************************** Private Classes *****************************************/

    //Get All time
    private class GetAllTimeTask extends AsyncTask<ApiConnector,Long,JSONArray> {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {

            // it is executed on Background thread
            return params[0].GetAllTime();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            // This make sure that getAllSocietyTask get excuted first
            if(addedSociety == true)
            {
                parseTime(jsonArray);
            }
        }
    }

    //Get all society
    private class GetAllSocietyTask extends AsyncTask<ApiConnectorSociety,Long,JSONArray> {
        @Override
        protected JSONArray doInBackground(ApiConnectorSociety... params) {

            // it is executed on Background thread
            return params[0].GetAllSociety();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            parseSociety(jsonArray);
        }
    }
}