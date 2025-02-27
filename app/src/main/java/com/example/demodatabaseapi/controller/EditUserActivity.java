package com.example.demodatabaseapi.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demodatabaseapi.R;
import com.example.demodatabaseapi.database.ApiClient;
import com.example.demodatabaseapi.database.ApiService;
import com.example.demodatabaseapi.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {
    private EditText edtEditName, edtEditEmail;
    private Button btnUpdateUser;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        edtEditName = findViewById(R.id.edtEditName);
        edtEditEmail = findViewById(R.id.edtEditEmail);
        btnUpdateUser = findViewById(R.id.btnUpdateUser);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);
        String userName = intent.getStringExtra("user_name");
        String userEmail = intent.getStringExtra("user_email");

        // Hiển thị dữ liệu cũ
        edtEditName.setText(userName);
        edtEditEmail.setText(userEmail);

        // Xử lý sự kiện khi nhấn "Cập nhật User"
        btnUpdateUser.setOnClickListener(v -> {
            String updatedName = edtEditName.getText().toString();
            String updatedEmail = edtEditEmail.getText().toString();

            if (updatedName.isEmpty() || updatedEmail.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            User updatedUser = new User(userId, updatedName, updatedEmail, ""); // Password không thay đổi

            Call<User> call = apiService.updateUser(userId, updatedUser);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EditUserActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditUserActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(EditUserActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
