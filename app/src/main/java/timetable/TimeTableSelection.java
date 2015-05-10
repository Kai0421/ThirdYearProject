package timetable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.third.year.project.smktpk.virtualcit.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class TimeTableSelection extends ActionBarActivity {

    private Spinner timetableOptions;
    private int urlIndex;
    private Button timetableBtn;
    private ArrayList<String> urlList;
    private String key;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_selection);

        // Set the ActionBar background colour
        super.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d11748")));

        key = getResources().getString(R.string.timetable_bundle_key);

        // Set a list of timetable days to access
        urlList = new ArrayList<>();
        urlList.add("http://timetables.cit.ie:70/reporting/Individual;Student+Set;name;CO.DCOM3%0D%0A?weeks=24-31;34-38&days=1-5&periods=1-40&height=100&width=100");
        urlList.add("http://timetables.cit.ie:70/reporting/Individual;Student+Set;name;CO.DNET3%0D%0A?weeks=4-16&days=1-5&periods=1-40&height=100&width=100");
        urlList.add("http://timetables.cit.ie:70/reporting/Individual;Student+Set;name;AI.ADCA2-A%0D%0A?weeks=4-16&days=1-5&periods=1-40&height=100&width=100");
        urlList.add("http://timetables.cit.ie:70/reporting/Individual;Student+Set;name;CO.COM1-A%0D%0A?weeks=4-16&days=1-5&periods=1-40&height=100&width=100");
        urlList.add("http://timetables.cit.ie:70/reporting/Individual;Student+Set;name;CO.DNET3%0D%0A?weeks=24-31;34-38&days=1-5&periods=1-40&height=100&width=100");
        urlList.add("http://timetables.cit.ie:70/reporting/Individual;Student+Set;name;CA.CM1%0D%0A?weeks=4-16&days=1-5&periods=1-40&height=100&width=100");
        urlList.add("http://timetables.cit.ie:70/reporting/Individual;Student+Set;name;PS.IP1%0D%0A?weeks=24-31;34-38&days=1-5&periods=1-40&height=100&width=100");
        urlList.add("http://timetables.cit.ie:70/reporting/Individual;Student+Set;name;BI.BMS1%0D%0A?weeks=24-31;34-38&days=1-5&periods=1-40&height=100&width=100");
        urlList.add("http://timetables.cit.ie:70/reporting/Individual;Student+Set;name;MB.DME1%0D%0A?weeks=4-16&days=1-5&periods=1-40&height=100&width=100");

        timetableOptions = (Spinner)findViewById(R.id.time_table_selection_spinner);
        timetableBtn = (Button) findViewById(R.id.timetable_spinner_button);

        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this, R.array.spinner_url_list, android.R.layout.simple_spinner_dropdown_item);
        timetableOptions.setAdapter(adapter);

        timetableOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                urlIndex = position;
                title = (String) parent.getItemAtPosition(urlIndex);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        timetableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScrapeTimetableURL(urlList.get(urlIndex), TimeTableSelection.this, key, title).execute();
            }
        });

    }
}

// Scrape the timetable data for a given URL
class ScrapeTimetableURL extends AsyncTask<Void, Void, Void> implements Serializable{

    private int numDays;
    private int dayCounter;
    private String urlToScrape;
    private ProgressDialog dialog;
    private Context mContext;
    private ArrayList<TimeTableDay> classDays;
    private String bundleKey;
    private String mTitle;

    public ScrapeTimetableURL(String url, Context context, String key, String title){
        numDays = 0;
        this.urlToScrape = url;
        mContext = context;
        this.bundleKey = key;
        this.mTitle = title;
    }

    @Override
    protected void onPreExecute() {

        // Display Dialog to the User
        dialog = new ProgressDialog(mContext);
        dialog.setTitle((R.string.timetable_dialog_msg));
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        TimeTableDay monday;
        TimeTableDay tuesday;
        TimeTableDay wednesday;
        TimeTableDay thursday;
        TimeTableDay friday;

        Document doc;

        String documentString = null;

        try {

            classDays = new ArrayList<>();

            // Ger a referent to a JSoup document object which is created from
            // the HTML retrieved form the given URL
            doc = Jsoup.connect(urlToScrape).get();

            // Convert the JSoup Document object into a String object
            documentString = doc.toString();

            // Remove the header and footer table
            // The purpose of this is to isolate the main table only
            documentString = documentString.replaceFirst("((.|\\n)*)<!-- END REPORT HEADER -->(.*)", "");
            documentString = documentString.replaceFirst("<!-- START REPORT FOOTER -->((.|\\n)*)", "");

            // After the header and footer tables have been removed
            // the document needs to be re-parsed in order to have
            // valid HTML structure
            doc = Jsoup.parseBodyFragment(documentString);

            // Based on the table header - work out how many days there are
            // Each day will be an instance of TimeTableDay object
            if(documentString.contains("Monday")){
                monday = new TimeTableDay("Monday", mTitle);
                numDays++;
                classDays.add(monday);
            }
            if(documentString.contains("Tuesday")){
                tuesday = new TimeTableDay("Tuesday", mTitle);
                numDays++;
                classDays.add(tuesday);
            }
            if(documentString.contains("Wednesday")){
                wednesday = new TimeTableDay("Wednesday", mTitle);
                numDays++;
                classDays.add(wednesday);
            }
            if(documentString.contains("Thursday")){
                thursday = new TimeTableDay("Thursday", mTitle);
                numDays++;
                classDays.add(thursday);
            }
            if(documentString.contains("Friday")){
                friday = new TimeTableDay("Friday", mTitle);
                numDays++;
                classDays.add(friday);
            }

            // Use JSoup to access the Document object at the row level
            Elements rows = doc.select("body > table > tbody > tr");

            // This ArrayList will hold a table row per index
            ArrayList<String> timeTableToParse = new ArrayList<>();

            // Add table rows to the ArrayList
            for(Element td : rows) {
                timeTableToParse.add(td.outerHtml());
            }

            ArrayList<String> rowList;
            TimeTableClass timeTableClass;

            for(int x = 0; x < timeTableToParse.size(); x++){

                // If a row contains at least 1 class
                if(timeTableToParse.get(x).matches("((.|\n)*)\\d:00((.|\n)*)"))
                    if (timeTableToParse.get(x).contains("<!-- START OBJECT-CELL -->")) {

                        rowList = new ArrayList<>();
                        String time = null;
                        String className = null;
                        String classRoom = null;
                        boolean doubleClass = false;

                        String str[] = timeTableToParse.get(x).split("\n");

                        for (int innerRowIndex = 0; innerRowIndex < str.length; innerRowIndex++) {

                            // Get the time
                            if (str[innerRowIndex].contains("rowspan=\"1\"")) {
                                time = str[innerRowIndex].substring((str[innerRowIndex].indexOf("#000000\">") + 9));
                                time = time.replaceFirst("</font></td>", "");
                                time = time.trim();
                            }

                            // If table cell with no class - move to next cell (day)
                            if (str[innerRowIndex].matches("((.|\n)*)<td>&nbsp;</td>((.|\n)*)")) {

                                boolean freeClass = false;

                                // while day has not free class --- increment day until free class
                                while (freeClass != true) {

                                    if (classDays.get(dayCounter).getHourlySchedule().get(time) == null) {
                                        freeClass = true;
                                    } else {
                                        incrementDayCounter();
                                    }
                                }

                                incrementDayCounter();

                                if ((dayCounter == numDays - 1) && (classDays.get(dayCounter).getHourlySchedule().get(time) != null)) {
                                    incrementDayCounter();
                                }
                            }

                            // If class is innerRowIndex double 2 hour class
                            if (str[innerRowIndex].contains("rowspan=\"8\"")) {
                                doubleClass = true;
                            }

                            // Get the class name
                            if (str[innerRowIndex].matches("((.|\n)*)align=\"left\"><font color=\"#000000\">((.|\n)*)")) {
                                className = str[innerRowIndex].substring((str[innerRowIndex].indexOf("#000000\">") + 9));
                                className = className.replaceFirst("</font></td>", "");
                                className = className.replaceFirst("&amp;", "&");
                            }

                            // Get the class room
                            if (str[innerRowIndex].matches("((.|\n)*)align=\"right\"><font color=\"#008000\">((.|\n)*)")) {
                                classRoom = str[innerRowIndex].substring((str[innerRowIndex].indexOf("#008000\">") + 9));
                                classRoom = classRoom.replaceFirst("</font></td>", "");

                                boolean freeClass = false;

                                // while day has not free class --- increment day until free class
                                while (freeClass != true) {

                                    if (classDays.get(dayCounter).getHourlySchedule().get(time) == null) {
                                        freeClass = true;
                                    } else {
                                        incrementDayCounter();
                                    }
                                }


                                timeTableClass = new TimeTableClass(className, classRoom);
                                classDays.get(dayCounter).getHourlySchedule().put(time, timeTableClass);

                                // Add double class
                                // IF the class is a double class duplicate data in the next hour segment
                                if (doubleClass == true) {

                                    Map<String, TimeTableClass> map = classDays.get(dayCounter).getHourlySchedule();

                                    Iterator entries = map.entrySet().iterator();
                                    while (entries.hasNext()) {
                                        Map.Entry thisEntry = (Map.Entry) entries.next();
                                        String key = (String) thisEntry.getKey();
                                        TimeTableClass value = (TimeTableClass) thisEntry.getValue();

                                        if (((key.equals(time)))) {
                                            Map.Entry nextKey = (Map.Entry) entries.next();
                                            classDays.get(dayCounter).getHourlySchedule().put((String) nextKey.getKey(), timeTableClass);
                                            doubleClass = false;
                                            break;
                                        }
                                    }

                                }

                                incrementDayCounter();

                                // If the day counter is the last day of the week and
                                // the cell is taken by a double class
                                // Skip to the next cell
                                if ((dayCounter == numDays - 1) && (classDays.get(dayCounter).getHourlySchedule().get(time) != null)) {
                                    incrementDayCounter();
                                }
                            }
                        }
                    }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void params){
        dialog.dismiss();
        Intent intent = new Intent(mContext, TimeTableMain.class);
        intent.putExtra(bundleKey, classDays);
        mContext.startActivity(intent);
    }

    private void incrementDayCounter(){
        if(dayCounter == (numDays -1) ){
            dayCounter = 0;
        }
        else{
            dayCounter++;
        }
    }
}
