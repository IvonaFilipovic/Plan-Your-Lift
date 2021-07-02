package com.ivonafyp.planyourlift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView bottomnav = findViewById(R.id.bottomnav);

        bottomnav.setOnNavigationItemSelectedListener(menuitemsclicked);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener menuitemsclicked = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment clickeditem = null;
            switch (item.getItemId()) {
                case R.id.history:
                    clickeditem = new History();
                    break;
                case R.id.profile:
                    clickeditem = new Profile();
                    break;
                case R.id.createw:
                    clickeditem = new CreateWorkout();
                    break;
                case R.id.currentw:
                    clickeditem = new ChooseWorkout();
                    break;
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.hostfragment, clickeditem).setReorderingAllowed(true).addToBackStack("").commit();
            return true;
        }
    };
}



//Code may include some snippets from https://developer.android.com/ and https://firebase.google.com/docs






