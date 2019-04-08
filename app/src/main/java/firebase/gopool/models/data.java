package firebase.gopool.models;

/**
 * Created by P16174003 on 06/02/2019.
 */

public class data {

    public String rideID;
    public String username;
    public String profile_photo;
    public String to;
    public String from;

    public data() {
    }

    public data(String rideID, String username, String profile_photo, String to, String from) {
        this.rideID = rideID;
        this.username = username;
        this.profile_photo = profile_photo;
        this.to = to;
        this.from = from;
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

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
