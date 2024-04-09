package com.example.freegameslibrary;

import com.example.freegameslibrary.ApiService;
import com.example.freegameslibrary.Game;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllGamesDataSet {

    private final Retrofit retrofit;
    private final ApiService apiService;
    private List<Game> gamesList;
    private List<Game> originalGamesList;


    public AllGamesDataSet() {
        gamesList = new ArrayList<>();

        // Initialize Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.freetogame.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public void fetchAllGames(OnGamesFetchedListener listener) {
        apiService.getAllGames().enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    gamesList = response.body();
                    originalGamesList = new ArrayList<>(gamesList); // Save the original games list
                    listener.onSuccess(gamesList);
                } else {
                    listener.onError("Failed to load games");
                }
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                listener.onError("Error: " + t.getMessage());
            }
        });
    }


    public void fetchGamesByGenre(String genre, OnGamesFetchedListener listener) {
        apiService.getGamesByGenre(genre).enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    gamesList = response.body();
                    listener.onSuccess(gamesList);
                } else {
                    listener.onError("Failed to load games");
                }
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                listener.onError("Error: " + t.getMessage());
            }
        });
    }

    public List<Game> getGames() {
        return gamesList;
    }

    public interface OnGamesFetchedListener {
        void onSuccess(List<Game> games);
        void onError(String message);
    }
}