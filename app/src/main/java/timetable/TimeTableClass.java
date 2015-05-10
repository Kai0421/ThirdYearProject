package timetable;

import java.io.Serializable;

public class TimeTableClass implements Serializable{

    private String className;
    private String classRoom;

    public TimeTableClass(String cName, String cRoom){
        className = cName;
        classRoom = cRoom;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }
}
