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

import java.util.ArrayList;
import java.util.List;

import vn.itplus.reviewmovie.R;
import vn.itplus.reviewmovie.model.cast.Cast;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {
    ArrayList<Cast> casts;
    Context context;

    public  CastAdapter(ArrayList<Cast> casts,Context context){
        this.casts = casts;
        this.context = context;
    }
    @NonNull
    @Override
    public CastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.ViewHolder holder, int position) {
        Cast cast = casts.get(position);
        holder.txtNameCast.setText(cast.getName());
        holder.txtCharacter.setText(cast.getCharacter());
        Picasso.get().load("https://image.tmdb.org/t/p/original"+cast.getProfilePath()).resize(300,340).centerCrop().into(holder.imgItemCast);
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItemCast;
        TextView txtNameCast, txtCharacter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemCast = itemView.findViewById(R.id.imgItemCast);
            txtNameCast = itemView.findViewById(R.id.txtNameCast);
            txtCharacter = itemView.findViewById(R.id.txtCharacter);

        }
    }
}
