package societytimetable;

public class Day {

    private String day, sTime, eTime;

    public Day(String day, String s, String e){
        setDay(day);
        setsTime(s);
        seteTime(e);
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }


}
