package firebase.gopool.models;

/**
 * Created by P16174003 on 05/02/2019.
 */

public class Request {
    private String user_id;
    private String profile_photo;
    private String userProfilePhoto;
    private String username;
    private String dateOfJourney;
    private String pickupTime;
    private Float cost;
    private int seats;
    private String destination;
    private String location;
    private int luggage;
    private Boolean accepted;
    private String ride_id;
    private String pickupLocation;
    private String licencePlate;

    public Request() {
    }

    public Request(String user_id, String profile_photo, String userProfilePhoto, String username, int seats, String destination, String location, int luggage, Boolean accepted, String ride_id, String dateOfJourney,
                   String pickupTime, Float cost, String pickupLocation, String licencePlate) {
        this.user_id = user_id;
        this.profile_photo = profile_photo;
        this.userProfilePhoto = userProfilePhoto;
        this.username = username;
        this.seats = seats;
        this.destination = destination;
        this.location = location;
        this.luggage = luggage;
        this.accepted = accepted;
        this.ride_id = ride_id;
        this.dateOfJourney = dateOfJourney;
        this.pickupTime = pickupTime;
        this.cost = cost;
        this.pickupLocation = pickupLocation;
        this.licencePlate = licencePlate;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getLuggage() {
        return luggage;
    }

    public void setLuggage(int luggage) {
        this.luggage = luggage;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public String getRide_id() {
        return ride_id;
    }

    public void setRide_id(String ride_id) {
        this.ride_id = ride_id;
    }

    public String getDateOfJourney() {
        return dateOfJourney;
    }

    public void setDateOfJourney(String dateOfJourney) {
        this.dateOfJourney = dateOfJourney;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    @Override
    public String toString() {
        return "Request{" +
                "user_id='" + user_id + '\'' +
                ", profile_photo='" + profile_photo + '\'' +
                ", userProfilePhoto='" + userProfilePhoto + '\'' +
                ", username='" + username + '\'' +
                ", dateOfJourney='" + dateOfJourney + '\'' +
                ", pickupTime='" + pickupTime + '\'' +
                ", cost=" + cost +
                ", seats=" + seats +
                ", destination='" + destination + '\'' +
                ", location='" + location + '\'' +
                ", luggage=" + luggage +
                ", accepted=" + accepted +
                ", ride_id='" + ride_id + '\'' +
                ", pickupLocation='" + pickupLocation + '\'' +
                ", licencePlate='" + licencePlate + '\'' +
                '}';
    }
}
