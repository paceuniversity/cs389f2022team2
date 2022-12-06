package com.example.wellnesswatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wellnesswatch.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new StartWorkoutFragment());
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.startWorkout);




        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.history:
                    replaceFragment(new HistoricalFragment());
                    break;
                case R.id.startWorkout:
                    replaceFragment(new StartWorkoutFragment());
                    break;
                case R.id.wellness:
                    replaceFragment(new WellnessFragment());
                    break;
            }

            return true;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.settingsmenu:
                Button settings = (Button) findViewById(R.id.settingsmenu);
                settings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    }
                });

                break;
            case R.id.aboutusmenu:
                Button about = (Button) findViewById(R.id.aboutusmenu);
                about.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                    }
                });
                break;
            case R.id.logoutmenu:
                Toast.makeText(getApplicationContext(), "You've been logged out!",Toast.LENGTH_LONG).show();
                //placeholder toast popup, not sure if this will need to be an activity atm
                //Switch to log out activity on click...
                break;
        }
        return true;

    }
}