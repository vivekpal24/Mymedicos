package com.example.my_medicos.activities.job;

import static com.example.my_medicos.list.subSpecialitiesData.subspecialities;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.my_medicos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
public class PostJobActivity extends AppCompatActivity {
    EditText title,Organiser,salary,jobposition,description,Opening,duration,hospital;
    public FirebaseDatabase db = FirebaseDatabase.getInstance();
    String selectedMode;
    String selectedMode2;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String current = user.getPhoneNumber();
    private Spinner subspecialitySpinner;
    private Spinner specialitySpinner;
    String subspecialities1;
    private Button btnDatePicker;
    private TextView tvDate;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private ArrayAdapter<CharSequence> specialityAdapter;
    private ArrayAdapter<CharSequence> subspecialityAdapter;
    FirebaseFirestore dc = FirebaseFirestore.getInstance();
    public DatabaseReference cmeref = db.getReference().child("CME's");
    String speciality;
    Button post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);
        btnDatePicker = findViewById(R.id.btnDatePicker);
        tvDate = findViewById(R.id.tvDate);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        title=findViewById(R.id.Job_title);
        Organiser=findViewById(R.id.Job_organiser);
        Organiser.setEnabled(false);
        Organiser.setTextColor(Color.parseColor("#80000000"));
        Organiser.setBackgroundResource(R.drawable.rounded_edittext_background);
        Opening=findViewById(R.id.opening);
        salary=findViewById(R.id.Salary);
        jobposition=findViewById(R.id.Job_post);
        description=findViewById(R.id.Job_desc);
        duration=findViewById(R.id.job_duration);
        hospital=findViewById(R.id.Job_hopital);

        dc.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> dataMap = document.getData();
                        String field3 = ((String) dataMap.get("Phone Number"));
                        boolean areEqualIgnoreCase = current.equalsIgnoreCase(field3);
                        Log.d("vivek", String.valueOf(areEqualIgnoreCase));
                        int r=current.compareTo(field3);
                        if (r==0){
                            String field4 = ((String) dataMap.get("Name"));
                            Log.d("veefe",field4);
                            Organiser.setText(field4);
                        }
                    }
                } else {
                    // Handle the error
                    Toast.makeText(PostJobActivity.this, "Error fetching data from Firebase Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });


        specialitySpinner = findViewById(R.id.Job_Speciality);
        subspecialitySpinner = findViewById(R.id.Job_subspeciality);

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
                    subspecialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            subspecialities1 = subspecialitySpinner.getSelectedItem().toString();
                            speciality=specialitySpinner.getSelectedItem().toString();

                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
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
        Spinner  spinner2= (Spinner) findViewById(R.id.city);
        ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(this,
                R.array.indian_cities, android.R.layout.simple_spinner_item);
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(myadapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedMode = spinner2.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Spinner modeSpinner = findViewById(R.id.job);
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(this,
                R.array.Job_type, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);


        Spinner cmemodeSpinner = findViewById(R.id.job);
        cmemodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedMode2 = cmemodeSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        post=findViewById(R.id.post1);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    post();
                }
            }
        });
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String selectedDate = dateFormat.format(calendar.getTime());
                        tvDate.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void post(){

        String Title = title.getText().toString().trim();
        String organiser = Organiser.getText().toString().trim();
        String Salary = salary.getText().toString().trim();
        String opening = Opening.getText().toString().trim();
        String Position = jobposition.getText().toString().trim();
        String Desciption = description.getText().toString().trim();
        String Duration = duration.getText().toString().trim();

        if (TextUtils.isEmpty(Title)) {
            title.setError("Title Required");
            return;
        }
        if (TextUtils.isEmpty(organiser)) {
            Organiser.setError("Organizer Required");
            return;
        }
        if (TextUtils.isEmpty(Salary)) {
            salary.setError("Salary Required");
            return;
        }
        if (TextUtils.isEmpty(Position)) {
            jobposition.setError("Position Required");
            return;
        }

        String selectedDate = tvDate.getText().toString();
        Log.d("viveknew",speciality);
        Log.d("viveknew",speciality);
        String Hospital=hospital.getText().toString().trim();

        HashMap<String, Object> usermap = new HashMap<>();
        usermap.put("JOB Title", Title);
        usermap.put("JOB Organiser", organiser);
        usermap.put("Job Salary", Salary);
        usermap.put("JOB Description", Desciption);
        usermap.put("Job Duration", Duration);
        usermap.put("JOB Opening", opening);
        usermap.put("User", current);
        usermap.put("Location",selectedMode);
        usermap.put("Job type",selectedMode2);
        usermap.put("Speciality", speciality);
        usermap.put("SubSpeciality",subspecialities1);
        usermap.put("date", selectedDate);
        usermap.put("Hospital",Hospital);

        cmeref.push().setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful()) {

                    if (task.isSuccessful()) {
                        String generatedDocId = cmeref.push().getKey();
                        usermap.put("documentId", generatedDocId);
                        dc.collection("JOB").document(generatedDocId).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PostJobActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PostJobActivity.this, "Task Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(PostJobActivity.this, "Task Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}