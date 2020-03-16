package vn.itplus.reviewmovie.model.genreCategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GenreCategory {
    @SerializedName("genres")
    @Expose
    private ArrayList<Genre> genres = null;

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }
}
