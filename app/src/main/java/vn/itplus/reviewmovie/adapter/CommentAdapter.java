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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import vn.itplus.reviewmovie.R;
import vn.itplus.reviewmovie.model.comment.Comment;
import vn.itplus.reviewmovie.model.user.User;
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
      try {
          DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
          databaseReference.child(comments.get(position).getIdUser()).addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              try {
                  User user = dataSnapshot.getValue(User.class);
                  holder.name.setText(user.getName());
                  if (user.getPhotoURL().equalsIgnoreCase("no photo")){
                      Glide.with(context).load(R.drawable.profile)
                              .into(holder.thumbnail);
                  }else {
                      Glide.with(context).load(user.getPhotoURL()).into(holder.thumbnail);

                  }
              }catch (Exception e){
                  e.printStackTrace();
              }

              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });


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
