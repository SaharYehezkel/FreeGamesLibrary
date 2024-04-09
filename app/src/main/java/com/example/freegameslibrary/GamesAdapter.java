package com.example.freegameslibrary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolder> {

    private List<Game> gamesList;
    private List<Game> originalGamesList;
    private String currentUserUid;
    private View.OnClickListener itemClickListener;

    public GamesAdapter(List<Game> gamesList, String currentUserUid) {
        this.gamesList = gamesList;
        this.currentUserUid = currentUserUid;
        this.originalGamesList = new ArrayList<>(gamesList);
        this.itemClickListener = itemClickListener;

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title , description;
        TextView genre;
        ImageView image;
        ImageButton like;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.gameTitleTextView);
            genre = itemView.findViewById(R.id.genreTextView);
            description = itemView.findViewById(R.id.tv_short_description);
            image = itemView.findViewById(R.id.imageView);
            like = itemView.findViewById(R.id.like);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        view.setOnClickListener(itemClickListener); //  Set click listener to the view
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Game game = gamesList.get(position);
        holder.title.setText(game.getTitle());
        holder.genre.setText(game.getGenre());
        Glide.with(holder.itemView.getContext())
                .load(game.getThumbnail())
                .into(holder.image);

        // Get the current user from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            // Check if the game is already in the user's favorite games list
            Query query = userRef.child("favoriteGames").orderByChild("title").equalTo(game.getTitle());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // The game is already in the user's favorite games list
                        holder.like.setImageResource(android.R.drawable.btn_star_big_on);
                    } else {
                        // The game is not in the user's favorite games list
                        holder.like.setImageResource(android.R.drawable.btn_star_big_off);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
                    // Check if the game is already in the user's favorite games list
                    Query query = userRef.child("favoriteGames").orderByChild("title").equalTo(game.getTitle());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // The game is already in the user's favorite games list, remove it
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.getRef().removeValue();
                                    // Update the favorite status of the game
                                    holder.like.setImageResource(android.R.drawable.btn_star_big_off);
                                }
                            } else {
                                // The game is not in the user's favorite games list, add it
                                userRef.child("favoriteGames").child(String.valueOf(game.getId())).setValue(game);
                                // Update the favorite status of the game
                                holder.like.setImageResource(android.R.drawable.btn_star_big_on);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                        }
                    });
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                //         Game currentGame = gamesList.get(position);
                // Pass the data
/*
                intent.putExtra("game", game);              //  intent.putExtra("poster", currentItem.get());
                intent.putExtra("name", currentGame.getTitle());
             //   intent.putExtra("imageDrawable", currentGame.getImage());
                intent.putExtra("genre", currentGame.getBio());
                intent.putExtra("genre", currentGame.getBio());
                context.startActivity(intent);
*/

                intent.putExtra("game", game);              //  intent.putExtra("poster", currentItem.get());
                context.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    // Method to update the dataset
    public void setGames(List<Game> games) {
        this.gamesList = games;
        this.originalGamesList = new ArrayList<>(gamesList);
        notifyDataSetChanged();
    }

    public void resetOriginalDataset() {
        gamesList = new ArrayList<>(originalGamesList);
        notifyDataSetChanged();
    }

    public void filter(String text) {
        if (text.isEmpty()) {
            resetOriginalDataset();
        } else {
            ArrayList<Game> filteredList = new ArrayList<>();
            for (Game item : originalGamesList) {
                if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            updateDataset(filteredList);
        }
    }

    public void updateDataset(List<Game> filteredList) {
        gamesList = filteredList;
        notifyDataSetChanged();
    }
}
