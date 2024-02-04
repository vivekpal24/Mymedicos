package com.medical.my_medicos.activities.job;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.job.fragments.LocumFragment;
import com.medical.my_medicos.activities.job.fragments.RegularFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

public class JobsActivity2 extends AppCompatActivity {
    String Title1;
    TextView OK;

    private ViewPager2 pagerjobs, viewpagerjobs;
    private TabLayout tabLayoutjobs;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs2);

        Toolbar toolbar = findViewById(R.id.jobstoolbar);
        OK = findViewById(R.id.okButton);
        OK.setBackgroundColor(Color.GRAY);
        Spinner specialitySpinner = findViewById(R.id.statespeciality);

        ArrayAdapter<CharSequence> specialityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.indian_cities,
                android.R.layout.simple_spinner_item
        );
        specialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialitySpinner.setAdapter(specialityAdapter);

        specialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedSpeciality = parentView.getItemAtPosition(position).toString();

                if (selectedSpeciality.equals("Select Speciality")) {
                    OK.setClickable(false);
                    OK.setBackgroundColor(Color.GRAY);
                } else {
                    OK.setClickable(true);
                    OK.setBackgroundColor(Color.BLUE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if no speciality is selected
            }
        });

        // Set the support action bar
        setSupportActionBar(toolbar);

        // Set the navigation icon and listener
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.arrowbackforappbar);
        }// Set the toolbar as the ActionBar

        // Get the title from the intent
        Intent intent = getIntent();
        if (intent != null) {
            Title1 = intent.getStringExtra("Title");
        }

        if (Title1 != null) {
            toolbar.setTitle(Title1); // Set the title now that you have it
        }

        viewpagerjobs = findViewById(R.id.view_pager_jobs);
        viewpagerjobs.setAdapter(new ViewPagerAdapterJobs(this, Title1)); // Pass Title1 to the adapter

        pagerjobs = findViewById(R.id.view_pager_jobs);
        tabLayoutjobs = findViewById(R.id.tablayout);
        new TabLayoutMediator(tabLayoutjobs, viewpagerjobs, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Regular");
                    break;
                case 1:
                    tab.setText("Locum");
                    break;
            }
        }).attach();
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected speciality
                String selectedSpeciality = specialitySpinner.getSelectedItem().toString();

                // Start the next activity with the selected speciality as an intent extra
                Intent nextActivityIntent = new Intent(JobsActivity2.this, JobsSearchActivity.class); // Replace NextActivity with the actual next activity
                nextActivityIntent.putExtra("selectedSpeciality", selectedSpeciality);
                nextActivityIntent.putExtra("Title", Title1);
                startActivity(nextActivityIntent);
            }
        });
    }

    class ViewPagerAdapterJobs extends FragmentStateAdapter {
        private String title;

        public ViewPagerAdapterJobs(@NonNull FragmentActivity fragmentActivity, String title) {
            super(fragmentActivity);
            this.title = title; // Store the title in the adapter
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    RegularFragment regularFragment = new RegularFragment();
                    Bundle args = new Bundle();
                    args.putString("Title", title); // Pass Title1 to RegularFragment
                    regularFragment.setArguments(args);
                    return regularFragment;
                case 1:
                    LocumFragment locumFragment = new LocumFragment();
                    Bundle argsLocum = new Bundle();
                    argsLocum.putString("Title", title); // Pass Title1 to LocumFragment
                    locumFragment.setArguments(argsLocum);
                    return locumFragment;
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
