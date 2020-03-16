package vn.itplus.reviewmovie.retrofit2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.itplus.reviewmovie.model.cast.Casts;
import vn.itplus.reviewmovie.model.genreCategory.GenreCategory;
import vn.itplus.reviewmovie.model.movie.details.Movie;
import vn.itplus.reviewmovie.model.movie.discover.Discover;
import vn.itplus.reviewmovie.model.movie.nowplaying.NowPlaying;
import vn.itplus.reviewmovie.model.movie.search.Search;
import vn.itplus.reviewmovie.model.movie.translation.Translation;
import vn.itplus.reviewmovie.model.movie.trending.Trending;
import vn.itplus.reviewmovie.model.movie.upcoming.UpComing;
import vn.itplus.reviewmovie.model.video.Video;

public interface MService {

    @GET("movie/{id}/translations?api_key=9bb89316d8693b06d7a84980b29c011f")
    Call<Translation> getAnswers(@Path("id") String id);

    @GET("trending/movie/day?api_key=9bb89316d8693b06d7a84980b29c011f")
    Call<Trending> getTrending();
    @GET("movie/now_playing?api_key=9bb89316d8693b06d7a84980b29c011f&page=1`")
    Call<NowPlaying> getNowPlaying();

    @GET("movie/upcoming?api_key=9bb89316d8693b06d7a84980b29c011f&page=1&region=VN")
    Call<UpComing> getUpComing();

    @GET("movie/{id}/credits?api_key=9bb89316d8693b06d7a84980b29c011f")
    Call<Casts> getCasts(@Path("id") String id);

    @GET("movie/{id}/videos?api_key=9bb89316d8693b06d7a84980b29c011f")
    Call<Video> getVideo(@Path("id") String id);

    @GET("movie/{id}?api_key=9bb89316d8693b06d7a84980b29c011f")
    Call<Movie> getDetails(@Path("id") String id);
    @GET("genre/movie/list?api_key=9bb89316d8693b06d7a84980b29c011f&language=vi-VN")
    Call<GenreCategory> getGenre();
    @GET("discover/movie?api_key=9bb89316d8693b06d7a84980b29c011f&sort_by=popularity.desc&page=1")
    Call<Discover> getDiscover(@Query("page") int page,@Query("with_genres") int id);
    @GET("search/movie?api_key=9bb89316d8693b06d7a84980b29c011f")
    Call<Search> getSearch(@Query("query") String query,@Query("page") int page);
}
