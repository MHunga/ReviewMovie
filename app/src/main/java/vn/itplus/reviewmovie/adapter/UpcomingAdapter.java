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
import vn.itplus.reviewmovie.model.movie.upcoming.ResultUpcoming;
import vn.itplus.reviewmovie.onclickitem.OnClickItem;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder> {
    ArrayList<ResultUpcoming> resultUpcomings;
    Context context;
    OnClickItem onClickItem;

    public UpcomingAdapter(ArrayList<ResultUpcoming> resultUpcomings, Context context, OnClickItem onClickItem){
        this.resultUpcomings = resultUpcomings;
        this.context = context;
        this.onClickItem = onClickItem;
    }
    @NonNull
    @Override
    public UpcomingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,null);
        ViewHolder viewHolder = new ViewHolder(view,onClickItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingAdapter.ViewHolder holder, int position) {
        Picasso.get().load("https://image.tmdb.org/t/p/original"+ resultUpcomings.get(position).getPosterPath()).resize(300,340).centerCrop().into(holder.imgItemMovie);
        holder.txtMovieName.setText(resultUpcomings.get(position).getTitle());
        holder.release_date.setText(resultUpcomings.get(position).getReleaseDate());
        holder.txtPercent.setText(String.valueOf(resultUpcomings.get(position).getVoteAverage()*10));
        holder.crpv.setPercent((float) (resultUpcomings.get(position).getVoteAverage()*10));
    }

    @Override
    public int getItemCount() {
        return resultUpcomings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
onClickItem.onClickUpcoming(getAdapterPosition());
        }
    }
}
