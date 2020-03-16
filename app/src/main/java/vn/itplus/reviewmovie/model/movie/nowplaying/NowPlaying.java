package vn.itplus.reviewmovie.model.movie.nowplaying;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NowPlaying {
    @SerializedName("results")
    @Expose
    private ArrayList<ResultNowPlaying> resultNowPlayings = null;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("dates")
    @Expose
    private Dates dates;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    public ArrayList<ResultNowPlaying> getResultNowPlayings() {
        return resultNowPlayings;
    }

    public void setResultNowPlayings(ArrayList<ResultNowPlaying> resultNowPlayings) {
        this.resultNowPlayings = resultNowPlayings;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Dates getDates() {
        return dates;
    }

    public void setDates(Dates dates) {
        this.dates = dates;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
