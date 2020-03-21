package vn.itplus.reviewmovie.model.comment;

public class Comment {
    private  String idMovie;
    private String idUser;
    private String description;
    private String timestamp;



    public Comment() {
    }

    public Comment(String idMovie, String idUser, String description, String timestamp) {
        this.idMovie = idMovie;
        this.idUser = idUser;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
