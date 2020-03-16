package vn.itplus.reviewmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.itplus.reviewmovie.R;
import vn.itplus.reviewmovie.model.genreCategory.Genre;
import vn.itplus.reviewmovie.model.genreCategory.GenreCategory;
import vn.itplus.reviewmovie.onclickitem.OnClickItem;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    ArrayList<Genre> genres;
    Context context;
    OnClickItem onClickItem;
    public GenreAdapter(  ArrayList<Genre> genres, Context context,OnClickItem onClickItem){
        this.genres = genres;
        this.context = context;
        this.onClickItem = onClickItem;
    }
    @NonNull
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,null);
        ViewHolder viewHolder = new ViewHolder(view,onClickItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.ViewHolder holder, int position) {
        holder.txtCategory.setText(genres.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtCategory;
       public OnClickItem onClickItem;
        public ViewHolder(@NonNull View itemView, OnClickItem onClickItem) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);

            this.onClickItem = onClickItem;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickItem.onClickCategory(getAdapterPosition());
        }
    }
}
