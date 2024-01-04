package com.medical.my_medicos.activities.publications.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medical.my_medicos.R;
import com.medical.my_medicos.activities.pg.activites.internalfragments.HomePgFragment;
import com.medical.my_medicos.activities.publications.activity.insiders.CategoryPublicationInsiderActivity;
import com.medical.my_medicos.activities.publications.adapters.CategoryAdapter;
import com.medical.my_medicos.activities.publications.adapters.ProductAdapter;
import com.medical.my_medicos.activities.publications.model.Category;
import com.medical.my_medicos.activities.publications.model.Product;
import com.medical.my_medicos.activities.publications.utils.Constants;
import com.medical.my_medicos.activities.utils.ConstantsDashboard;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.medical.my_medicos.databinding.ActivityPublicationBinding;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PublicationActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCREENSHOT = 1001;

    ActivityPublicationBinding binding;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;
    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        binding = ActivityPublicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent = new Intent(PublicationActivity.this, SearchPublicationActivity.class);
                intent.putExtra("query", text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
            }
        });

        initCategories();
        initProducts();
        initSlider();

        binding.totheccart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PublicationActivity.this, UserContentActivity.class));
            }
        });


        ImageView backToHomeImageView = findViewById(R.id.backtothehomefrompublication);
        backToHomeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void initSlider() {
        getRecentOffers();
    }

    void initCategories() {
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categories);

        getCategories();

        // Initialize the RecyclerView with a GridLayoutManager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.categoriesList.setLayoutManager(layoutManager);
        binding.categoriesList.setAdapter(categoryAdapter);
    }

    void updateCategoryGridLayout() {
        // Update the GridLayoutManager based on the number of words in the category name
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.categoriesList.setLayoutManager(layoutManager);
        binding.categoriesList.setAdapter(categoryAdapter);
    }

    void getCategories() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, ConstantsDashboard.GET_SPECIALITY, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err", response);
                    JSONObject mainObj = new JSONObject(response);
                    if (mainObj.getString("status").equals("success")) {
                        JSONArray categoriesArray = mainObj.getJSONArray("data");

                        for (int i = 0; i < categoriesArray.length(); i++) {
                            JSONObject object = categoriesArray.getJSONObject(i);
                            int priority = object.getInt("priority");
                            if (priority >= 1 && priority <= 3) {
                                Category category = new Category(
                                        object.getString("id"),
                                        priority
                                );
                                categories.add(category);
                                Log.e("Something went wrong..", String.valueOf(priority));
                            }
                        }

                        categoryAdapter.notifyDataSetChanged();

                        binding.categoriesList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                                View child = rv.findChildViewUnder(e.getX(), e.getY());
                                int position = rv.getChildAdapterPosition(child);

                                if (position != RecyclerView.NO_POSITION) {
                                    if (position == categories.size() - 1 && categories.get(position).getPriority() == -1) {
                                        Intent intent = new Intent(PublicationActivity.this, CategoryPublicationInsiderActivity.class);
                                        startActivity(intent);
                                    } else {
                                    }
                                }
                                return false;
                            }

                            @Override
                            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
                        });
                        categoryAdapter.notifyDataSetChanged();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors here
            }
        });

        queue.add(request);
    }


    void initProducts() {
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);

        getRecentProducts();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }
    void getRecentProducts() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ConstantsDashboard.GET_SPECIALITY_ALL_PRODUCT;
        @SuppressLint("NotifyDataSetChanged") StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray productsArray = object.getJSONArray("data");
                    for(int i =0; i< productsArray.length(); i++) {
                        JSONObject childObj = productsArray.getJSONObject(i);
                        JSONObject productObj = childObj.getJSONObject("data");

                        String documentId = childObj.getString("id");

                        Product product = new Product(
                                productObj.getString("Title"),
                                productObj.getString("thumbnail"),
                                productObj.getString("Author"),
                                productObj.getDouble("Price"),
                                productObj.getString("Type"),
                                productObj.getString("Category"),
                                documentId,
                                productObj.getString("Subject")
                        );

                        products.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { });

        queue.add(request);
    }


    void getRecentOffers() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getString("status").equals("success")) {
                    JSONArray offerArray = object.getJSONArray("news_infos");
                    for(int i =0; i < offerArray.length(); i++) {
                        JSONObject childObj =  offerArray.getJSONObject(i);
                        binding.carousel.addData(
                                new CarouselItem(
                                        Constants.NEWS_IMAGE_URL + childObj.getString("image"),
                                        childObj.getString("title")
                                )
                        );
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {});
        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}