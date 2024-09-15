package com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.tablayouts.swgt;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.medical.my_medicos.activities.pg.activites.internalfragments.Preprationindexing.adapter.Swgt.Swgtpastadapter;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapter;
import com.medical.my_medicos.activities.pg.adapters.WeeklyQuizAdapterSwgt;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.pg.model.QuizPGExam;
import com.medical.my_medicos.activities.pg.model.Swgtmodel;

import java.util.ArrayList;
import java.util.List;

public class PreprationIndexingSwgtPast extends Fragment {
    private Swgtpastadapter quizAdapter;
    private ArrayList<QuizPGExam> quizpg = new ArrayList<>();
    private String speciality;

    private static final String ARG_SPECIALITY = "speciality";

    public PreprationIndexingSwgtPast() {
        // Required empty public constructor
    }

    public static PreprationIndexingSwgtPast newInstance(String speciality) {
        PreprationIndexingSwgtPast fragment = new PreprationIndexingSwgtPast();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prepration_indexing_swgt_past, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        quizAdapter = new Swgtpastadapter(getContext(), quizpg);
        recyclerView.setAdapter(quizAdapter);

        getQuestions(speciality);
        return view;
    }

    void getQuestions(String title1) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<String> subcollectionIds = new ArrayList<>();

        if (user != null) {
            String userId = user.getPhoneNumber();
            CollectionReference quizResultsCollection = db.collection("QuizResults").document(userId).collection("Weekley");

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
         Timestamp now = Timestamp.now();

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String quizTitle = document.getString("title");
                    String speciality = document.getString("speciality");
                    Timestamp to = document.getTimestamp("to");
                    String id = document.getString("qid");
                    boolean index=document.contains("index");
                    if (index==true) {
                        String index1 = document.getString("index");
                        boolean paid1=document.contains("type");
                        if (paid1==true) {


                            boolean paid =false;
                            Timestamp from = document.getTimestamp("from");

                            if (now.compareTo(to) > 0 && (title1.isEmpty() || speciality.equals(title1))) {
                                int r = speciality.compareTo(title1);
                                if (r == 0) {
                                    QuizPGExam quiz = new QuizPGExam(quizTitle, speciality, to, id, paid, index1);
                                    quizpg.add(quiz);
                                }
                                quizAdapter.notifyDataSetChanged();


                            }
                        }
                    }
                }



            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }
}
