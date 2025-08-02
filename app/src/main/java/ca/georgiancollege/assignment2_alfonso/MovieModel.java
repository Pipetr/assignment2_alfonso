package ca.georgiancollege.assignment2_alfonso;

import android.net.Uri;

public class MovieModel {
    private String Title;

    private String Year;

    private Uri Poster;

    private String userUid;

    private String uid;

    // constructor
    public MovieModel() {
    }

    public MovieModel(String title, String year, Uri poster, String userUid, String uid) {
        Title = title;
        Year = year;
        Poster = poster;
        this.userUid = userUid;
        this.uid = uid;
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

    public Uri getPoster() {
        return Poster;
    }

    public void setPoster(Uri poster) {
        Poster = poster;
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
