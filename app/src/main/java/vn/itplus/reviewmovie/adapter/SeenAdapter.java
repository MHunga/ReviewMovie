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
import vn.itplus.reviewmovie.model.movie.seen.Seen;
import vn.itplus.reviewmovie.onclickitem.OnClickItem2;

public class SeenAdapter extends RecyclerView.Adapter<SeenAdapter.ViewHolder> {
    ArrayList<Seen> seenArrayList;
    Context context;
    OnClickItem2 onClickItem2;
    public SeenAdapter(ArrayList<Seen> seenArrayList, Context context,OnClickItem2 onClickItem2){
        this.context = context;
        this.seenArrayList = seenArrayList;
        this.onClickItem2 = onClickItem2;
    }
    @NonNull
    @Override
    public SeenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,null);
        ViewHolder viewHolder = new ViewHolder(view,onClickItem2) ;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SeenAdapter.ViewHolder holder, int position) {
        Seen seen = seenArrayList.get(position);
        Picasso.get().load("https://image.tmdb.org/t/p/original"+seen.getPhotoUrl()).resize(300,340).centerCrop().into(holder.imgItemMovie);

        holder.txtMovieName.setText(seen.getName());
        holder.release_date.setText(seen.getRelease_date());
        holder.crpv.setPercent((seen.getUserScore()*10));
        holder.txtPercent.setText(String.valueOf(seen.getUserScore()*10));
    }

    @Override
    public int getItemCount() {
        return seenArrayList.size();
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
