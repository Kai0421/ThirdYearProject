package societytimetable;

import java.util.ArrayList;

/**
 * Created by Kai on 14/02/2015.
 */
public class Society {
    private String id, name, location, discription;
    private double fees;
    private ArrayList<Day> dayNTime = new ArrayList<>();

    //1st Constructor
    public Society (String id, String n, String d, String l, double f )
    {
        setId(id);
        setName(n);
        setDiscription(d);
        setLocation(l);
        setFees(f);
    }

    public Society (String id, String day, String start, String end){
        setId(id);
        setDayNTime(new Day(day, start, end));
    }

    public Society (String n, String l, String d, Day day)
    {
        setName(n);
        setLocation(l);
        setDiscription(d);
        setDayNTime(day);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public ArrayList<Day> getDayNTime() {
        return dayNTime;
    }

    public void setDayNTime(Day day) {
        this.dayNTime.add(day);
    }
}
