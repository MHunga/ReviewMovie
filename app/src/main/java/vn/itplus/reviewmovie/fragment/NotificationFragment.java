package vn.itplus.reviewmovie.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.itplus.reviewmovie.R;
import vn.itplus.reviewmovie.adapter.NotificationAdapter;
import vn.itplus.reviewmovie.model.notification.Notification;
import vn.itplus.reviewmovie.notification.OpenHelperDataBaseNotification;

public class NotificationFragment extends Fragment {

    ArrayList<Notification> notifications;
    NotificationAdapter notificationAdapter;
    TextView checkNullNotification;

    RecyclerView recyclerNotifications;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification,null);
        addControls(view);
        getNotifications();
        return view;
    }

    private void getNotifications() {

        OpenHelperDataBaseNotification dataBaseNotification = new OpenHelperDataBaseNotification(getActivity());
        notifications = dataBaseNotification.getAll();
      // Log.d("DATANOTI",notifications.get(0).getBody());

        if (notifications.size()>0){
        notificationAdapter = new NotificationAdapter(notifications,getActivity());
        recyclerNotifications.setHasFixedSize(true);
        recyclerNotifications.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        recyclerNotifications.setItemAnimator(new DefaultItemAnimator());
        recyclerNotifications.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();
            checkNullNotification.setVisibility(View.GONE);
    }
    }

    private void addControls(View view) {
        recyclerNotifications = view.findViewById(R.id.recyclerNotifications);
        checkNullNotification = view.findViewById(R.id.checkNullNotification);
        notifications = new ArrayList<>();
    }
}
