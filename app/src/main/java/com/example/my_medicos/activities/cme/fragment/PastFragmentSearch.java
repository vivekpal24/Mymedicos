package com.example.my_medicos.activities.cme.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_medicos.R;
import com.example.my_medicos.adapter.cme.MyAdapter4;
import com.example.my_medicos.adapter.cme.items.cmeitem2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PastFragmentSearch extends Fragment {
    RecyclerView recyclerView2;
    String Speciality,SubSpeciality,Mode;
    public static PastFragmentSearch newInstance(String speciality, String subSpeciality, String mode) {
        PastFragmentSearch fragment = new PastFragmentSearch();
        Bundle args = new Bundle();
        args.putString("Speciality", speciality);
        args.putString("SubSpeciality", subSpeciality);
        args.putString("Mode", mode);
        fragment.setArguments(args);
        return fragment;
    }



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        recyclerView2 = view.findViewById(R.id.cme_recyclerview_past);
        List<cmeitem2> myitem = new ArrayList<cmeitem2>();
        if (getArguments() != null) {
            Speciality = getArguments().getString("Speciality");
            SubSpeciality = getArguments().getString("SubSpeciality");
            Mode = getArguments().getString("Mode");
        }

        //......
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Query query1=db.collection("CME").orderBy("Time", Query.Direction.DESCENDING);

            query1.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task ) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Map<String, Object> dataMap = document.getData();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    String Time = document.getString("Selected Time");
                                    String date =  document.getString("Selected Date");

                                    //
                                    LocalTime parsedTime = null;
                                    LocalDate parsedDate=null;
                                    try {
                                        // Parse the time string into a LocalTime object
                                        parsedTime = null;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            parsedTime = LocalTime.parse(Time, formatter);
                                            parsedDate = LocalDate.parse(date, formatter1);

                                        }

                                        // Display the parsed time
                                        System.out.println("Parsed Time: " + parsedTime);
                                    } catch (DateTimeParseException e) {
                                        // Handle parsing error, e.g., if the input string is in the wrong format
                                        System.err.println("Error parsing time: " + e.getMessage());

                                    }
                                    LocalTime currentTime = null;
                                    LocalDate currentDate = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        currentTime = LocalTime.now();
                                        currentDate = LocalDate.now();
                                    }


                                    int r = parsedTime.compareTo(currentTime);

                                    Log.d("vivek", String.valueOf(r));
                                    int r1 = parsedDate.compareTo(currentDate);
                                    if ((r1 <= 0)) {
                                        String field3 = ((String) dataMap.get("CME Title"));
                                        List<String> presenters = (List<String>) dataMap.get("CME Presenter");
                                        String field4=presenters.get(0);
                                        String field1 = (String) dataMap.get("CME Organiser");
                                        String field2;
                                        String field5=((String ) dataMap.get("User"));
                                        field2 = ((String) dataMap.get("Speciality"));
                                        String Date=((String) dataMap.get("Selected Date"));
                                        String time =((String) dataMap.get("Selected Time"));
                                        String documentid=((String) dataMap.get("documentId"));
                                        String end=((String) dataMap.get("endtime"));
                                        String subspeciality=((String) dataMap.get("SubSpeciality"));
                                        String mode=((String) dataMap.get("Mode"));
                                        cmeitem2 c = new cmeitem2(field1, field2, Date, field3, field4,5,time,field5,"PAST",documentid);
                                        int r4=Speciality.compareTo(field2);
                                        int r3=SubSpeciality.compareTo(subspeciality);
                                        int r2=Mode.compareTo(mode);
                                        if (end!=null) {
                                            if ((r2==0)&&(r3==0)&&(r4==0)) {
                                                myitem.add(c);

                                            }
                                        }



                                    } else if ((r < 0)&& (r1 == 0)){
                                        String field3 = ((String) dataMap.get("CME Title"));
                                        String field4 = ((String) dataMap.get("CME Presenter"));
                                        String field1 = (String) dataMap.get("CME Organiser");
                                        String field2;
                                        String time =((String) dataMap.get("Selected Time"));
                                        String field5=((String ) dataMap.get("User"));
                                        field2 = ((String) dataMap.get("Speciality"));
                                        String Date=((String) dataMap.get("Selected Date"));
                                        String documentid=((String) dataMap.get("documentId"));
                                        String end=((String) dataMap.get("endtime"));
                                        String subspeciality=((String) dataMap.get("SubSpeciality"));
                                        String mode=((String) dataMap.get("Mode"));
                                        cmeitem2 c = new cmeitem2(field1, field2, Date, field3, field4,5,time,field5,"PAST",documentid);
                                        int r4=Speciality.compareTo(field2);
                                        int r3=SubSpeciality.compareTo(subspeciality);
                                        int r2=Mode.compareTo(mode);
                                        if (end!=null) {
                                            if ((r2==0)&&(r3==0)&&(r4==0)) {
                                                myitem.add(c);

                                            }
                                        }


                                    }

                                }
                                recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                recyclerView2.setAdapter(new MyAdapter4(getContext(), myitem));

                            } else {
                                Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

        // Query and display Today's CME events in the RecyclerView
        // Customize your logic to query and display data for today's events.
        // Set the appropriate adapter for the RecyclerView.

        return view;
    }
}
