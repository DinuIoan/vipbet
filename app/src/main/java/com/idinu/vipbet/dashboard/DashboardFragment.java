package com.idinu.vipbet.dashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private DatabaseReference databaseReference;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private LinearLayout dashboardView;
    private View mProgressView;

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
        dashboardView = view.findViewById(R.id.dashboardView);
        mProgressView = view.findViewById(R.id.dashboard_progress);
        showProgress(true);
        fetch();
        showProgress(false);

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
                holder.setOddTextView(prediction.getOdd());
                holder.setLeagueTextView(prediction.getLeague());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    Date parse = formatter.parse(prediction.getDate());
                    Date date = new Date();
                    if (date.after(parse)) {
                        holder.setRootRelativeLayout(R.color.themeMainColorDarkBlue);
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
        public TextView oddTextView;
        public TextView leagueTextView;
        public RelativeLayout rootRelativeLayout;

        public PredictionsViewHolder(@NonNull View itemView) {
            super(itemView);
            homeTextView = itemView.findViewById(R.id.homeEditText);
            awayTextView = itemView.findViewById(R.id.awayEditText);
            predictionTextView = itemView.findViewById(R.id.predictionEditText);
            dateTextView = itemView.findViewById(R.id.dateEditText);
            oddTextView = itemView.findViewById(R.id.oddTextView);
            leagueTextView = itemView.findViewById(R.id.leagueTextView);
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


        public void setOddTextView(String odd) {
            oddTextView.setText(odd);
        }

        public void setRootRelativeLayout(int color) {
            rootRelativeLayout.setBackgroundResource(color);
        }

        public void setLeagueTextView(String league) {
            leagueTextView.setText(league);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            dashboardView.setVisibility(show ? View.GONE : View.VISIBLE);
            dashboardView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dashboardView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            dashboardView.setVisibility(show ? View.GONE : View.VISIBLE);
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
