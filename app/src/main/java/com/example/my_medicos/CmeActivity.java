package com.example.my_medicos;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CmeActivity extends AppCompatActivity {

    String field1;
    String  field2;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    RecyclerView recyclerView3;
    RecyclerView recyclerView2;

    private ViewPager2 pager;
    private TabLayout tabLayout;
    private Spinner specialitySpinner;

    private FirebaseFirestore db;

    private Spinner subspecialitySpinner;
    private ArrayAdapter<CharSequence> specialityAdapter;
    private ArrayAdapter<CharSequence> subspecialityAdapter;

    Toolbar toolbar;

    // Define subspecialities for each speciality
    private final String[][] subspecialities = subSpecialitiesData.subspecialities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cme);

        toolbar = findViewById(R.id.cmetoolbar);
        recyclerView = findViewById(R.id.cme_recyclerview1);
        setSupportActionBar(toolbar);

        DatabaseReference mbase;
        db = FirebaseFirestore.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CmeActivity.this, PostCmeActivity.class);
                startActivity(i);
            }
        });

        recyclerView = findViewById(R.id.cme_recyclerview1);
        List<cmeitem1> items = new ArrayList<>();
        items.add(new cmeitem1("John wick", "Dentist", R.drawable.img_2,"5"));
        items.add(new cmeitem1("Robert j", "Pediatrics", R.drawable.img_3,"5"));
        // Add more items here

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new MyAdapter2(this, items));

        recyclerView3 = findViewById(R.id.recyclerview3);
        List<cmeitem3> item = new ArrayList<>();
        //.....
        db.collection("CME")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {


                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> dataMap = document.getData();
                                field1 = (String) dataMap.get("CME Organiser");
                                field2 = ((String) dataMap.get("CME Place"));
                                Log.d(TAG,(String) dataMap.get("CME Organiser"));

                                cmeitem3 c = new cmeitem3("abc", "field", field2,field1);
                                Log.d("vivek",field1);
                                Log.d("vivek","hello");
                                item.add(c);


                                recyclerView3.setLayoutManager(new LinearLayoutManager(getApplication()));
                                recyclerView3.setAdapter(new MyAdapter3(getApplication(), item));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        //.....
        item.add(new cmeitem3("25th AUG", "1:30pm", "abcdefgh", "John wick"));
        item.add(new cmeitem3("26th AUG", "12:30pm", "anscncdc", "Robert k"));
        // Add more items here

        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView3.setAdapter(new MyAdapter3(this, item));

        recyclerView2 = findViewById(R.id.recyclerview2);
        List<cmeitem2> myitem = new ArrayList<cmeitem2>();
        //......
        db.collection("CME")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {


                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> dataMap = document.getData();
                                field1 = (String) dataMap.get("CME Organiser");
                                field2 = ((String) dataMap.get("CME Place"));
                                Log.d(TAG,(String) dataMap.get("CME Organiser"));

                                cmeitem2 c = new cmeitem2("abc", "field", field2);
                                Log.d("vivek",field1);
                                Log.d("vivek","hello");
                                myitem.add(c);


                                recyclerView2.setLayoutManager(new LinearLayoutManager(getApplication(), LinearLayoutManager.HORIZONTAL, false));
                                recyclerView2.setAdapter(new MyAdapter4(getApplication(), myitem));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        //......
        myitem.add(new cmeitem2("John wick", "ESI Hospital", "Peitiric"));
        myitem.add(new cmeitem2("Robert j", "Shushrta Hospital", "Peitiric"));
        // Add more items here

        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setAdapter(new MyAdapter4(this, myitem));

        pager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabLayout);
        specialitySpinner = findViewById(R.id.speciality);
        subspecialitySpinner = findViewById(R.id.subspeciality);

        specialityAdapter = ArrayAdapter.createFromResource(this,
                R.array.speciality, android.R.layout.simple_spinner_item);
        specialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialitySpinner.setAdapter(specialityAdapter);

        subspecialityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        subspecialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subspecialitySpinner.setAdapter(subspecialityAdapter);

        // Initially, hide the subspeciality spinner
        subspecialitySpinner.setVisibility(View.GONE);

        specialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // Check if the selected speciality has subspecialities
                int specialityIndex = specialitySpinner.getSelectedItemPosition();
                if (specialityIndex >= 0 && specialityIndex < subspecialities.length && subspecialities[specialityIndex].length > 0) {
                    String[] subspecialityArray = subspecialities[specialityIndex];
                    subspecialityAdapter.clear();
                    subspecialityAdapter.add("Select Subspeciality");
                    for (String subspeciality : subspecialityArray) {
                        subspecialityAdapter.add(subspeciality);
                    }
                    // Show the subspeciality spinner
                    subspecialitySpinner.setVisibility(View.VISIBLE);
                } else {
                    // Hide the subspeciality spinner
                    subspecialitySpinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        Spinner modeSpinner = findViewById(R.id.mode);
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(this,
                R.array.mode, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);

        pager.setAdapter(new ViewPagerAdapter(this));

        new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Todays");
                        break;
                    case 1:
                        tab.setText("Upcoming");
                        break;
                    case 2:
                        tab.setText("Past");
                        break;
                }
            }
        }).attach();
    }

    class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new TodaysFragment();
                case 1:
                    return new UpcomingFragment();
                case 2:
                    return new PastFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Toast.makeText(this, "Chat clicked", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent i = new Intent(CmeActivity.this, HomeActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
