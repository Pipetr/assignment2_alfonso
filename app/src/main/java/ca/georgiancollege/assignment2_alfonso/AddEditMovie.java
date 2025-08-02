package ca.georgiancollege.assignment2_alfonso;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import ca.georgiancollege.assignment2_alfonso.databinding.ActivityAddEditMovieBinding;

public class AddEditMovie extends AppCompatActivity {

    ActivityAddEditMovieBinding binding;
    MovieViewModel viewModel;
    UserModel user;
    MovieModel movie;
    String movieId;
    boolean isEditMode = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        
        // Get user data from intent
        user = (UserModel) getIntent().getSerializableExtra("UserModel");
        
        // Check if we're editing an existing movie
        movie = (MovieModel) getIntent().getSerializableExtra("MovieModel");
        movieId = getIntent().getStringExtra("MovieId");
        
        if (movie != null && movieId != null) {
            isEditMode = true;
            populateFields();
            binding.btnSave.setText("Update Movie");
            binding.txtTitle.setText("Edit Movie");
        } else {
            binding.btnSave.setText("Add Movie");
            binding.txtTitle.setText("Add New Movie");
        }
        
        binding.btnSave.setOnClickListener(v -> saveMovie());
        
        binding.btnCancel.setOnClickListener(v -> {
            finish(); // Go back to previous screen
        });
    }
    
    private void populateFields() {
        if (movie != null) {
            binding.txtInTitle.setText(movie.getTitle());
            binding.txtInYear.setText(movie.getYear());
            if (movie.getPoster() != null) {
                binding.txtInPosterUrl.setText(movie.getPoster());
            }
        }
    }
    
    private void saveMovie() {
        String title = Objects.requireNonNull(binding.txtInTitle.getText()).toString().trim();
        String year = Objects.requireNonNull(binding.txtInYear.getText()).toString().trim();
        String posterUrl = Objects.requireNonNull(binding.txtInPosterUrl.getText()).toString().trim();
        
        // Validate fields
        if (title.isEmpty()) {
            binding.txtInTitle.setError("Title is required");
            return;
        }
        if (year.isEmpty()) {
            binding.txtInYear.setError("Year is required");
            return;
        }
        
        // Create or update movie
        Uri poster = posterUrl.isEmpty() ? null : Uri.parse(posterUrl);
        
        if (isEditMode) {
            // Update existing movie
            movie.setTitle(title);
            movie.setYear(year);
            movie.setPosterUri(poster);
            viewModel.EditMovieInCollection(movie, movieId);
            Toast.makeText(this, "Movie updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Add new movie
            MovieModel newMovie = new MovieModel(title, year, poster, user.getUid(), null);
            viewModel.SaveMovieToCollection(newMovie);
            Toast.makeText(this, "Movie added successfully", Toast.LENGTH_SHORT).show();
        }
        
        // Go back to main activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("UserModel", user);
        startActivity(intent);
        finish();
    }
}
