package firebase.gopool.models;


public class SearchResults {

    private String rideID;
    private String username;
    private String profile_picture;
    private String currentLocation;
    private String destination;
    private String lengthOfJourney;
    private String car;
    private String dateOfJourney;
    private String pickupTime;
    private int completeRides;
    private int seatsAvailable;
    private int extraTime;
    private int cost;
    private int userRating;

    public SearchResults() {
    }

    public SearchResults(String rideID, String username, String profile_picture, String currentLocation, String destination, String lengthOfJourney, String car, String dateOfJourney, String pickupTime, int completeRides,
                         int extraTime, int seatsAvailable, int cost, int userRating) {
        this.rideID = rideID;
        this.username = username;
        this.profile_picture = profile_picture;
        this.currentLocation = currentLocation;
        this.destination = destination;
        this.lengthOfJourney = lengthOfJourney;
        this.car = car;
        this.dateOfJourney = dateOfJourney;
        this.pickupTime = pickupTime;
        this.completeRides = completeRides;
        this.seatsAvailable = seatsAvailable;
        this.extraTime = extraTime;
        this.cost = cost;
        this.userRating = userRating;
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

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public String getDateOfJourney() {
        return dateOfJourney;
    }

    public void setDateOfJourney(String dateOfJourney) {
        this.dateOfJourney = dateOfJourney;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCompleteRides() {
        return completeRides;
    }

    public void setCompleteRides(int completeRides) {
        this.completeRides = completeRides;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLengthOfJourney() {
        return lengthOfJourney;
    }

    public void setLengthOfJourney(String lengthOfJourney) {
        this.lengthOfJourney = lengthOfJourney;
    }

    public int getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(int extraTime) {
        this.extraTime = extraTime;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "rideID='" + rideID + '\'' +
                ", username='" + username + '\'' +
                ", profile_picture='" + profile_picture + '\'' +
                ", currentLocation='" + currentLocation + '\'' +
                ", destination='" + destination + '\'' +
                ", lengthOfJourney='" + lengthOfJourney + '\'' +
                ", car='" + car + '\'' +
                ", dateOfJourney='" + dateOfJourney + '\'' +
                ", pickupTime='" + pickupTime + '\'' +
                ", completeRides=" + completeRides +
                ", seatsAvailable=" + seatsAvailable +
                ", extraTime=" + extraTime +
                ", cost=" + cost +
                ", userRating=" + userRating +
                '}';
    }
}
