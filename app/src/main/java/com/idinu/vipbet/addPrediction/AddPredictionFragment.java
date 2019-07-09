package com.idinu.vipbet.addPrediction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idinu.vipbet.R;
import com.idinu.vipbet.home.HomeFragment;
import com.idinu.vipbet.model.Prediction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AddPredictionFragment extends Fragment {
    private EditText homeEditText;
    private EditText awayEditText;
    private EditText oddEditText;
    private EditText dateEditText;
    private EditText predictionEditText;
    private EditText leagueEditText;
    private Button savePredictionButton;
    private DatabaseReference predictionsDatabase;
    private FragmentManager fragmentManager;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_prediction, container, false);
        homeEditText = view.findViewById(R.id.homeEditText);
        awayEditText = view.findViewById(R.id.awayEditText);
        oddEditText = view.findViewById(R.id.oddEditText);
        predictionEditText = view.findViewById(R.id.predictionEditText);
        leagueEditText = view.findViewById(R.id.leagueEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        savePredictionButton = view.findViewById(R.id.savePredictionButton);
        fragmentManager = getActivity().getSupportFragmentManager();

        savePredictionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predictionsDatabase = FirebaseDatabase.getInstance().getReference().child("predictions");

                predictionsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Prediction> dataMap = (Map<String, Prediction>) dataSnapshot.getValue();
                        List<String> ids = new ArrayList<>();
                        if (dataMap != null ) {
                            for (Map.Entry<String, Prediction> prediction : dataMap.entrySet()) {
                                ids.add(prediction.getKey());
                            }
                        }
                        long id = Long.MAX_VALUE;
                        if (!ids.isEmpty()) {
                            Collections.sort(ids);
                            id = Long.parseLong(ids.get(ids.size() - 1));
                        }

                        Prediction prediction = new Prediction();
                        prediction.setHome(homeEditText.getText().toString());
                        prediction.setAway(awayEditText.getText().toString());
                        prediction.setDate(dateEditText.getText().toString());
                        prediction.setLeague(leagueEditText.getText().toString());
                        prediction.setOdd(oddEditText.getText().toString());
                        prediction.setPrediction(predictionEditText.getText().toString());
                        prediction.setId(id - 1);

                        predictionsDatabase.child(prediction.getId().toString()).setValue(prediction);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                changeFragment(new HomeFragment());
            }
        });

        return view;
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, fragment);
        fragmentTransaction.commit();
    }

}
