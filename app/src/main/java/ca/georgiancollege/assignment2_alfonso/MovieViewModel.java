package ca.georgiancollege.assignment2_alfonso;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieViewModel extends ViewModel {

    private final MutableLiveData<List<MovieModel>> movieListLiveData = new MutableLiveData<>();
    MovieModel movieModel = new MovieModel();
    public LiveData<List<MovieModel>> getMovieData() {
        return movieListLiveData;
    }
    FirebaseFirestore db;
    private CollectionReference collectionReference;
    private UserModel user;

    public MovieViewModel() {
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Movies");
    }
    
    public void setUser(UserModel user) {
        this.user = user;
    }

    public void retrieveMovies(UserModel user) {
        if (user == null) {
            Log.e("MovieViewModel", "User is null, cannot retrieve movies");
            movieListLiveData.postValue(new ArrayList<>());
            return;
        }
        
        this.user = user;
        collectionReference.whereEqualTo("userUid", user.getUid()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<MovieModel> movieList = new ArrayList<>();
                    for (var document : queryDocumentSnapshots.getDocuments()) {
                        MovieModel movie = document.toObject(MovieModel.class);
                        if (movie != null) {
                            movie.setUid(document.getId()); // Set document ID for editing/deleting
                            movieList.add(movie);
                        }
                    }
                    Log.d("MovieViewModel", "Retrieved " + movieList.size() + " movies for user: " + user.getUid());
                    movieListLiveData.postValue(movieList);
                })
                .addOnFailureListener(e -> {
                    Log.e("MovieViewModel", "Error retrieving movies: ", e);
                    movieListLiveData.postValue(new ArrayList<>());
                });
    }

    public void SearchMovie(String title) {

        String url = "https://www.omdbapi.com/?apikey=62703845&type=movie&s=" + title + "&plot=short";
        ApiClient.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                String error = e.getMessage();
                Log.d("TAG", error);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("TAG", "onResponse: ");
                assert response.body() != null;
                String responseData = response.body().string();
                Log.d("TAG", "Response: " + responseData);
                List<MovieModel> movieList = new ArrayList<>();
                JSONObject json = null;
                try{
                    json = new JSONObject(responseData);
                    for (int i = 0; i < json.getJSONArray("Search").length(); i++) {
                        JSONObject movieJson = json.getJSONArray("Search").getJSONObject(i);
                        String title = movieJson.getString("Title");
                        String year = movieJson.getString("Year");
                        Uri poster = movieJson.getString("Poster").equals("N/A") ? null : Uri.parse(movieJson.getString("Poster"));

                        movieList.add(new MovieModel(title, year, poster));
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                movieListLiveData.postValue(movieList);
            }
        });
    }

    public void SaveMovieToCollection(MovieModel movie) {
        if (movie != null && this.user != null) {
            movie.setUserUid(this.user.getUid());
            collectionReference.add(movie)
                .addOnSuccessListener(documentReference -> {
                    // Set the document ID as the movie UID
                    String documentId = documentReference.getId();
                    movie.setUid(documentId);
                    // Update the document with the UID
                    collectionReference.document(documentId).set(movie)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("MovieViewModel", "Movie added with ID: " + documentId);
                            // Refresh the movie list
                            retrieveMovies(this.user);
                        })
                        .addOnFailureListener(e -> Log.e("MovieViewModel", "Error updating movie with UID: ", e));
                })
                .addOnFailureListener(e -> Log.e("MovieViewModel", "Error adding movie: ", e));
        } else {
            Log.e("MovieViewModel", "Movie is null or user not set, cannot save to collection.");
        }
    }

    public void EditMovieInCollection(MovieModel movie, String movieId) {
        if (movie != null && movieId != null) {
            collectionReference.document(movieId).set(movie)
                .addOnSuccessListener(aVoid -> Log.d("MovieViewModel", "Movie updated successfully."))
                .addOnFailureListener(e -> Log.e("MovieViewModel", "Error updating movie: ", e));
        } else {
            Log.e("MovieViewModel", "Movie or Movie ID is null, cannot update.");
        }
    }

    public void DeleteMovieFromCollection(String movieId) {
        if (movieId != null) {
            collectionReference.document(movieId).delete()
                .addOnSuccessListener(aVoid -> Log.d("MovieViewModel", "Movie deleted successfully."))
                .addOnFailureListener(e -> Log.e("MovieViewModel", "Error deleting movie: ", e));
        } else {
            Log.e("MovieViewModel", "Movie ID is null, cannot delete.");
        }
    }
}
