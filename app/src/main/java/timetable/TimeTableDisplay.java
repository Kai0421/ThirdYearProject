package timetable;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.third.year.project.smktpk.virtualcit.R;

import java.util.ArrayList;
import java.util.Map;

import sliding_tab_layout.view.SlidingTabLayout;

public class TimeTableDisplay extends Fragment{

    // Class from Google (URL on MainScreen.java) for modern looking sliding effect
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<TimeTableDay> timetableInfo;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        timetableInfo = (ArrayList<TimeTableDay>) bundle.get("ctimetable");

        context = container.getContext();

        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Get reference to the view pager for swiping effect
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new TimetablePagerAdapter(context));

        // Link the sliding effect to the sliding tab layout feature
        slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);

    }

    class TimetablePagerAdapter extends PagerAdapter {
        private Context mContext;

        public TimetablePagerAdapter(Context context){
            mContext = context;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // Depending on the position - different timetable day will be shown
        @Override
        public CharSequence getPageTitle(int position) {

            if(position == 0){
                return "Monday";
            }
            else if(position == 1){
                return "Tuesday";
            }
            else if(position == 2){
                return "Wednesday";
            }
            else if(position == 3){
                return "Thursday";
            }
            else{
                return "Friday";
            }
        }

        // Set the layout and sizes of the views
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item, container, false);
            container.addView(view);

            CharSequence dayPositionCharSeq = getPageTitle(position);
            String sDayPositionCharSeq = (String) dayPositionCharSeq;

            int dayPositionInArrayList;
            dayPositionInArrayList = getDayPosition(sDayPositionCharSeq);

            TableLayout tableLayout = (TableLayout) view.findViewById(R.id.timetable_table);
            tableLayout.setColumnStretchable(0, true);
            TableRow row;

            if(dayPositionInArrayList == -1){
                row= new TableRow(mContext);
                TextView tv0 = new TextView(mContext);
                tv0.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                tv0.setTypeface(null, Typeface.BOLD_ITALIC);
                tv0.setPaddingRelative(25, 30, 0, 0);
                tv0.setTextColor(Color.parseColor("#808080"));
                tv0.setText(mContext.getString(R.string.no_time_table_classes));
                tv0.setGravity(Gravity.CENTER);
                row.addView(tv0);
                tableLayout.addView(row);
            }
            else{
                tableLayout.setColumnStretchable(1, true);
                tableLayout.setColumnStretchable(2, true);
                tableLayout.setColumnShrinkable(2, true);

                Map<String, TimeTableClass> map = timetableInfo.get(position).getHourlySchedule();

                TableRow titleRow = new TableRow(mContext);
                TextView titleView = (TextView) view.findViewById(R.id.table_title_tv);
                titleView.setText(timetableInfo.get(0).getTitle());
                titleView.setTextColor(Color.BLACK);
                titleView.setPaddingRelative(0, 80, 0, 0);
                titleView.setTypeface(null, Typeface.BOLD);
                titleView.setGravity(Gravity.CENTER);
                titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                for(Map.Entry<String,TimeTableClass> entry : map.entrySet()) {

                    row= new TableRow(mContext);

                    String time = entry.getKey();
                    TimeTableClass value = entry.getValue();

                    String room = null;
                    String subject = null;

                    TextView tv1 = new TextView(mContext);
                    tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    tv1.setTypeface(null, Typeface.BOLD);
                    tv1.setPaddingRelative(25, 30, 0, 0);
                    tv1.setTextColor(Color.parseColor("#808080"));

                    TextView tv2 = new TextView(mContext);
                    tv2.setPaddingRelative(20, 30, 0, 0);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    tv2.setTextColor(Color.parseColor("#636363"));

                    TextView tv3 = new TextView(mContext);
                    tv3.setPaddingRelative(20,30,0,0);
                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                    tv3.setTextColor(Color.parseColor("#636363"));

                    if( (value != null) )
                    {
                        room = value.getClassRoom();
                        subject = value.getClassName();

                        tv1.setText(time);
                        row.addView(tv1);

                        tv2.setText(room);
                        row.addView(tv2);

                        tv3.setText(subject);
                        row.addView(tv3);
                    }
                    else if(value == null){
                        tv1.setText(time);
                        row.addView(tv1);

                        tv2.setText("");
                        row.addView(tv2);

                        tv3.setText("");
                        row.addView(tv3);
                    }

                    tableLayout.addView(row);
                }
            }

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    int getDayPosition(String day){

        int index = -1;

        for(int x = 0; x < timetableInfo.size(); x++){

            String arrayDay = timetableInfo.get(x).getDay();

            if(day.equals(arrayDay)){
                index = x;
                break;
            }
        }

        return index;
    }




}
