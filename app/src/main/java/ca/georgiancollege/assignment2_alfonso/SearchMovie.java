package ca.georgiancollege.assignment2_alfonso;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;
import java.util.Objects;

import ca.georgiancollege.assignment2_alfonso.databinding.ActivitySearchMovieBinding;

public class SearchMovie extends AppCompatActivity {

    ActivitySearchMovieBinding binding;
    MovieAdapter myAdapter;
    MovieViewModel viewModel;
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Get user data from intent
        user = (UserModel) getIntent().getSerializableExtra("UserModel");
        
        if (user == null) {
            Log.e("SearchMovie", "User is null, redirecting to login");
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
            return;
        }
        
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.setUser(user); // Set user in viewModel for saving movies
        
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the movie title from the input field
                String movieTitle = Objects.requireNonNull(binding.txtInSearch.getText()).toString().trim();
                // Check if the input is not empty
                if (!movieTitle.isEmpty()) {
                    // Call the ViewModel to search for the movie
                    viewModel.SearchMovie(movieTitle);
                } else {
                    // Show an error message or handle empty input
                    binding.txtInSearch.setError("Please enter a movie title");
                }

            }
        });

        binding.btnBack.setOnClickListener(v -> {
            // Go back to the previous screen
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("UserModel", user); // Pass the user data back
            startActivity(intent);
            finish(); // Close the current activity
        });

        viewModel.getMovieData().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if(myAdapter == null){
                    myAdapter = new MovieAdapter(movieModels, user, true); // true = search mode
                    myAdapter.setViewModel(viewModel); // Pass the viewModel to adapter
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(SearchMovie.this));
                    binding.recyclerView.setAdapter(myAdapter);
                }else {
                    // Update the adapter with the new movie list
                    myAdapter.updateData(movieModels);
                }
            }
        });

        Log.d("tag", "SearchMovie: onCreate called");
    }
}