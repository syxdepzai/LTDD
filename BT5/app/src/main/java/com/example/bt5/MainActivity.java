package com.example.bt5;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcCate;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ RecyclerView
        rcCate = findViewById(R.id.rc_category);

        // Khởi tạo danh sách
        categoryList = new ArrayList<>();

        // Gọi API
        getCategoryFromAPI();
    }

    private void getCategoryFromAPI() {
        // Khởi tạo API Service
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        // Thực hiện request
        Call<List<Category>> call = apiService.getCategoryAll();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                Log.d("MainActivity", "Response Code: " + response.code());
                Log.d("MainActivity", "Response Message: " + response.message());
                
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body();
                    Log.d("MainActivity", "Số lượng categories: " + categoryList.size());
                    
                    // Khởi tạo Adapter
                    categoryAdapter = new CategoryAdapter(MainActivity.this, categoryList);
                    
                    // Cài đặt LayoutManager và Adapter cho RecyclerView
                    rcCate.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    rcCate.setAdapter(categoryAdapter);
                    
                    // Cập nhật dữ liệu
                    categoryAdapter.notifyDataSetChanged();
                    
                    Toast.makeText(MainActivity.this, "Tải " + categoryList.size() + " categories thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("MainActivity", "Response không thành công. Code: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("MainActivity", "Error body: " + response.errorBody().string());
                        } catch (Exception e) {
                            Log.e("MainActivity", "Không đọc được error body: " + e.getMessage());
                        }
                    }
                    Toast.makeText(MainActivity.this, "Không có dữ liệu! Code: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("MainActivity", "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}