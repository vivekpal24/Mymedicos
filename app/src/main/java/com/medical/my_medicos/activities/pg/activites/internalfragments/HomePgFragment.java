package com.medical.my_medicos.activities.pg.activites.internalfragments;

import static android.content.Context.MODE_PRIVATE;
import static androidx.fragment.app.FragmentManager.TAG;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.medical.my_medicos.activities.home.HomeActivity;
import com.medical.my_medicos.activities.job.JobsApplyActivity;
import com.medical.my_medicos.activities.job.fragments.TermsandConditionDialogueFragment;
import com.medical.my_medicos.activities.login.bottom_controls.TermsandConditionsActivity;
import com.medical.my_medicos.activities.pg.activites.PgprepActivity;
import com.medical.my_medicos.activities.pg.activites.extras.PreparationCategoryDisplayActivity;
import com.medical.my_medicos.activities.pg.activites.extras.PreparationCategoryMaterialDisplayActivity;
import com.medical.my_medicos.activities.pg.activites.extras.RecetUpdatesNoticeActivity;
import com.medical.my_medicos.activities.pg.activites.extras.TermsandConditionsDialogueFragmentPg;
import com.medical.my_medicos.activities.pg.activites.internalfragments.intwernaladapters.ExamQuizAdapter;
import com.medical.my_medicos.activities.pg.adapters.PerDayPGAdapter;
import com.medical.my_medicos.activities.pg.adapters.QuestionBankPGAdapter;
import com.medical.my_medicos.activities.pg.model.PerDayPG;
import com.medical.my_medicos.activities.pg.model.QuestionPG;
import com.medical.my_medicos.activities.pg.model.QuizPG;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.medical.my_medicos.activities.utils.UpdatingScreen;
import com.medical.my_medicos.databinding.FragmentHomePgBinding;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class HomePgFragment extends Fragment {
    FragmentHomePgBinding binding;
    ArrayList<PerDayPG> dailyquestionspg;
    SwipeRefreshLayout swipeRefreshLayout;
    String quiztiddaya;
    LinearLayout nocardp;
    FirebaseUser currentUser;
    QuestionBankPGAdapter questionsAdapter;
    ArrayList<QuestionPG> questionsforpg;
    private Handler handlerprepration;
    private ExamQuizAdapter quizAdapter;
    CardView gotoupdatesofpg;
    private ArrayList<QuizPG> quizpg;
    String title1;

    TextView practivemcq,material;

    private TermsandConditionsDialogueFragmentPg dialog;

    private final int AUTO_SCROLL_DELAY = 3000;

    int i = 0;


    public static HomePgFragment newInstance() {
        HomePgFragment fragment = new HomePgFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomePgBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        showFirstTimePopup();

        Bundle args = getArguments();
        if (args != null) {

            quizpg = new ArrayList<>();
            quizAdapter = new ExamQuizAdapter(requireContext(), quizpg);

            RecyclerView recyclerViewVideos = binding.specialexam;
            LinearLayoutManager layoutManagerVideos = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            recyclerViewVideos.setLayoutManager(layoutManagerVideos);
            recyclerViewVideos.setAdapter(quizAdapter);

            getPaidExam(title1);
        } else {
            Log.e("ERROR", "Arguments are null in WeeklyQuizFragment");
        }

        gotoupdatesofpg = view.findViewById(R.id.gotoupdatesofpg);
        gotoupdatesofpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), RecetUpdatesNoticeActivity.class);
                startActivity(i);
            }
        });

        practivemcq = view.findViewById(R.id.practivemcq);
        practivemcq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar today = Calendar.getInstance();

                // Set the target date to April 1st, 2024
                Calendar targetDate = Calendar.getInstance();
                targetDate.set(2024, Calendar.APRIL, 8); // Note: Months are 0-based in Calendar

                Intent i;
                if (today.before(targetDate)) {
                    i = new Intent(getActivity(), UpdatingScreen.class);
                    Toast.makeText(getActivity(), "This feature will be available soon!", Toast.LENGTH_SHORT).show();
                } else {
                    i = new Intent(getActivity(), PreparationCategoryDisplayActivity.class);
                }
                startActivity(i);
            }
        });


        material = view.findViewById(R.id.material);
        material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PreparationCategoryMaterialDisplayActivity.class);
                startActivity(i);
            }
        });

//        TextView promoteBtn = view.findViewById(R.id.promotebtn);
//        promoteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Customize the content to share
//                String appLink = "https://play.google.com/store/apps/details?id=com.medical.my_medicos";
//                String message = "Check out our medical app!\nDownload now: " + appLink;
//
//                // Create an Intent with ACTION_SEND
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, message);
//
//                // Check if there's an app to handle the Intent
//                if (shareIntent.resolveActivity(requireContext().getPackageManager()) != null) {
//                    startActivity(Intent.createChooser(shareIntent, "Share via"));
//                }
//            }
//        });

//        viewFlipperPrepration = view.findViewById(R.id.viewFlipperPrepration);
//        dotsLayoutprepration = view.findViewById(R.id.dotsLayoutPrepration);
        handlerprepration = new Handler();

//        addDotsPrepration();

//        handlerprepration.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);

        return view;
    }


    private void showFirstTimePopup() {
        SharedPreferences prefs = getActivity().getSharedPreferences("PgPrepPrefs", MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean("isFirstLaunch", true);

            if (isFirstLaunch) {
                View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.custompopupforpg, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setView(popupView);

                Button agreeButton = popupView.findViewById(R.id.agreepg);
                Button disagreeButton = popupView.findViewById(R.id.visit);
                ImageView closeOpt = popupView.findViewById(R.id.closebtndialogue);

                AlertDialog dialog = builder.create();

                agreeButton.setOnClickListener(v -> {
                    dialog.dismiss();
                });

                disagreeButton.setOnClickListener(v -> {
                    dialog.dismiss();
                    Intent intent = new Intent(requireContext(), TermsandConditionsActivity.class);
                    Toast.makeText(requireContext(), "Redirecting..", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                });

                closeOpt.setOnClickListener(v -> {
                    dialog.dismiss();
                    Intent intent = new Intent(requireContext(), HomeActivity.class);
                    Toast.makeText(requireContext(), "Returning back...", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    dialog.dismiss();
                });

                dialog.show();
                prefs.edit().putBoolean("isFirstLaunch", false).apply();
            }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getPhoneNumber();
        }

        swipeRefreshLayout = binding.getRoot().findViewById(R.id.swipeRefreshLayoutPg);
        swipeRefreshLayout.setOnRefreshListener(this::refreshContent);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = currentUser.getPhoneNumber();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                Map<String, Object> dataMap = document.getData();
                                String field1 = (String) dataMap.get("Phone Number");

                                if (field1 != null && currentUser.getPhoneNumber() != null) {
                                    int a = field1.compareTo(userId);
                                    Log.d("Issue with the userID", String.valueOf(a));

                                    if (a == 0) {
                                        Log.d("Can't get it", String.valueOf(a));
                                        quiztiddaya = ((String) dataMap.get("QuizToday"));
                                        break;
                                    } else {
                                        quiztiddaya = null;
                                    }
                                }
                            }
                        } else {
                            Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        initSliderPg();
        initQuestionsBanks();
    }


    private void initSliderPg() {
        getRecentPgSlider();
    }

    void getRecentPgSlider() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_PG_SLIDER_URL, response -> {
            try {
                JSONArray pgsliderArray = new JSONArray(response);
                for (int i = 0; i < pgsliderArray.length(); i++) {
                    JSONObject childObj = pgsliderArray.getJSONObject(i);
                    binding.carouselpghome.addData(
                            new CarouselItem(
                                    childObj.getString("url"),
                                    childObj.getString("action")
                            )
                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // Handle error if needed
        });
        queue.add(request);
    }

    // For the Suggested Exam
    void getPaidExam(String title) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<String> subcollectionIds = new ArrayList<>();

        if (user != null) {
            String userId = user.getUid();

            CollectionReference quizResultsCollection = db.collection("QuizResults").document(userId).collection("Exam");

            quizResultsCollection.get()
                    .addOnCompleteListener(subcollectionTask -> {
                        if (subcollectionTask.isSuccessful()) {
                            for (QueryDocumentSnapshot subdocument : subcollectionTask.getResult()) {
                                String subcollectionId = subdocument.getId();
                                subcollectionIds.add(subcollectionId);
                                Log.d("Subcollection ID", subcollectionId);
                            }

                            for (String id : subcollectionIds) {
                                Log.d("All Subcollection IDs", id);
                            }
                        } else {
                            Log.e("Subcollection ID", "Error fetching subcollections", subcollectionTask.getException());
                        }
                    });
        }

        if (title == null || title.isEmpty()) {
            title = "home";
        }

        CollectionReference quizzCollection = db.collection("PGupload").document("Weekley").collection("Quiz");
        Query query = quizzCollection;
        String finalTitle = title;

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();

                        if (!subcollectionIds.contains(id)) {
                            String quizTitle = document.getString("title");
                            String speciality = document.getString("speciality");
                            Timestamp To = document.getTimestamp("to");

                            if (finalTitle.isEmpty() || finalTitle.equals("Home")) {
                                int r = speciality.compareTo(finalTitle);
                                if (r == 0) {
                                    QuizPG quizday = new QuizPG(quizTitle, title1, To);
                                    quizpg.add(quizday);
                                }
                            } else {
                                int r = speciality.compareTo(finalTitle);
                                if (r == 0) {
                                    QuizPG quizday = new QuizPG(quizTitle, finalTitle, To);
                                    quizpg.add(quizday);
                                }
                            }
                        }
                    }
                    quizAdapter.notifyDataSetChanged();
                } else {
                    Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

    void initQuestionsBanks() {
        questionsforpg = new ArrayList<>();
        questionsAdapter = new QuestionBankPGAdapter(getActivity(), questionsforpg);

        getRecentQuestions();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.QB.setLayoutManager(layoutManager);
        binding.QB.setAdapter(questionsAdapter);
    }

    void getRecentQuestions() {
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String url = ConstantsDashboard.GET_PG_QUESTIONBANK_URL_HOME;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject childObj = array.getJSONObject(i);
                        QuestionPG questionbankItem = new QuestionPG(
                                childObj.getString("Title"),
                                childObj.getString("Description"),
                                childObj.getString("Time"),
                                childObj.getString("file")
                        );
                        questionsforpg.add(questionbankItem);
                    }
                    questionsAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        queue.add(request);
    }

    private void refreshContent() {
        swipeRefreshLayout.setRefreshing(false);
    }

}
