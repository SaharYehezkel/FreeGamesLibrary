package com.example.freegameslibrary;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String password;
    private List<Game> favoriteGames;

    public List<Game> getItem() {
        return favoriteGames;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public User(String userEmail, String userPassword) {
        this.email = userEmail;
        this.password = userPassword;
        this.favoriteGames = new ArrayList<Game>();
    }

}
