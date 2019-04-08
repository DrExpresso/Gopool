package firebase.gopool.models;

public class User {

    private String user_id;
    private String email;
    private String full_name;
    private String username;
    private String profile_photo;
    private String dob;
    private String licence_number;
    private String gender;
    private String registration_plate;
    private String car;
    private String car_photo;
    private String education;
    private String work;
    private String bio;
    private Long mobile_number;
    private int completedRides;
    private int seats;
    private int userRating;
    private int points;
    private Boolean carOwner;

    public User() { }

    public User(String user_id, String email, String full_name, String username, String profile_photo, Long mobile_number, String dob, String licence_number, int completedRides, int userRating,
                String car, String registration_plate, int seats, String education, String work, String bio, Boolean carOwner, String gender, int points, String car_photo) {
        this.user_id = user_id;
        this.email = email;
        this.full_name = full_name;
        this.username = username;
        this.profile_photo = profile_photo;
        this.mobile_number = mobile_number;
        this.completedRides = completedRides;
        this.dob = dob;
        this.licence_number = licence_number;
        this.userRating = userRating;
        this.carOwner = carOwner;
        this.seats = seats;
        this.registration_plate = registration_plate;
        this.gender = gender;
        this.car = car;
        this.car_photo = car_photo;
        this.education = education;
        this.work = work;
        this.points = points;
        this.bio = bio;
    }

    public String getUser_id() {
        return user_id;
    }


    public String getEmail() {
        return email;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getUsername() {
        return username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getMobile_number() {
        return mobile_number;
    }

    public String getDob() {
        return dob;
    }

    public void setMobile_number(Long mobile_number) {
        this.mobile_number = mobile_number;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getLicence_number() {
        return licence_number;
    }

    public void setLicence_number(String licence_number) {
        this.licence_number = licence_number;
    }

    public int getCompletedRides() {
        return completedRides;
    }

    public void setCompletedRides(int completedRides) {
        this.completedRides = completedRides;
    }

    public String getGender() {
        return gender;
    }

    public String getRegistration_plate() {
        return registration_plate;
    }

    public String getCar() {
        return car;
    }

    public int getSeats() {
        return seats;
    }

    public Boolean getCarOwner() {
        return carOwner;
    }

    public String getCar_photo() {
        return car_photo;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setRegistration_plate(String registration_plate) {
        this.registration_plate = registration_plate;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public void setCar_photo(String car_photo) {
        this.car_photo = car_photo;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setCarOwner(Boolean carOwner) {
        this.carOwner = carOwner;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", full_name='" + full_name + '\'' +
                ", username='" + username + '\'' +
                ", profile_photo='" + profile_photo + '\'' +
                ", dob='" + dob + '\'' +
                ", licence_number='" + licence_number + '\'' +
                ", gender='" + gender + '\'' +
                ", registration_plate='" + registration_plate + '\'' +
                ", car='" + car + '\'' +
                ", car_photo='" + car_photo + '\'' +
                ", education='" + education + '\'' +
                ", work='" + work + '\'' +
                ", bio='" + bio + '\'' +
                ", mobile_number=" + mobile_number +
                ", completedRides=" + completedRides +
                ", seats=" + seats +
                ", userRating=" + userRating +
                ", points=" + points +
                ", carOwner=" + carOwner +
                '}';
    }
}
