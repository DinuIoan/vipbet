package com.example.vipbet.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vipbet.R;
import com.example.vipbet.model.Notification;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationFragment  extends Fragment {
    private RecyclerView mRecyclerView;
    private DatabaseReference databaseReference;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("notifications");
        mRecyclerView = view.findViewById(R.id.notificationsRecyclerView);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        fetch();
        return view;
    }

    private void fetch() {
        Query query = databaseReference.orderByValue();

        FirebaseRecyclerOptions<Notification> options =
                new FirebaseRecyclerOptions.Builder<Notification>()
                        .setQuery(query, new SnapshotParser<Notification>() {
                            @NonNull
                            @Override
                            public Notification parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Notification(
                                        snapshot.child("message").getValue().toString(),
                                        snapshot.child("title").getValue().toString(),
                                        snapshot.child("date").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Notification, NotificationsViewHolder>(options) {
            @Override
            public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notifications_recycler_row, parent, false);

                return new NotificationsViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(NotificationsViewHolder holder, final int position, Notification notification) {
                holder.setMessage(notification.getMessage());
                holder.setTitle(notification.getTitle());
                holder.setNotificationDate(notification.getDate());
            }

        };
        mRecyclerView.setAdapter(adapter);
    }

    public class NotificationsViewHolder extends RecyclerView.ViewHolder {
        public TextView notificationMessage;
        public TextView notificationTitle;
        public TextView notificationDate;

        public NotificationsViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationDate = itemView.findViewById(R.id.notificationDateTextView);
            notificationMessage = itemView.findViewById(R.id.messageTextView);
            notificationTitle = itemView.findViewById(R.id.titleTextView);
        }

        public void setTitle(String notificationTitleText) {
            notificationTitle.setText(notificationTitleText);
        }

        public void setMessage(String message) {
            notificationMessage.setText(message);
        }

        public void setNotificationDate(String date) {
            notificationDate.setText(date);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
