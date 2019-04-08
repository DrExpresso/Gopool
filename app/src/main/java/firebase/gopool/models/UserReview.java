package firebase.gopool.models;

public class UserReview {

    public String comment;
    public Float rating;

    public UserReview() {
    }

    public UserReview(Float rating, String comment) {
        this.comment = comment;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "UserReview{" +
                "comment='" + comment + '\'' +
                ", rating=" + rating +
                '}';
    }
}
