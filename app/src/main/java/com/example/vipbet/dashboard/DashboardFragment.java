package com.example.vipbet.dashboard;

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
import com.example.vipbet.model.Prediction;
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

public class DashboardFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("predictions");
        mRecyclerView = view.findViewById(R.id.predictionsRecyclerView);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        fetch();


        return view;
    }

    private void fetch() {
        Query query = databaseReference.orderByValue();

        FirebaseRecyclerOptions<Prediction> options =
                new FirebaseRecyclerOptions.Builder<Prediction>()
                        .setQuery(query, new SnapshotParser<Prediction>() {
                            @NonNull
                            @Override
                            public Prediction parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Prediction(snapshot.child("home").getValue().toString(),
                                        snapshot.child("away").getValue().toString(),
                                        snapshot.child("prediction").getValue().toString(),
                                        snapshot.child("date").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Prediction, PredictionsViewHolder>(options) {
            @Override
            public PredictionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.prediction_recycler_row, parent, false);

                return new PredictionsViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(PredictionsViewHolder holder, final int position, Prediction prediction) {
                holder.setHomeTextView(prediction.getHome());
                holder.setAwayTextView(prediction.getAway());
                holder.setPredictionTextView(prediction.getPrediction());
                holder.setDateTextView(prediction.getDate());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    Date parse = formatter.parse(prediction.getDate());
                    Date date = new Date();
                    if (date.before(parse)) {
                        holder.setRootRelativeLayout(R.color.playedGames);
                    } else {
                        holder.setRootRelativeLayout(R.color.availablePrediction);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        };
        mRecyclerView.setAdapter(adapter);
    }

    public class PredictionsViewHolder extends RecyclerView.ViewHolder {
        public TextView homeTextView;
        public TextView awayTextView;
        public TextView predictionTextView;
        public TextView dateTextView;
        public RelativeLayout rootRelativeLayout;

        public PredictionsViewHolder(@NonNull View itemView) {
            super(itemView);
            homeTextView = itemView.findViewById(R.id.homeEditText);
            awayTextView = itemView.findViewById(R.id.awayEditText);
            predictionTextView = itemView.findViewById(R.id.predictionEditText);
            dateTextView = itemView.findViewById(R.id.dateEditText);
            rootRelativeLayout = itemView.findViewById(R.id.recyclerViewRow);
        }

        public void setHomeTextView(String homeText) {
            homeTextView.setText(homeText);
        }

        public void setAwayTextView(String awayText) {
            awayTextView.setText(awayText);
        }

        public void setPredictionTextView(String predictionText) {
            predictionTextView.setText(predictionText);
        }

        public void setDateTextView(String date) {
            dateTextView.setText(date);
        }

        public void setRootRelativeLayout(int color) {
            rootRelativeLayout.setBackgroundResource(color);
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
