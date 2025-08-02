package ca.georgiancollege.assignment2_alfonso;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class MovieModel implements Serializable {
    private String Title;

    private String Year;

    private String Poster; // Changed from Uri to String for Firestore compatibility

    private String userUid;

    private String uid;

    // constructor
    public MovieModel() {
    }

    public MovieModel(String title, String year, Uri poster, String userUid, String uid) {
        Title = title;
        Year = year;
        Poster = poster != null ? poster.toString() : null;
        this.userUid = userUid;
        this.uid = uid;
    }

    public MovieModel(String title, String year, Uri poster) {
        Title = title;
        Year = year;
        Poster = poster != null ? poster.toString() : null;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }
    
    @Exclude
    public Uri getPosterUri() {
        return Poster != null ? Uri.parse(Poster) : null;
    }

    @Exclude
    public void setPosterUri(Uri poster) {
        Poster = poster != null ? poster.toString() : null;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
