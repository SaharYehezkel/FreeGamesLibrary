package com.example.freegameslibrary;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private final String[] genres;
    private Integer selectedPosition = null; // null indicates no genre is selected

    private final OnGenreClickListener genreClickListener;

    public GenreAdapter(String[] genres, OnGenreClickListener genreClickListener) {
        this.genres = genres;
        this.genreClickListener = genreClickListener;
    }
    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre_button, parent, false);
        return new GenreViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        String genre = genres[position];
        holder.genreButton.setText(genre);

        // Set background based on selection
        holder.genreButton.setBackgroundResource(
                selectedPosition != null && selectedPosition == position ? R.drawable.button2 : R.drawable.buttonoff
        );

        holder.genreButton.setOnClickListener(v -> {
            // Toggle selection
            if (selectedPosition != null && selectedPosition == position) {
                // Deselecting the currently selected button
                selectedPosition = null;
                genreClickListener.onGenreClick(null); // Pass null to indicate deselection
            } else {
                // Selecting a new genre
                selectedPosition = position;
                genreClickListener.onGenreClick(genre);
            }
            notifyDataSetChanged(); // Refresh list to update backgrounds
        });
    }

    @Override
    public int getItemCount() {
        return genres.length;
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        final Button genreButton;

        GenreViewHolder(View itemView) {
            super(itemView);
            genreButton = itemView.findViewById(R.id.buttonGenre);
        }
    }

    public interface OnGenreClickListener {
        void onGenreClick(String genre);
    }
}

