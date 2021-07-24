package com.example.week6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, idEditText, passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.et_usernameInput);
        idEditText = findViewById(R.id.et_idInput);
        passwordEditText = findViewById(R.id.et_pwInput);
        registerButton = findViewById(R.id.btn_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String id = idEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty() || id.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "에러", Toast.LENGTH_SHORT).show();
                }
                else {
                    User user = new User(id, password, username);
                    UserBody body = new UserBody(user);
                    NetworkHelper.getInstance().getService().register(body).enqueue(new Callback<UserBody>() {
                        @Override
                        public void onResponse(Call<UserBody> call, Response<UserBody> response) {
                            if (response.isSuccessful()) {
                                finish();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserBody> call, Throwable t) {
                            //t.printStackTrace();
                            Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
