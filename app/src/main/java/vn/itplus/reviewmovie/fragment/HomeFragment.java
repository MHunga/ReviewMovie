package vn.itplus.reviewmovie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.itplus.reviewmovie.DetailsMovie;
import vn.itplus.reviewmovie.R;


import vn.itplus.reviewmovie.adapter.NowPlayingAdapter;
import vn.itplus.reviewmovie.adapter.TrendingMovieAdapter;
import vn.itplus.reviewmovie.adapter.UpcomingAdapter;
import vn.itplus.reviewmovie.model.movie.details.Movie;
import vn.itplus.reviewmovie.model.movie.nowplaying.NowPlaying;
import vn.itplus.reviewmovie.model.movie.nowplaying.ResultNowPlaying;
import vn.itplus.reviewmovie.model.movie.trending.Result;
import vn.itplus.reviewmovie.model.movie.trending.Trending;
import vn.itplus.reviewmovie.model.movie.upcoming.ResultUpcoming;
import vn.itplus.reviewmovie.model.movie.upcoming.UpComing;
import vn.itplus.reviewmovie.onclickitem.OnClickItem;
import vn.itplus.reviewmovie.retrofit2.MService;
import vn.itplus.reviewmovie.retrofit2.RetrofitClient;
import vn.itplus.reviewmovie.utils.Utils;

public class HomeFragment extends Fragment implements OnClickItem {
    ProgressBar progressBarHome;
    RecyclerView recyclerViewMovie, recyclerNowPlaying,recyclerViewUpcoming;





    Trending trending;
    TrendingMovieAdapter trendingMovieAdapter;
    NowPlaying nowPlaying;
    NowPlayingAdapter nowPlayingAdapter;

    UpComing upComing;
    UpcomingAdapter upcomingAdapter;
    Movie movie;
    ArrayList<Result> results;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        addControls(view);

        getTrendingMovies();


        return view;
    }



    private void getTrendingMovies() {

        MService mService = RetrofitClient.getInstance().getClient().create(MService.class);
        Call<Trending> call = mService.getTrending().clone();
        Call<NowPlaying> call1 = mService.getNowPlaying().clone();
        Call<UpComing> call2 = mService.getUpComing().clone();
        call2.enqueue(new Callback<UpComing>() {
            @Override
            public void onResponse(Call<UpComing> call, Response<UpComing> response) {
                upComing = response.body();

                   setRecyclerViewUpcoming(upComing.getResultUpcomings());

            }

            @Override
            public void onFailure(Call<UpComing> call, Throwable t) {

            }
        });
        call1.enqueue(new Callback<NowPlaying>() {
            @Override
            public void onResponse(Call<NowPlaying> call, Response<NowPlaying> response) {
                nowPlaying = response.body();
                Log.d("UPPPPPP",nowPlaying.getResultNowPlayings().get(0).getTitle());
      setRecyclerViewNowPlaying(nowPlaying.getResultNowPlayings());

            }

            @Override
            public void onFailure(Call<NowPlaying> call, Throwable t) {

            }
        });
        call.enqueue(new Callback<Trending>() {
            @Override
            public void onResponse(Call<Trending> call, Response<Trending> response) {
                trending = response.body();
                results = trending.getResults();
                 setRecyclerViewTrending(results);
                progressBarHome.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Trending> call, Throwable t) {

            }
        });


    }


    public void addControls(View view) {
        progressBarHome = view.findViewById(R.id.progressBarHome);

        recyclerViewMovie = view.findViewById(R.id.recyclerViewMovie);
        recyclerNowPlaying = view.findViewById(R.id.recyclerNowPlaying);
        recyclerViewUpcoming = view.findViewById(R.id.recyclerUpComing);
        movie = new Movie();
        results = new ArrayList<>();
        trending = new Trending();
        nowPlaying = new NowPlaying();
        upComing = new UpComing();

    }

    public void setRecyclerViewTrending(ArrayList<Result> results) {
        trendingMovieAdapter = new TrendingMovieAdapter(results, getActivity(), this);
        recyclerViewMovie.setHasFixedSize(true);
        recyclerViewMovie.setLayoutManager(new GridLayoutManager(getActivity(), 1, RecyclerView.HORIZONTAL, false));

        recyclerViewMovie.setItemAnimator(new DefaultItemAnimator());

        recyclerViewMovie.setAdapter(trendingMovieAdapter);
    }

    public void setRecyclerViewNowPlaying(ArrayList<ResultNowPlaying> resultNowPlayings) {
        nowPlayingAdapter = new NowPlayingAdapter(resultNowPlayings, getActivity(), this);
        recyclerNowPlaying.setHasFixedSize(true);
        recyclerNowPlaying.setLayoutManager(new GridLayoutManager(getActivity(), 1, RecyclerView.HORIZONTAL, false));
        recyclerNowPlaying.setItemAnimator(new DefaultItemAnimator());

        recyclerNowPlaying.setAdapter(nowPlayingAdapter);

    }
    public void setRecyclerViewUpcoming(ArrayList<ResultUpcoming> resultUpcomings) {
        upcomingAdapter = new UpcomingAdapter(resultUpcomings,getActivity(),this);
        recyclerViewUpcoming.setHasFixedSize(true);
        recyclerViewUpcoming.setLayoutManager(new GridLayoutManager(getActivity(), 1, RecyclerView.HORIZONTAL, false));
        recyclerViewUpcoming.setItemAnimator(new DefaultItemAnimator());

        recyclerViewUpcoming.setAdapter(upcomingAdapter);

    }


    @Override
    public void onClickTrending(int position) {

        Utils.addSeenDatabase(getActivity(),trending.getResults().get(position).getId(),
                trending.getResults().get(position).getTitle(),trending.getResults().get(position).getPosterPath(),
                trending.getResults().get(position).getReleaseDate(),trending.getResults().get(position).getVoteCount());
        Intent intent = new Intent(getActivity().getBaseContext(), DetailsMovie.class);
        Log.d("TAG: ID", String.valueOf(trending.getResults().get(position).getId()));
        intent.putExtra("datVe", "no");
        intent.putExtra("id", String.valueOf(trending.getResults().get(position).getId()).trim());
        intent.putExtra("title", trending.getResults().get(position).getTitle());
        intent.putExtra("overview", trending.getResults().get(position).getOverview());
        startActivity(intent);
    }

    @Override
    public void onClickNowplaying(int position) {
        Utils.addSeenDatabase(getActivity(),nowPlaying.getResultNowPlayings().get(position).getId(),
                nowPlaying.getResultNowPlayings().get(position).getTitle(),nowPlaying.getResultNowPlayings().get(position).getPosterPath(),
                nowPlaying.getResultNowPlayings().get(position).getReleaseDate(),nowPlaying.getResultNowPlayings().get(position).getVoteCount());
        Intent intent = new Intent(getActivity().getBaseContext(), DetailsMovie.class);
        intent.putExtra("datVe", "yes");
        intent.putExtra("id", String.valueOf(nowPlaying.getResultNowPlayings().get(position).getId()));
        intent.putExtra("title", nowPlaying.getResultNowPlayings().get(position).getTitle());
        intent.putExtra("overview", nowPlaying.getResultNowPlayings().get(position).getOverview());
        startActivity(intent);
    }

    @Override
    public void onClickUpcoming(int position) {
        Utils.addSeenDatabase(getActivity(),upComing.getResultUpcomings().get(position).getId(),
                upComing.getResultUpcomings().get(position).getTitle(),upComing.getResultUpcomings().get(position).getPosterPath(),
                upComing.getResultUpcomings().get(position).getReleaseDate(),upComing.getResultUpcomings().get(position).getVoteCount());
        Intent intent = new Intent(getActivity().getBaseContext(), DetailsMovie.class);
        intent.putExtra("datVe", "no");
        intent.putExtra("id", String.valueOf(upComing.getResultUpcomings().get(position).getId()));
        intent.putExtra("title", upComing.getResultUpcomings().get(position).getTitle());
        intent.putExtra("overview", upComing.getResultUpcomings().get(position).getOverview());
        startActivity(intent);
    }

    @Override
    public void onClickCategory(int position) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
