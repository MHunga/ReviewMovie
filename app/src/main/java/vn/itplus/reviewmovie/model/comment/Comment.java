package vn.itplus.reviewmovie.model.comment;

public class Comment {
    private  String idMovie;
    private String idUser;
    private String name;
    private String description;
    private String timestamp;
    private String urlPhoto;



    public Comment() {
    }

    public Comment(String idMovie, String idUser, String name, String description, String timestamp,String urlPhoto) {
        this.idMovie = idMovie;
        this.idUser = idUser;
        this.name = name;
        this.description = description;
        this.timestamp = timestamp;
        this.urlPhoto = urlPhoto;
    }
    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
