package vn.itplus.reviewmovie.model.movie.seen;

public class Seen {
    private int id;
    private String name;
    private String photoUrl;
    private float userScore;
    private String release_date;

    public Seen() {
    }

    public Seen(int id, String name, String photoUrl, float userScore, String release_date) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.userScore = userScore;
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public float getUserScore() {
        return userScore;
    }

    public void setUserScore(float userScore) {
        this.userScore = userScore;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
