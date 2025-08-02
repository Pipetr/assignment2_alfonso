package ca.georgiancollege.assignment2_alfonso;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import ca.georgiancollege.assignment2_alfonso.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseFirestore db;
    UserModel user;
    MovieModel newMovie;
    MovieAdapter myAdapter;
    MovieViewModel viewModel;
    private CollectionReference collectionReference;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("tag", "onCreate started");
        
        user = (UserModel) getIntent().getSerializableExtra("UserModel");
        
        if (user == null) {
            Log.e("MainActivity", "User is null, redirecting to login");
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
            return;
        }
        
        Log.d("tag", "User received: " + user.getUsername());
        
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        Log.d("tag", "View binding completed");
        
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Movies");

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        
        Log.d("MainActivity", "Setting up observer");
        
        try {
            // Observe the movie data and update RecyclerView
            viewModel.getMovieData().observe(this, new Observer<List<MovieModel>>() {
                @Override
                public void onChanged(List<MovieModel> movieModels) {
                    try {
                        if (movieModels != null && !movieModels.isEmpty()) {
                            // Show RecyclerView, hide empty state
                            binding.recyclerView.setVisibility(View.VISIBLE);
                            binding.emptyStateLayout.setVisibility(View.GONE);

                            if (myAdapter == null) {
                                myAdapter = new MovieAdapter(movieModels, user, false); // false = favorites mode
                                myAdapter.setViewModel(viewModel); // Pass the viewModel to adapter
                                binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                binding.recyclerView.setAdapter(myAdapter);
                            } else {
                                myAdapter.updateData(movieModels);
                            }
                        } else {
                            // Show empty state, hide RecyclerView
                            binding.recyclerView.setVisibility(View.GONE);
                            binding.emptyStateLayout.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        Log.e("MainActivity", "Error in onChanged: ", e);
                    }
                }
            });
        } catch (Exception e) {
            Log.e("MainActivity", "Error setting up observer: ", e);
            return;
        }

        try {
            // Retrieve user's favorite movies after a small delay to ensure everything is initialized
            binding.getRoot().post(() -> {
                if (viewModel != null && user != null) {
                    Log.d("MainActivity", "Calling retrieveMovies");
                    viewModel.retrieveMovies(user);
                }
            });
        } catch (Exception e) {
            Log.e("MainActivity", "Error setting up movie retrieval: ", e);
        }

        try {
            binding.btnAddMovie.setOnClickListener(v -> {
                // Show the search movie activity
                Intent intent = new Intent(getApplicationContext(), SearchMovie.class);
                intent.putExtra("UserModel", user);
                startActivity(intent);
            });
            Log.d("MainActivity", "Add movie button setup completed");
        } catch (Exception e) {
            Log.e("MainActivity", "Error setting up add movie button: ", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the movie list when returning to this activity
        try {
            if (viewModel != null && user != null) {
                Log.d("MainActivity", "Refreshing movies for user: " + user.getUid());
                viewModel.retrieveMovies(user);
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error in onResume: ", e);
        }
    }
}