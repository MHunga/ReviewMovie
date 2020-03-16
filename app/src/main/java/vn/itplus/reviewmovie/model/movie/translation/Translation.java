package vn.itplus.reviewmovie.model.movie.translation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Translation {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("translations")
    @Expose
    private List<Translation_> translations = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Translation_> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation_> translations) {
        this.translations = translations;
    }

}
