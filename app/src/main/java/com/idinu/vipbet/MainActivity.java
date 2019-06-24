package com.idinu.vipbet;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.idinu.vipbet.daily.DailyTipsFragment;
import com.idinu.vipbet.dashboard.DashboardFragment;
import com.idinu.vipbet.home.HomeFragment;
import com.idinu.vipbet.notification.NotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private TextView isAdmin;
    private FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(new HomeFragment());
                    return true;
                case R.id.navigation_dashboard:
                    changeFragment(new DashboardFragment());
                    return true;
                case R.id.navigation_daily:
                    changeFragment(new DailyTipsFragment());
                    return true;
                case R.id.navigation_notifications:
                    changeFragment(new NotificationFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        fragmentManager = getSupportFragmentManager();
        changeFragment(new HomeFragment());
//        navView.setSelectedItemId(R.id.navigation_home);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, fragment);
        fragmentTransaction.commit();
    }
}
