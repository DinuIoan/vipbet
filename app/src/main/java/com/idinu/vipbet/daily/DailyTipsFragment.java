package com.idinu.vipbet.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idinu.vipbet.R;
import com.idinu.vipbet.model.Prediction;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DailyTipsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private DatabaseReference databaseReference;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private LinearLayout dailyTipsView;
    private View mProgressView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_tips, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("daily_tips");
        mRecyclerView = view.findViewById(R.id.dailyTipsRecyclerView);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        dailyTipsView = view.findViewById(R.id.daily_tips_view);
        mProgressView = view.findViewById(R.id.daily_tips_progress);

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
                                        snapshot.child("date").getValue().toString(),
                                        snapshot.child("odd").getValue().toString(),
                                        snapshot.child("league").getValue().toString());
                            }
                        })
                        .build();
        adapter = new FirebaseRecyclerAdapter<Prediction, DailyTipsFragment.DailyTipsViewHolder>(options) {
            @Override
            public DailyTipsFragment.DailyTipsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.daily_tips_recycler_row, parent, false);

                return new DailyTipsFragment.DailyTipsViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(DailyTipsFragment.DailyTipsViewHolder holder, final int position, Prediction prediction) {
                holder.setHomeTextView(prediction.getHome());
                holder.setAwayTextView(prediction.getAway());
                holder.setPredictionTextView(prediction.getPrediction());
                holder.setDateTextView(prediction.getDate());
                holder.setOddTextView(prediction.getOdd());
                holder.setLeagueTextView(prediction.getLeague());
            }

        };
        mRecyclerView.setAdapter(adapter);
    }

    public class DailyTipsViewHolder extends RecyclerView.ViewHolder {
        public TextView homeTextView;
        public TextView awayTextView;
        public TextView predictionTextView;
        public TextView dateTextView;
        public TextView oddTextView;
        public TextView leagueTextView;

        public DailyTipsViewHolder(@NonNull View itemView) {
            super(itemView);
            homeTextView = itemView.findViewById(R.id.homeEditText);
            awayTextView = itemView.findViewById(R.id.awayEditText);
            predictionTextView = itemView.findViewById(R.id.predictionEditText);
            dateTextView = itemView.findViewById(R.id.dateEditText);
            oddTextView = itemView.findViewById(R.id.oddEditText);
            leagueTextView = itemView.findViewById(R.id.leagueTextView);
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


        public void setOddTextView(String odd) {
            oddTextView.setText(odd);
        }

        public void setLeagueTextView(String league) {
            leagueTextView.setText(league);
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
