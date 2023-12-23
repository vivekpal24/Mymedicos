package com.medical.my_medicos.activities.pg.activites.insiders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.fragment.QuestionbankFragment;
import com.medical.my_medicos.activities.pg.fragment.VideoBankFragment;
import com.medical.my_medicos.activities.pg.fragment.WeeklyQuizFragment;
import com.medical.my_medicos.databinding.ActivitySpecialityPgBinding;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SpecialityPGInsiderActivity extends AppCompatActivity {

    private ActivitySpecialityPgBinding binding;
    private BottomNavigationView bottomNavigationCategoryPublication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpecialityPgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupBottomAppBar();

        if (savedInstanceState == null) {
            // Initial fragment setup
            String title = getIntent().getStringExtra("specialityPgName");
            replaceFragment(QuestionbankFragment.newInstance(0, title));
        }
    }

    private void setupToolbar() {
        binding.specialitytoolbar.setTitle(getIntent().getStringExtra("specialityPgName"));
        setSupportActionBar(binding.specialitytoolbar);
    }

    private void setupBottomAppBar() {
        BottomAppBar bottomAppBar = binding.bottomappabarpg;
        bottomNavigationCategoryPublication = bottomAppBar.findViewById(R.id.bottomNavigationViewpg);
        bottomNavigationCategoryPublication.setBackground(null);

        bottomNavigationCategoryPublication.setOnItemSelectedListener(item -> {
            int frgId = item.getItemId();
            Log.d("ItemSelected", "Fragment ID: " + frgId);
            if (frgId == R.id.qb) {
                String title = getTitleFromIntent();
                QuestionbankFragment fragment = QuestionbankFragment.newInstance(0, title);
                replaceFragment(fragment);
            } else if (frgId == R.id.lc) {
                String title = getTitleFromIntent();
                VideoBankFragment fragment = VideoBankFragment.newInstance(0, title);
                replaceFragment(fragment);
            } else {
                String title = getTitleFromIntent();
                WeeklyQuizFragment fragment = WeeklyQuizFragment.newInstance(0, title);
                replaceFragment(fragment);
            }
            return true;
        });
    }


    private String getTitleFromIntent() {
        return getIntent().getStringExtra("specialityPgName");
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_slideshow, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

}