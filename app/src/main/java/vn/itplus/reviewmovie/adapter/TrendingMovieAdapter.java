package vn.itplus.reviewmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.util.ArrayList;

import vn.itplus.reviewmovie.R;
import vn.itplus.reviewmovie.model.movie.trending.Result;
import vn.itplus.reviewmovie.onclickitem.OnClickItem;

public class TrendingMovieAdapter extends RecyclerView.Adapter<TrendingMovieAdapter.ViewHolder> {

    ArrayList<Result> results;
    Context context;
    OnClickItem onClickItem;

    public TrendingMovieAdapter(ArrayList<Result> results, Context context, OnClickItem onClickItem){
        this.context = context;
        this.results = results;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public TrendingMovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,null);
        ViewHolder viewHolder = new ViewHolder(view,onClickItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingMovieAdapter.ViewHolder holder, int position) {
                Result result = results.get(position);
                if (result.getName()==null){
                    holder.txtMovieName.setText(result.getTitle());
                }else {
                    holder.txtMovieName.setText(result.getName());
                }
                holder.release_date.setText(result.getReleaseDate());
                holder.crpv.setPercent((float) (result.getVoteAverage()*10));
        holder.txtPercent.setText(String.valueOf(result.getVoteAverage()*10));
        Picasso.get().load("https://image.tmdb.org/t/p/original"+result.getPosterPath()).resize(300,340).centerCrop().into(holder.imgItemMovie);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnClickItem onClickItem;
        public ImageView imgItemMovie;
        public TextView txtMovieName,txtPercent,release_date;
        public ColorfulRingProgressView crpv;
        public ViewHolder(@NonNull View itemView, OnClickItem onClickItem) {
            super(itemView);
            imgItemMovie = itemView.findViewById(R.id.imgItemMovie);
            txtMovieName = itemView.findViewById(R.id.txtMovieName);
             txtPercent = itemView.findViewById(R.id.txtPercent);
            release_date = itemView.findViewById(R.id.release_date);
            crpv = itemView.findViewById(R.id.crpv);

            this.onClickItem = onClickItem;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickItem.onClickTrending(getAdapterPosition());
        }
    }
}
