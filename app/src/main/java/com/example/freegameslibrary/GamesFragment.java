package com.example.freegameslibrary;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Button;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class GamesFragment extends Fragment {

    private SearchView searchView;
    private final String[] genres = {
            "mmorpg", "shooter", "strategy", "racing", "sports", "social",
            "sandbox", "open-world", "survival", "pvp", "pve", "pixel",
            "zombie", "first-person", "third-Person",
            "tank", "space", "card", "battle-royale", "mmo", "mmofps", "mmotps", "3d", "2d", "anime",
            "fantasy", "sci-fi", "fighting", "action-rpg", "action", "military",
            "martial-arts", "flight", "horror", "mmorts"
    };

    private RecyclerView recyclerView;
    private GamesAdapter gamesAdapter;
    private AllGamesDataSet allGamesDataSet;
    private RecyclerView genresRecyclerView;
    private Button showFavoritesButton, homeButton;

    public GamesFragment() {
        // Required empty public constructor
    }

    public static GamesFragment newInstance(String param1, String param2) {
        GamesFragment fragment = new GamesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allGamesDataSet = new AllGamesDataSet();
        fetchAllGames();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);
        initializeUI(view);
        return view;
    }

    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewGames);
        gamesAdapter = new GamesAdapter(new ArrayList<>(), "");
        recyclerView.setAdapter(gamesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        genresRecyclerView = view.findViewById(R.id.genreRecyclerView);
        genresRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        GenreAdapter genreAdapter = new GenreAdapter(genres, this::onGenreSelected);
        genresRecyclerView.setAdapter(genreAdapter);

        searchView = view.findViewById(R.id.searchView);
        setupSearchView();

        showFavoritesButton = view.findViewById(R.id.showFavoritesButton);
        homeButton = view.findViewById(R.id.home_button);
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        showFavoritesButton.setOnClickListener(v -> {
            showFavoriteGames();
            setButtonState(false); // False for Home, True for Favorites
        });

        homeButton.setOnClickListener(v -> {
            fetchAllGames();
            setButtonState(true); // True for Home, False for Favorites
        });

        // Set default state
        setButtonState(true); // True for Home, False for Favorites
    }

    private void setButtonState(boolean isHomeSelected) {
        if (isHomeSelected) {
            homeButton.setBackgroundResource(R.drawable.button2);
            showFavoritesButton.setBackgroundResource(R.drawable.buttonoff);
        } else {
            homeButton.setBackgroundResource(R.drawable.buttonoff);
            showFavoritesButton.setBackgroundResource(R.drawable.button2);
        }
    }

    private void showFavoriteGames() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            userRef.child("favoriteGames").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Game> favoriteGames = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Game game = snapshot.getValue(Game.class);
                        if (game != null) {
                            favoriteGames.add(game);
                        }
                    }
                    if (!favoriteGames.isEmpty()) {
                        // Display the favorite games in the RecyclerView
                        gamesAdapter.setGames(favoriteGames);
                        gamesAdapter.notifyDataSetChanged();
                    } else {
                        // Handle case where there are no favorite games
                        Toast.makeText(getActivity(), "No favorite games found", Toast.LENGTH_SHORT).show();
                        // Set the Home button as selected since there are no favorite games
                        Button homeButton = getView().findViewById(R.id.home_button);
                        Button favoriteButton = getView().findViewById(R.id.showFavoritesButton);
                        homeButton.setBackgroundResource(R.drawable.button2);
                        favoriteButton.setBackgroundResource(R.drawable.buttonoff);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Toast.makeText(getActivity(), "Failed to retrieve favorite games", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setAllGamesDataSet(AllGamesDataSet allGamesDataSet) {
        this.allGamesDataSet = allGamesDataSet;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                gamesAdapter.filter(newText);
                return false;
            }
        });
    }

    private void fetchAllGames() {
        allGamesDataSet.fetchAllGames(new AllGamesDataSet.OnGamesFetchedListener() {
            @Override
            public void onSuccess(List<Game> games) {
                if (isAdded()) {
                    gamesAdapter.setGames(games);
                    gamesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String message) {
                if (isAdded()) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void onGenreSelected(String genre) {
        if (genre == null) {
            allGamesDataSet.fetchAllGames(new AllGamesDataSet.OnGamesFetchedListener() {
                @Override
                public void onSuccess(List<Game> games) {
                    gamesAdapter.setGames(games);
                    gamesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            allGamesDataSet.fetchGamesByGenre(genre, new AllGamesDataSet.OnGamesFetchedListener() {
                @Override
                public void onSuccess(List<Game> games) {
                    gamesAdapter.setGames(games);
                    gamesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}