package com.example.freegameslibrary;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    TextView titleGame, genreGame, shortDescriptionGame, gameUrlGame, platformGame, publisherGame, developerGame, releaseDateGame;
    ImageView posterGame;
    Button backButton;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        game = getIntent().getParcelableExtra("game");

        titleGame = findViewById(R.id.tv_title);
        genreGame = findViewById(R.id.tv_genre);
        shortDescriptionGame = findViewById(R.id.tv_short_description);
        gameUrlGame = findViewById(R.id.tv_game_url);
        platformGame = findViewById(R.id.tv_platform);
        publisherGame = findViewById(R.id.tv_publisher);
        developerGame = findViewById(R.id.tv_developer);
        releaseDateGame = findViewById(R.id.tv_release_date);
        posterGame = findViewById(R.id.img_poster);
        backButton = findViewById(R.id.back_button);

        titleGame.setText(game.getTitle());
        genreGame.setText(game.getGenre());
        shortDescriptionGame.setText(game.getShortDescription());
        gameUrlGame.setText(game.getGameUrl());
        platformGame.setText("Platform: " + game.getPlatform());
        publisherGame.setText("Publisher: " + game.getPublisher());
        developerGame.setText("Developer: " + game.getDeveloper());
        releaseDateGame.setText("Release Date: " + game.getReleaseDate());

        Glide.with(this)
                .load(game.getThumbnail())
                .into(posterGame);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
