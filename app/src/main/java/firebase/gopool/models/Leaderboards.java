package firebase.gopool.models;

public class Leaderboards{

    public String username;
    public String profile_photo;
    public int points;

    public Leaderboards() {
    }

    public Leaderboards(String username, String profile_photo, int points) {
        this.username = username;
        this.profile_photo = profile_photo;
        this.points = points;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Leaderboards{" +
                "username='" + username + '\'' +
                ", profile_photo='" + profile_photo + '\'' +
                ", points='" + points + '\'' +
                '}';
    }
}
