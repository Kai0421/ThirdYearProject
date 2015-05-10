package societytimetable;

/**
 * Created by Kai on 02/03/2015.
 */
public class TodaysActivity {
    private String id, name, location, discription;
    private double fees;
    private Day day;

    public TodaysActivity(String id, String n, String d, String l, Day day){
        setId(id);
        setName(n);
        setDiscription(d);
        setLocation(l);
        this.day = day;
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

    public Day getDay() {
        return day;
    }
}
