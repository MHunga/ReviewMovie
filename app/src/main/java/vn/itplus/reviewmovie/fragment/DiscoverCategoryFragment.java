package vn.itplus.reviewmovie.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.itplus.reviewmovie.DetailsMovie;
import vn.itplus.reviewmovie.R;
import vn.itplus.reviewmovie.adapter.CategoryAdapter;
import vn.itplus.reviewmovie.adapter.SearchAdapter;
import vn.itplus.reviewmovie.model.movie.discover.Discover;
import vn.itplus.reviewmovie.model.movie.discover.Result;
import vn.itplus.reviewmovie.model.movie.search.ResultSearch;
import vn.itplus.reviewmovie.model.movie.search.Search;
import vn.itplus.reviewmovie.onclickitem.OnClickItem2;
import vn.itplus.reviewmovie.retrofit2.MService;
import vn.itplus.reviewmovie.retrofit2.RetrofitClient;

public class DiscoverCategoryFragment extends Fragment implements OnClickItem2 {
    int id;
    String name,query;
    TextView txtCategoryItemName,txtTotalPage;
    ImageButton btnBackToCategory;
    Button  btnPreviousPage, btnNextPage;
    RecyclerView recyclerByCategory;
    Discover discover;
    ArrayList<Result> results;
    CategoryAdapter categoryAdapter;
    Search search;
    SearchAdapter searchAdapter;
    int page=1;

    public static DiscoverCategoryFragment newInstance(int id, String name) {
        DiscoverCategoryFragment discoverCategoryFragment = new DiscoverCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("getID",id);
        bundle.putString("getName",name);
        discoverCategoryFragment.setArguments(bundle);
        return discoverCategoryFragment;

    }
    public static DiscoverCategoryFragment newInstance2(String query ) {
        DiscoverCategoryFragment discoverCategoryFragment = new DiscoverCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("getQuery",query);
        discoverCategoryFragment.setArguments(bundle);
        return discoverCategoryFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_by_category, null);
       try {
           id = getArguments().getInt("getID");
           name = getArguments().getString("getName");
           query = getArguments().getString("getQuery");
       }catch (Exception e){
           e.printStackTrace();
       }

        addControls(view);


        addEvents();
        if (name!=null) {
            txtCategoryItemName.setText(name);
            getDiscover(page, id);
        }else {
            getSearch(query,page);
            txtCategoryItemName.setText("Kết quả tìm kiếm");
        }
        return view;
    }

    private void getSearch(String query,int page) {
        MService mService = RetrofitClient.getInstance().getClientSearch().create(MService.class);
        Call<Search> call = mService.getSearch(query,page);
        call.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                search = response.body();
                txtTotalPage.setText(page+"/"+search.getTotalPages());
                if (search.getTotalPages()==page){
                    btnNextPage.setEnabled(false);
                    btnNextPage.setTextColor(getResources().getColor(R.color.disable_button_change_page));
                }else {
                    btnNextPage.setEnabled(true);
                    btnNextPage.setTextColor(getResources().getColor(R.color.enable_button_change_page));
                }
                setRecyclerSearch(search.getResults());
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

            }
        });
    }



    private void addEvents() {
        btnBackToCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        btnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                page ++;
                if (name!=null) {
                    getDiscover(page, id);
                }else {
                    getSearch(query,page);
                }
                btnPreviousPage.setEnabled(true);
                btnPreviousPage.setTextColor(getResources().getColor(R.color.enable_button_change_page));
            }
        });
      btnPreviousPage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              page --;
              if (page>1){
                  btnPreviousPage.setEnabled(true);

                  if (name!=null) {
                      getDiscover(page, id);
                  }else {
                      getSearch(query,page);
                  }
              }else {
                  btnPreviousPage.setEnabled(false);
                  if (name!=null) {
                      getDiscover(page, id);
                  }else {
                      getSearch(query,page);
                  }
                  btnPreviousPage.setTextColor(getResources().getColor(R.color.disable_button_change_page));
              }

          }
      });
    }

    private void getDiscover(int page,int id) {
        MService mService = RetrofitClient.getInstance().getClientDiscover().create(MService.class);
        Call<Discover> call = mService.getDiscover(page,id);
        call.enqueue(new Callback<Discover>() {
            @Override
            public void onResponse(Call<Discover> call, Response<Discover> response) {
               try {
                   discover = response.body();
                   txtTotalPage.setText(page+"/"+discover.getTotalPages());
                   results.clear();
                   results = discover.getResults();
                   setRecyclerByCategory(results);
               }catch (Exception e)
               {
                   e.printStackTrace();
               }

            }

            @Override
            public void onFailure(Call<Discover> call, Throwable t) {

            }
        });
    }
    private void setRecyclerSearch(ArrayList<ResultSearch> results) {
        searchAdapter = new SearchAdapter(results,getContext(),this);
        recyclerByCategory.setHasFixedSize(true);
        recyclerByCategory.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        recyclerByCategory.setItemAnimator(new DefaultItemAnimator());
        recyclerByCategory.setAdapter(searchAdapter);
    }
    public void setRecyclerByCategory(ArrayList<Result> results){
        categoryAdapter = new CategoryAdapter(results,getContext(),this);
        recyclerByCategory.setHasFixedSize(true);
        recyclerByCategory.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        recyclerByCategory.setItemAnimator(new DefaultItemAnimator());
        recyclerByCategory.setAdapter(categoryAdapter);
    }

    private void addControls(View view) {
        txtTotalPage= view.findViewById(R.id.txtTotalPage);
        btnPreviousPage = view.findViewById(R.id.btnPreviousPage);
        btnNextPage = view.findViewById(R.id.btnNextPage);
        btnBackToCategory  = view.findViewById(R.id.btnBackToCategory);
        txtCategoryItemName = view.findViewById(R.id.txtCategoryItemName);
        recyclerByCategory = view.findViewById(R.id.recyclerByCategory);
        discover = new Discover();
        results = new ArrayList<>();
        search = new Search();
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity().getBaseContext(), DetailsMovie.class);
        if (name != null) {
            Log.d("TAG: ID", String.valueOf(results.get(position).getId()));
            intent.putExtra("id", String.valueOf(results.get(position).getId()).trim());
            intent.putExtra("title", results.get(position).getTitle());
            intent.putExtra("overview", results.get(position).getOverview());
            startActivity(intent);
        }else {
            intent.putExtra("id", String.valueOf(search.getResults().get(position).getId()).trim());
            intent.putExtra("title", search.getResults().get(position).getTitle());
            intent.putExtra("overview", search.getResults().get(position).getOverview());
            startActivity(intent);
        }
    }

    }


