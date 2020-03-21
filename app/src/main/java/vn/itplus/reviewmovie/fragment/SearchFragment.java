package vn.itplus.reviewmovie.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.itplus.reviewmovie.R;
import vn.itplus.reviewmovie.adapter.GenreAdapter;
import vn.itplus.reviewmovie.model.genreCategory.Genre;
import vn.itplus.reviewmovie.model.genreCategory.GenreCategory;
import vn.itplus.reviewmovie.model.movie.search.Search;
import vn.itplus.reviewmovie.onclickitem.OnClickItem;

import vn.itplus.reviewmovie.retrofit2.MService;
import vn.itplus.reviewmovie.retrofit2.RetrofitClient;

public class SearchFragment extends Fragment implements OnClickItem {
    GenreCategory genreCategory;
    GenreAdapter genreAdapter;
    RecyclerView recyclerCategory;
    ProgressBar progressCategory;
TextView txtSearch;
ImageButton btnSearch;

    public SearchFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,null);

        addControls(view);
        getGenresMovie();
        addEvents();
        return view;
    }

    private void addEvents() {

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!txtSearch.getText().toString().isEmpty()){
                    DiscoverCategoryFragment discoverCategoryFragment = new DiscoverCategoryFragment() ;
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.begin_bottom_to_top,R.anim.exit_bottom_to_top
                            ,R.anim.begin_top_to_bottom,R.anim.exit_top_to_bottom)
                            .replace(R.id.frame_container, discoverCategoryFragment.newInstance2(txtSearch.getText().toString()))
                            .addToBackStack(null)
                            .commit();
                    }
                    else {
                        Toast.makeText(getActivity(),"Không được để trống",Toast.LENGTH_LONG).show();
                    }
                }
            });

    }

    private void getGenresMovie() {
        MService mService = RetrofitClient.getInstance().getClient().create(MService.class);
        Call<GenreCategory> call = mService.getGenre();
        call.enqueue(new Callback<GenreCategory>() {
            @Override
            public void onResponse(Call<GenreCategory> call, Response<GenreCategory> response) {
                if (response.isSuccessful()){
                    progressCategory.setVisibility(View.GONE);
                    genreCategory = response.body();
                    setRecyclerGenre(genreCategory.getGenres());
                }

            }

            @Override
            public void onFailure(Call<GenreCategory> call, Throwable t) {

            }
        });
    }

    private void addControls(View view) {
        txtSearch = view.findViewById(R.id.txtSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        recyclerCategory=view.findViewById(R.id.recyclerCategory);
        progressCategory = view.findViewById(R.id.progressCategory);
        genreCategory = new GenreCategory();
    }
    public void setRecyclerGenre(ArrayList<Genre> genre){
        genreAdapter = new GenreAdapter(genre,getActivity(),this);
        recyclerCategory.setHasFixedSize(true);
        recyclerCategory.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        recyclerCategory.setItemAnimator(new DefaultItemAnimator());
        recyclerCategory.setAdapter(genreAdapter);

    }

    @Override
    public void onClickTrending(int position) {

    }

    @Override
    public void onClickNowplaying(int position) {

    }

    @Override
    public void onClickUpcoming(int position) {

    }

    @Override
    public void onClickCategory(int position) {
        DiscoverCategoryFragment discoverCategoryFragment = new DiscoverCategoryFragment() ;
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.begin_bottom_to_top,R.anim.exit_bottom_to_top
                                         ,R.anim.begin_top_to_bottom,R.anim.exit_top_to_bottom)
                .replace(R.id.frame_container, discoverCategoryFragment.newInstance(genreCategory.getGenres().get(position).getId(), genreCategory.getGenres().get(position).getName()))
                .addToBackStack(null)
                .commit();


    }
}
