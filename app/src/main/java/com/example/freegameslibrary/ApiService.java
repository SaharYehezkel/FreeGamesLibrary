package com.example.freegameslibrary;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/games")
    Call<List<Game>> getAllGames();

    @GET("api/games")
    Call<List<Game>> getGamesByGenre(@Query("category") String genre);

    // Retrofit Instance
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://www.freetogame.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiService apiService = retrofit.create(ApiService.class);
}




