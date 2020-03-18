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
import vn.itplus.reviewmovie.model.notification.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

        ArrayList<Notification> notifications;
        Context context;

        public  NotificationAdapter ( ArrayList<Notification> notifications,Context context){
            this.notifications = notifications;
            this.context = context;
        }
    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
            holder.txtTitleNotification.setText(notifications.get(position).getTitle());
            holder.txtBodyNotification.setText(notifications.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView txtTitleNotification,txtBodyNotification;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitleNotification = itemView.findViewById(R.id.txtTitleNotification);
            txtBodyNotification = itemView.findViewById(R.id.txtBodyNotification);
        }
    }
}
