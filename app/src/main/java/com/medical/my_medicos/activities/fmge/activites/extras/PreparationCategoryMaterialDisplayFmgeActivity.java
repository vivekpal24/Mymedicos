package com.medical.my_medicos.activities.fmge.activites.extras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.fmge.activites.internalfragments.PreparationSubCategoryForMaterialFmgeActivity;
import com.medical.my_medicos.activities.pg.activites.internalfragments.HomePgFragment;
import com.medical.my_medicos.activities.pg.activites.internalfragments.PreparationSubCategoryActivity;
import com.medical.my_medicos.activities.pg.activites.internalfragments.PreparationSubCategoryForMaterialActivity;
import com.medical.my_medicos.databinding.ActivityPreparationCategoryDisplayBinding;
import com.medical.my_medicos.databinding.ActivityPreparationCategoryMaterialDisplayBinding;
import com.medical.my_medicos.databinding.ActivityPreparationCategoryMaterialDisplayFmgeBinding;

public class PreparationCategoryMaterialDisplayFmgeActivity extends AppCompatActivity {

    ActivityPreparationCategoryMaterialDisplayFmgeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreparationCategoryMaterialDisplayFmgeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView backToHomeImageView = findViewById(R.id.backtothehomefromprepcategory);
        backToHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.phase1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubCategoryActivity(binding.titlephase1.getText().toString());
            }
        });

        findViewById(R.id.phase2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubCategoryActivity(binding.titlephase2.getText().toString());
            }
        });

        findViewById(R.id.phase3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSubCategoryActivity(binding.titlephase3.getText().toString());
            }
        });

        configureWindow();
    }

    private void configureWindow() {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.backgroundcolor));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }


    private void startSubCategoryActivity(String categoryTitle) {
        Intent intent = new Intent(this, PreparationSubCategoryForMaterialFmgeActivity.class);
        intent.putExtra("CATEGORY_TITLE", categoryTitle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout_pg);
        if (currentFragment instanceof HomePgFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
