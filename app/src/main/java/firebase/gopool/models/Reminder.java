package firebase.gopool.models;

public class Reminder {
    public String date;
    public String notificationComment;


    public Reminder() {
    }

    public Reminder(String date, String notificationComment) {
        this.date = date;
        this.notificationComment = notificationComment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotificationComment() {
        return notificationComment;
    }

    public void setNotificationComment(String notificationComment) {
        this.notificationComment = notificationComment;
    }
}
