package com.medical.my_medicos.activities.pg.activites.internalfragments.insiderfragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.medical.my_medicos.databinding.FragmentUpcomingNeetBinding;
import com.medical.my_medicos.activities.pg.activites.internalfragments.intwernaladapters.ExamUpcomingAdapter;
import com.medical.my_medicos.activities.pg.model.QuizPGExam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UpcomingNeetFragment extends Fragment {
    private FragmentUpcomingNeetBinding binding;
    private ArrayList<QuizPGExam> upcomingQuizzes;
    private ExamUpcomingAdapter upcomingAdapter;
    private FirebaseUser currentUser;

    public static UpcomingNeetFragment newInstance() {
        UpcomingNeetFragment fragment = new UpcomingNeetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUpcomingNeetBinding.inflate(inflater, container, false);

        upcomingQuizzes = new ArrayList<>();
        upcomingAdapter = new ExamUpcomingAdapter(requireContext(), upcomingQuizzes);

        RecyclerView recyclerView = binding.recyclerViewUpcomingExams;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(upcomingAdapter);

        fetchUpcomingQuizzes("Exam");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    void fetchUpcomingQuizzes(String title) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Timestamp now = Timestamp.now();

        if (user != null) {
            String userId = user.getPhoneNumber();
            CollectionReference quizCollection = db.collection("PGupload").document("Weekley").collection("Quiz");

            Query query = quizCollection.whereGreaterThanOrEqualTo("from", now);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    upcomingQuizzes.clear();
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


                                boolean paid = document.getBoolean("type");
                                Timestamp from = document.getTimestamp("from");

                                if (now.compareTo(from) < 0 && (title.isEmpty() || speciality.equals(title))) {
                                    QuizPGExam quiz = new QuizPGExam(quizTitle, speciality, to, id, paid, index1);
                                    upcomingQuizzes.add(quiz);
                                }
                            }
                        }
                    }
                    // Sort the upcomingQuizzes list here by the 'from' date
                    Collections.sort(upcomingQuizzes, Comparator.comparing(QuizPGExam::getTo));
                    upcomingAdapter.notifyDataSetChanged();
                    updateUI();
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            });
        } else {
            Log.e(TAG, "User is not logged in.");
        }
    }


    private void updateUI() {
        if (upcomingQuizzes.isEmpty()) {
            binding.recyclerViewUpcomingExams.setVisibility(View.GONE);
            binding.nocardpg.setVisibility(View.VISIBLE);
            Log.d(TAG, "No upcoming quizzes found. Displaying no card.");
        } else {
            binding.recyclerViewUpcomingExams.setVisibility(View.VISIBLE);
            binding.nocardpg.setVisibility(View.GONE);
        }
    }
}
