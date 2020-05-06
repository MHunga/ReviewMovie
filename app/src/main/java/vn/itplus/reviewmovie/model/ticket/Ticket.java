package vn.itplus.reviewmovie.model.ticket;

public class Ticket {
    private String idUser;
    private String userName;
    private String phoneNumber;
    private String idMovie;
    private String movieName;
    private String cinema;
    private String seats;
    private String type;
    private String date;
    private double cost;

    public Ticket() {
    }

    public Ticket(String idUser, String userName, String phoneNumber, String idMovie,
                  String movieName, String cinema, String seats, String type, String date,
                  double cost) {
        this.idUser = idUser;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.idMovie = idMovie;
        this.movieName = movieName;
        this.cinema = cinema;
        this.seats = seats;
        this.type = type;
        this.date = date;
        this.cost = cost;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getCinema() {
        return cinema;
    }

    public void setCinema(String cinema) {
        this.cinema = cinema;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
