package com.example.my_medicos;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Jobregularfragment extends Fragment {

    private List<jobitem1> jobList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter6 adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_jobregularfragment, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewRegular);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter6(getActivity(), jobList);
        recyclerView.setAdapter(adapter);
        jobList.clear();

        // Fetch data from Firestore and update jobList
        db.collection("JOB")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> dataMap = document.getData();
                                String speciality = "";

                                String Organiser = (String) dataMap.get("JOB Organiser");
                                String Location = (String) dataMap.get("Location");
                                String date = (String) dataMap.get("date");
                                String user = (String) dataMap.get("User");
                                String type=((String) dataMap.get("Job type"));
                                String Title=((String) dataMap.get("JOB Title"));

                                Log.d("abcd",type);


                                int r=type.compareTo("Regular");
                                Log.d("abcdef", String.valueOf(r));
                                if(r==0) {

                                    jobitem1 c = new jobitem1(speciality, Organiser, Location, date, user,Title);
                                    jobList.add(c);
                                }
                            }
                            // Notify the adapter that the data has changed
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return rootView;
    }
}
