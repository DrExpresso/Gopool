package firebase.gopool.models;

public class OfferRide {

    private String rideID;
    private String user_id;
    private String username;
    private String currentLocation;
    private String destination;
    private String dateOfJourney;
    private int seatsAvailable;
    private String licencePlate;
    private double currentlongitude;
    private double currentlatitude;
    private boolean sameGender;
    private int luggageAllowance;
    private String car;
    private String pickupTime;
    private String pickupLocation;
    private int extraTime;
    private String profile_picture;
    private int cost;
    private int completeRides;
    private int userRating;
    private String duration;

    public OfferRide() { }

    public OfferRide(String rideID, String user_id, String username, String currentLocation, String destination, String dateOfJourney, int seatsAvailable, String licencePlate,
                     double currentlongitude, double currentlatitude, boolean sameGender, int luggageAllowance, String car, String pickupTime, int extraTime, String profile_picture, int cost,
                     int completeRides, int userRating, String duration, String pickupLocation) {
        this.rideID = rideID;
        this.user_id = user_id;
        this.username = username;
        this.currentLocation = currentLocation;
        this.destination = destination;
        this.dateOfJourney = dateOfJourney;
        this.seatsAvailable = seatsAvailable;
        this.licencePlate = licencePlate;
        this.currentlongitude = currentlongitude;
        this.currentlatitude = currentlatitude;
        this.sameGender = sameGender;
        this.luggageAllowance = luggageAllowance;
        this.car = car;
        this.pickupTime = pickupTime;
        this.pickupLocation = pickupLocation;
        this.extraTime = extraTime;
        this.profile_picture = profile_picture;
        this.cost = cost;
        this.completeRides = completeRides;
        this.userRating = userRating;
        this.duration = duration;
    }

    public String getRideID() {
        return rideID;
    }

    public void setRideID(String rideID) {
        this.rideID = rideID;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public int getCompleteRides() {
        return completeRides;
    }

    public void setCompleteRides(int completeRides) {
        this.completeRides = completeRides;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateOfJourney() {
        return dateOfJourney;
    }

    public void setDateOfJourney(String dateOfJourney) {
        this.dateOfJourney = dateOfJourney;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public double getCurrentlongitude() {
        return currentlongitude;
    }

    public void setCurrentlongitude(double currentlongitude) {
        this.currentlongitude = currentlongitude;
    }

    public double getCurrentlatitude() {
        return currentlatitude;
    }

    public void setCurrentlatitude(double currentlatitude) {
        this.currentlatitude = currentlatitude;
    }

    public boolean isSameGender() {
        return sameGender;
    }

    public void setSameGender(boolean sameGender) {
        this.sameGender = sameGender;
    }

    public int getLuggageAllowance() {
        return luggageAllowance;
    }

    public void setLuggageAllowance(int luggageAllowance) {
        this.luggageAllowance = luggageAllowance;
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

    public int getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(int extraTime) {
        this.extraTime = extraTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    @Override
    public String toString() {
        return "OfferRide{" +
                "rideID='" + rideID + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", currentLocation='" + currentLocation + '\'' +
                ", destination='" + destination + '\'' +
                ", dateOfJourney='" + dateOfJourney + '\'' +
                ", seatsAvailable=" + seatsAvailable +
                ", licencePlate='" + licencePlate + '\'' +
                ", currentlongitude=" + currentlongitude +
                ", currentlatitude=" + currentlatitude +
                ", sameGender=" + sameGender +
                ", luggageAllowance=" + luggageAllowance +
                ", car='" + car + '\'' +
                ", pickupTime='" + pickupTime + '\'' +
                ", pickupLocation='" + pickupLocation + '\'' +
                ", extraTime=" + extraTime +
                ", profile_picture='" + profile_picture + '\'' +
                ", cost=" + cost +
                ", completeRides=" + completeRides +
                ", userRating=" + userRating +
                ", duration='" + duration + '\'' +
                '}';
    }
}
