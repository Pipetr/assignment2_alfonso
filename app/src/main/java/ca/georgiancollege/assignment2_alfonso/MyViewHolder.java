package ca.georgiancollege.assignment2_alfonso;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    public TextView title;
    public TextView released;
    public Button editButton;
    public Button deleteButton;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.moviePoster);
        title = itemView.findViewById(R.id.txtViewTitle);
        released = itemView.findViewById(R.id.tvRelease);
        editButton = itemView.findViewById(R.id.btnEdit);
        deleteButton = itemView.findViewById(R.id.btnDelete);
    }
}
