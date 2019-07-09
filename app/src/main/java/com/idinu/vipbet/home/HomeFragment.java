package com.idinu.vipbet.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.idinu.vipbet.R;
import com.idinu.vipbet.addPrediction.AddPredictionFragment;
import com.idinu.vipbet.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idinu.vipbet.model.Prediction;
import com.idinu.vipbet.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserDatabaseReference;
    private DatabaseReference cleanUpDatabaseReference;
    private RelativeLayout adminRelatieLayout;
    private RelativeLayout normalRelatieLayout;
    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView logoutButton;
    private TextView logoutButtonAdmin;
    private Button addPredictionButton;
    private Button cleanUpButton;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //ADMIN
        adminRelatieLayout = view.findViewById(R.id.adminView);
        addPredictionButton = view.findViewById(R.id.addPredictionButton);
        cleanUpButton = view.findViewById(R.id.cleanUpButton);
        logoutButtonAdmin = view.findViewById(R.id.logoutButtonAdmin);

        //GUEST
        normalRelatieLayout = view.findViewById(R.id.normalUserView);
        normalRelatieLayout.setVisibility(View.INVISIBLE);
        adminRelatieLayout.setVisibility(View.INVISIBLE);
//        logoutButton = view.findViewById(R.id.logoutButton);
        usernameTextView = view.findViewById(R.id.username);
        logoutButton = view.findViewById(R.id.logoutButton);
//        emailTextView = view.findViewById(R.id.emailTextView);
//
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AuthUI.getInstance()
//                        .signOut(getContext())
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
//                                startActivity(loginIntent);
//                            }
//                        });
//            }
//        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(loginIntent);
            }
        });
        logoutButtonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(loginIntent);
            }
        });

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("user_data")
                .child(mCurrentUser.getUid());
        getUser();


        fragmentManager = getActivity().getSupportFragmentManager();

        addPredictionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new AddPredictionFragment());
            }
        });

        cleanUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanUpDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user_data");
                cleanUpDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Map<String, Object>> dataMap = (Map<String, Map<String, Object>>) dataSnapshot.getValue();
                        if (dataMap != null ) {
                            List<String> removedUserId = new ArrayList<>();
                            for (Map.Entry<String, Map<String, Object>> userEntry: dataMap.entrySet()) {
                                Boolean isAdmin = (Boolean) userEntry.getValue().get("admin");
                                if (!isAdmin) {
                                    removedUserId.add(userEntry.getKey());
                                }
                            }
                            for (String userId: removedUserId) {
                                cleanUpDatabaseReference.child(userId).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;
    }

    private void getUser() {
        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean admin = (Boolean) dataSnapshot.child("admin").getValue();
                String  name =  dataSnapshot.child("name").getValue().toString();
                if (admin) {
                    adminRelatieLayout.setVisibility(View.VISIBLE);
                    normalRelatieLayout.setVisibility(View.INVISIBLE);
                } else {
                    adminRelatieLayout.setVisibility(View.INVISIBLE);
                    normalRelatieLayout.setVisibility(View.VISIBLE);
                }
                usernameTextView.setText(name);
//                emailTextView.setText(mCurrentUser.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, fragment);
        fragmentTransaction.commit();
    }
}
