package com.example.demodatabaseapi;

import android.content.Intent;
import android.os.Bundle;

import com.example.demodatabaseapi.controller.AddUserActivity;
import com.example.demodatabaseapi.database.ApiClient;
import com.example.demodatabaseapi.database.ApiService;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demodatabaseapi.model.User;
import com.example.demodatabaseapi.model.UserAdapter;
import com.google.gson.Gson;

import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private Button btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewUsers);
        btnAddUser = findViewById(R.id.btnAddUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Gọi API để lấy danh sách User
        loadUsers();

        // Xử lý khi nhấn nút Thêm User
        btnAddUser.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
            startActivity(intent);
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadUsers(); // Tải lại danh sách khi quay về màn hình chính
    }
    private void loadUsers() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<User>> call = apiService.getUsers();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> userList = response.body();
                    userAdapter = new UserAdapter(MainActivity.this, userList);
                    recyclerView.setAdapter(userAdapter);
                } else {
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}