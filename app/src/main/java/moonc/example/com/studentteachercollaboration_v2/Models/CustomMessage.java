package moonc.example.com.studentteachercollaboration_v2.Models;

import java.util.Date;

public class CustomMessage {
    private String from;
    private String messageBody;
    private Date time;

    public CustomMessage() {

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
