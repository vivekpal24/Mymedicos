package com.example.my_medicos;

import android.os.Bundle;

import com.example.my_medicos.databinding.ActivityPublicationBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.my_medicos.databinding.ActivityUgExamBinding;

public class UgExamActivity extends AppCompatActivity {
    private ActivityUgExamBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUgExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Find the Toolbar by its ID
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the Toolbar as the ActionBar
        setSupportActionBar(toolbar);

        // Enable the back arrow in the Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_ughome, R.id.navigation_ugform)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_ug);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}

