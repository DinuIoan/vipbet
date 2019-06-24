package com.idinu.vipbet.database;

import com.idinu.vipbet.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRepository {
    private DatabaseReference mDatabase;

    public FirebaseRepository() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void addUser(User user) {
//        mDatabase.child("user_data").setValue(user.getId());
        mDatabase.child("user_data").child("" + user.getId()).setValue(user);
    }
}
