package com.example.tp3.service;

import com.example.tp3.model.GitRepo;
import com.example.tp3.model.GitUserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitRepoServiceAPI {
    @GET("search/users")
    public Call<GitUserResponse> searchUsers(@Query("q") String query);
    @GET("users/{u}/repos")
    public Call<List<GitRepo>> usersRepos(@Path("u") String username);
}
