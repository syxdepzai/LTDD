package com.example.bt5;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("categories.php")
    Call<List<Category>> getCategoryAll();
}

