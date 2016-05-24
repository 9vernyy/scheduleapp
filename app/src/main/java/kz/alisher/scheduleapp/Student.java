package kz.alisher.scheduleapp;

/**
 * Created by Alisher Kozhabay on 21.05.2016.
 */
public class Student {
    private String fName;
    private String lName;
    private String groupId;


    public Student() {
    }

    public Student(String fName, String lName) {
        this.fName = fName;
        this.lName = lName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }
}
