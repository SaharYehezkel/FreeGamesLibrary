package com.example.freegameslibrary;

import android.util.Log;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

public class RatingManager {

    public static void updateRating(String gameId, float newRating) {
        DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference().child("games").child(gameId);

        // Fetch the current average rating
        gameRef.child("averageRating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    float currentRating = dataSnapshot.getValue(Float.class);
                    int numberOfRatings = 1;

                    // If there are previous ratings, calculate the new average
                    if (currentRating != 0.0f) {
                        numberOfRatings++;
                        currentRating = (currentRating + newRating) / numberOfRatings;
                    } else {
                        currentRating = newRating;
                    }

                    // Update the average rating in the database
                    gameRef.child("averageRating").setValue(currentRating);
                } else {
                    // Handle case where the game does not exist
                    Log.e("RatingManager", "Game with ID " + gameId + " does not exist.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.e("RatingManager", "Failed to fetch current rating: " + databaseError.getMessage());
            }
        });
    }

    public static void updateGameAverageRating(String gameId) {
        DatabaseReference gameRatingsRef = FirebaseDatabase.getInstance().getReference().child("game_ratings").child(gameId);
        gameRatingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float totalRating = 0;
                int numRatings = 0;
                for (DataSnapshot ratingSnapshot : dataSnapshot.getChildren()) {
                    Float rating = ratingSnapshot.getValue(Float.class);
                    if (rating != null) {
                        totalRating += rating;
                        numRatings++;
                    }
                }
                if (numRatings > 0) {
                    float averageRating = totalRating / numRatings;
                    DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference().child("games").child(gameId);
                    gameRef.child("average_rating").setValue(averageRating);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}