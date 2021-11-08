package com.example.tp3.model;

import com.google.gson.annotations.SerializedName;

public class GitUser {
    public int id;
    @SerializedName("login")
    public String userName;
    @SerializedName("avatar_url")
    public String avatarUrl;
    public int score;

}
