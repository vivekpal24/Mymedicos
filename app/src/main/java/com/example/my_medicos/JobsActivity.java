package com.example.my_medicos;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JobsActivity extends AppCompatActivity {


    RecyclerView recyclerView1;
    MyAdapter6 adapter1;

    FloatingActionButton floatingActionButton;

    private ViewPager2 pager;
    private TabLayout tabLayout;
    RecyclerView recyclerView2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String speciality,Organiser,Location;

    Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        toolbar = findViewById(R.id.jobstoolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(JobsActivity.this, PostJobActivity.class);
                startActivity(i);
            }
        });
        pager = findViewById(R.id.jobViewpager);
        tabLayout = findViewById(R.id.jobtablayout);

        Spinner spinner = (Spinner) findViewById(R.id.speciality);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.speciality, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Spinner  spinner2= (Spinner) findViewById(R.id.city);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(this,
                R.array.indian_cities, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner2.setAdapter(myadapter);



        pager.setAdapter(new ViewPagerAdapter(this));

        new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0: tab.setText("Regular"); break;
                    case 1: tab.setText("Locum"); break;
                }
            }
        }).attach();
        recyclerView1 = findViewById(R.id.recyclerview5);
        List<jobitem1> joblist = new ArrayList<jobitem1>();


        FirebaseFirestore dc = FirebaseFirestore.getInstance();
        //......
        dc.collection("JOB")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task ) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Map<String, Object> dataMap = document.getData();
                                String speciality = ((String) dataMap.get("JOB Speciality"));
                                String Organiser =((String) dataMap.get("JOB Organiser"));
                                String Location =((String) dataMap.get("JOB Location"));

                                jobitem1 c = new jobitem1(speciality, Organiser, Location);
                                joblist.add(c);
                                Log.d("speciality2", speciality);
                                Log.d("speciality2", Organiser);
                                Log.d("speciality2", Location);
//
//                                // Pass the joblist to the adapter
//                                Log.d("speciality2", speciality);
                                recyclerView1.setLayoutManager(new LinearLayoutManager(getApplication(),LinearLayoutManager.VERTICAL, false));
                                recyclerView1.setAdapter( new MyAdapter6(getApplication(),joblist));
                                Log.d("speciality2", speciality);



                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



    //......
}


    class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }



        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0: return new TodaysFragment();
                case 1: return new UpcomingFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.chat) {
            Toast.makeText(this, "Chat clicked", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent i = new Intent(JobsActivity.this, HomeActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}