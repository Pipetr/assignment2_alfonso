package ca.georgiancollege.assignment2_alfonso;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.georgiancollege.assignment2_alfonso.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseFirestore db;
    UserModel user;
    private CollectionReference collectionReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getIntent().getSerializableExtra("UserModel", UserModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");
        Log.d("tag", "main activity: " + user.toString());

    }

    private void SaveDataToNewDocument(UserModel user){
        //Log.d("tag", "SaveDataToNewDocument: "+ user.toString());
    }

    private void GetAllDocumentsInCollection(){

    }
}