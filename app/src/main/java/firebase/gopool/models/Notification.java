package firebase.gopool.models;

public class Notification {
    public String title;
    public String body;
    public String rideID;
    public String username;
    public String to;
    public String from;


    public Notification() {
    }

    public Notification(String title, String body, String rideID, String username, String to) {
        this.title = title;
        this.body = body;
        this.rideID = rideID;
        this.username = username;
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRideID() {
        return rideID;
    }

    public void setRideID(String rideID) {
        this.rideID = rideID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
