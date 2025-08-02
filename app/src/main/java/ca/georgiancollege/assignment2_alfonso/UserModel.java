package ca.georgiancollege.assignment2_alfonso;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class UserModel implements Serializable {
    String uid;
    String username;
    String email;

    public UserModel() {
    }

    public UserModel(String uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserModel{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
