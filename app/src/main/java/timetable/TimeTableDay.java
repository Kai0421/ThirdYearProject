package timetable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TimeTableDay implements Serializable{

    private String title;
    private String day;
    private LinkedHashMap<String, TimeTableClass> hourlySchedule;

    public TimeTableDay(String day, String title){
        this.day = day;
        this.title = title;
        hourlySchedule = new LinkedHashMap<>();

        //hourlySchedule.put("8:00", null);
        hourlySchedule.put("9:00", null);
        hourlySchedule.put("10:00", null);
        hourlySchedule.put("11:00", null);
        hourlySchedule.put("12:00", null);
        hourlySchedule.put("13:00", null);
        hourlySchedule.put("14:00", null);
        hourlySchedule.put("15:00", null);
        hourlySchedule.put("16:00", null);
        hourlySchedule.put("17:00", null);
    }

    public String getDay() {
        return day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public HashMap<String, TimeTableClass> getHourlySchedule() {
        return hourlySchedule;
    }

}
