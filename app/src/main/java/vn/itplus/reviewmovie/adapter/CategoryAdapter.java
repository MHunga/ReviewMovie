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
import vn.itplus.reviewmovie.model.movie.discover.Result;
import vn.itplus.reviewmovie.onclickitem.OnClickItem2;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
ArrayList<Result> results;
Context context;
OnClickItem2 onClickItem2;

    public CategoryAdapter(ArrayList<Result> results, Context context, OnClickItem2 onClickItem2) {
        this.results = results;
        this.context = context;
        this.onClickItem2 = onClickItem2;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,null);
        ViewHolder viewHolder = new ViewHolder(view,onClickItem2);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Result result = results.get(position);
        Picasso.get().load("https://image.tmdb.org/t/p/original"+result.getPosterPath()).resize(300,340).centerCrop().into(holder.imgItemMovie);

            holder.txtMovieName.setText(result.getTitle());
        holder.release_date.setText(result.getReleaseDate());
        holder.crpv.setPercent((float) (result.getVoteAverage()*10));
        holder.txtPercent.setText(String.valueOf(result.getVoteAverage()*10));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgItemMovie;
        public TextView txtMovieName,txtPercent,release_date;
        public ColorfulRingProgressView crpv;
        private OnClickItem2 onClickItem2;
        public ViewHolder(@NonNull View itemView, OnClickItem2 onClickItem2) {
            super(itemView);
            imgItemMovie = itemView.findViewById(R.id.imgItemMovie);
            txtMovieName = itemView.findViewById(R.id.txtMovieName);
            txtPercent = itemView.findViewById(R.id.txtPercent);
            release_date = itemView.findViewById(R.id.release_date);
            crpv = itemView.findViewById(R.id.crpv);

            this.onClickItem2 = onClickItem2;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickItem2.onClick(getAdapterPosition());
        }
    }
}
