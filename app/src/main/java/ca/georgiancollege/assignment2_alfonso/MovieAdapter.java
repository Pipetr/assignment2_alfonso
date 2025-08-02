package ca.georgiancollege.assignment2_alfonso;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<MovieModel> movieList;
    Context context;
    UserModel user;
    boolean isSearchMode;
    MovieViewModel viewModel; // Add viewModel reference

    public MovieAdapter(List<MovieModel> movieList) {
        this.movieList = movieList;
        this.isSearchMode = true; // Default to search mode
    }

    public MovieAdapter(List<MovieModel> movieList, UserModel user, boolean isSearchMode) {
        this.movieList = movieList;
        this.user = user;
        this.isSearchMode = isSearchMode;
    }
    
    public void setViewModel(MovieViewModel viewModel) {
        this.viewModel = viewModel;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card, parent, false);

        return new MyViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MovieModel movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        if(movie.getPoster() != null && !movie.getPoster().isEmpty()){
            Picasso.get()
                    .load(movie.getPoster())
                    .placeholder(R.drawable.broken_image_48)
                    .into(holder.image);
        }else {
            holder.image.setImageResource(R.drawable.broken_image_48);
        }
        holder.released.setText(movie.getYear());

        // Show/hide edit and delete buttons based on mode
        if (isSearchMode) {
            // Hide edit/delete buttons in search mode
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
            
            // Handle adding movie to favorites
            holder.itemView.setOnClickListener(v -> {
                Context context = v.getContext();
                if (context instanceof SearchMovie && user != null && viewModel != null){
                    movie.setUserUid(user.getUid());
                    viewModel.SaveMovieToCollection(movie);
                    Toast.makeText(context, "Movie added to favorites!", Toast.LENGTH_SHORT).show();
                    // Close the search activity and return to MainActivity
                    ((SearchMovie) context).finish();
                }
            });
        } else {
            // Show edit/delete buttons in favorites list
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
            
            // Handle edit button click
            holder.editButton.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, AddEditMovie.class);
                intent.putExtra("UserModel", user);
                intent.putExtra("MovieModel", movie);
                intent.putExtra("MovieId", movie.getUid());
                context.startActivity(intent);
            });
            
            // Handle delete button click
            holder.deleteButton.setOnClickListener(v -> {
                if (viewModel != null) {
                    viewModel.DeleteMovieFromCollection(movie.getUid());
                    Toast.makeText(v.getContext(), "Movie deleted from favorites!", Toast.LENGTH_SHORT).show();
                    // Remove from adapter list and notify
                    movieList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, movieList.size());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void updateData(List<MovieModel> movieModels) {
        if (movieModels != null) {
            this.movieList.clear();
            this.movieList.addAll(movieModels);
            notifyDataSetChanged();
        }
    }
}
