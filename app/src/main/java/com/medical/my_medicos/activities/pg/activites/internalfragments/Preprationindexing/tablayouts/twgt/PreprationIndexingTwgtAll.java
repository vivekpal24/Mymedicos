package com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.tablayouts.twgt;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.FilterViewModel;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapter;
import com.medical.my_medicos.activities.pg.model.QuizPG;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreprationIndexingTwgtAll#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreprationIndexingTwgtAll extends Fragment {
    private WeeklyQuizAdapter quizAdapter;
    private ArrayList<QuizPG> quizpg = new ArrayList<>();
    private String speciality;
    private FilterViewModel filterViewModel;

    // Parameter key
    private static final String ARG_SPECIALITY = "speciality";

    public PreprationIndexingTwgtAll() {
        // Required empty public constructor
    }

    public static PreprationIndexingTwgtAll newInstance(String speciality) {
        PreprationIndexingTwgtAll fragment = new PreprationIndexingTwgtAll();
        Bundle args = new Bundle();
        args.putString(ARG_SPECIALITY, speciality);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            speciality = getArguments().getString(ARG_SPECIALITY);
        }
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prepration_indexing_twgt_all, container, false);
        

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        quizAdapter = new WeeklyQuizAdapter(getContext(), quizpg);
        recyclerView.setAdapter(quizAdapter);
        filterViewModel.getSelectedSubspeciality().observe(getViewLifecycleOwner(), speciality -> {
            getQuestions(filterViewModel.getSelectedSubspeciality().getValue());
        });

        filterViewModel.getSelectedSubspeciality().observe(getViewLifecycleOwner(), subspeciality -> {
            getQuestions(subspeciality);
        });

        // Set initial filters if they are null
        if (filterViewModel.getSelectedSubspeciality().getValue() == null) {
            filterViewModel.setSelectedSubspeciality(speciality);
        }
        if (filterViewModel.getSelectedSubspeciality().getValue() == null) {
            filterViewModel.setSelectedSubspeciality("All (Default)");
        }

        getQuestions(speciality);

        return view;
    }

    void getQuestions(String subspeciality) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<String> subcollectionIds = new ArrayList<>();

        if (user != null) {
            String userId = user.getPhoneNumber();
            CollectionReference quizResultsCollection = db.collection("QuizResultsPGPrep").document(userId).collection("Weekley");

            quizResultsCollection.get()
                    .addOnCompleteListener(subcollectionTask -> {
                        if (subcollectionTask.isSuccessful()) {
                            for (QueryDocumentSnapshot subdocument : subcollectionTask.getResult()) {
                                String subcollectionId = subdocument.getId();
                                subcollectionIds.add(subcollectionId);
                                Log.d("Subcollection ID", subcollectionId);
                            }
                        } else {
                            Log.e("Subcollection ID", "Error fetching subcollections", subcollectionTask.getException());
                        }
                    });
        }

        CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");
        Query query = quizzCollection.orderBy("from", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                quizpg.clear();  // Clear the list before adding new items
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String id = document.getId();

                    if (!subcollectionIds.contains(id)) {
                        String title = document.getString("title");
                        String quizSpeciality = document.getString("speciality");
                        Timestamp to = document.getTimestamp("to");
                        if (speciality.equals(quizSpeciality)) {
                            if ("All (Default)".equals(subspeciality) || subspeciality.equals(document.getString("subspeciality"))) {
                                QuizPG quizday = new QuizPG(title, quizSpeciality, to, id);
                                quizpg.add(quizday);
                            }
                        }
                    }
                }
                quizAdapter.notifyDataSetChanged();
            } else {
                Log.d("PreprationIndexingSwgtLive", "Error getting documents: ", task.getException());
            }
        });
    }
}
