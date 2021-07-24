package com.example.week6.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.week6.util.NetworkHelper;
import com.example.week6.R;
import com.example.week6.model.User;
import com.example.week6.model.UserBody;
import com.example.week6.ui.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText idEditText, passwordEditText;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idEditText = findViewById(R.id.et_idInput);
        passwordEditText = findViewById(R.id.et_pwInput);
        loginButton = findViewById(R.id.btn_login);
        registerButton = findViewById(R.id.btn_register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (id.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "에러", Toast.LENGTH_SHORT).show();
                }
                else {
                    User user = new User(id, password);
                    UserBody body = new UserBody(user);
                    NetworkHelper.getInstance().getService().login(body).enqueue(new Callback<UserBody>() {
                        @Override
                        public void onResponse(Call<UserBody> call, Response<UserBody> response) {
                            if (response.isSuccessful()) {
                                // 유저 이름 화면에 띄우기
                                User responseUser = response.body().getUser();
                                NetworkHelper.getInstance().setToken(responseUser.getToken());

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("user", responseUser);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<UserBody> call, Throwable t) {
                            //t.printStackTrace();
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}