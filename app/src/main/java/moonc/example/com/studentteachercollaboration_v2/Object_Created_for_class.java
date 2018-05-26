package moonc.example.com.studentteachercollaboration_v2;

public class Object_Created_for_class {
    private String Subject;
    private String Course_code;
    private String Start;
    private String end;
    private String room;
    private String Key;


    public Object_Created_for_class() {

    }

    public Object_Created_for_class(String subject, String course_code, String start, String end, String room,String key) {
        Subject = subject;
        Course_code = course_code;
        Start = start;
        this.end = end;
        this.room = room;
        this.Key = key;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getCourse_code() {
        return Course_code;
    }

    public void setCourse_code(String course_code) {
        Course_code = course_code;
    }

    public String getStart() {
        return Start;
    }

    public void setStart(String start) {
        Start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
