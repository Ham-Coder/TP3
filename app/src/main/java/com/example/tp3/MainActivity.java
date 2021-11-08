package com.example.tp3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tp3.model.GitUser;
import com.example.tp3.model.GitUserResponse;
import com.example.tp3.model.usersListViewModel;
import com.example.tp3.service.GitRepoServiceAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
public List<GitUser> data = new ArrayList<>();
public static final String USERNAME_PARAM="username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button btnSearch=findViewById(R.id.btnGet);
        EditText txtSearch = findViewById(R.id.eTextSearch);
        ListView listUsers = findViewById(R.id.list);

        usersListViewModel listViewModel = new usersListViewModel(this, R.layout.users_list_view_layout,data);
        listUsers.setAdapter(listViewModel);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        btnSearch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String q = txtSearch.getText().toString();
                        GitRepoServiceAPI service = retrofit.create(GitRepoServiceAPI.class);
                        Call<GitUserResponse> gitUserResponseCall=service.searchUsers(q);

                        gitUserResponseCall.enqueue(
                                new Callback<GitUserResponse>() {
                                    @Override
                                    public void onResponse(Call<GitUserResponse> call, Response<GitUserResponse> response) {
                                        if(!response.isSuccessful()){
                                            Log.i("error",String.valueOf(response.code()));
                                            return;
                                        }
                                        GitUserResponse gitUserResponse=response.body();
                                        for(GitUser user: gitUserResponse.users){
                                          //  Log.i("username", user.userName);
                                            data.add(user);
                                        }
                                        listViewModel.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(Call<GitUserResponse> call, Throwable t) {
                                        Log.i("error",  "Error onFailure");
                                    }
                                }
                        );
                    }
                }
        );
        listUsers.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String username = data.get(i).userName;

                        Intent intent = new Intent(getApplicationContext(),ProfilActivity.class);
                        intent.putExtra(USERNAME_PARAM,username);

                        startActivity(intent);
                    }
                }
        );
    }
}