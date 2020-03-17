package vn.itplus.reviewmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import vn.itplus.reviewmovie.R;
import vn.itplus.reviewmovie.model.comment.Comment;
import vn.itplus.reviewmovie.utils.Utils;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    ArrayList<Comment> comments;
    Context context;
    public CommentAdapter(ArrayList<Comment> comments,Context context){
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
       // Picasso.get().load(comments.get(position).getUrlPhoto() + "?type=large").into(holder.thumbnail);
      try {
          if (comments.get(position).getUrlPhoto().equalsIgnoreCase("no photo")){
              Glide.with(context).load(R.drawable.profile)
                      .into(holder.thumbnail);
          }else {
              Glide.with(context).load(comments.get(position).getUrlPhoto()).into(holder.thumbnail);

          }
          holder.name.setText(comments.get(position).getName());
          Date date = Utils.StringToDate(comments.get(position).getTimestamp());
          String timestamp = Utils.getTimeStampComment(date);
          holder.timestamp.setText(timestamp);
          holder.description.setText(comments.get(position).getDescription());
      }catch (Exception e){
          e.printStackTrace();
      }

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView thumbnail;
        public TextView name,timestamp,description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.name);
            timestamp = itemView.findViewById(R.id.timestamp);
            description = itemView.findViewById(R.id.description);

        }
    }

}
