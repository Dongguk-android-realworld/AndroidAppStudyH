package com.example.week6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.week6.ApiService;
import com.example.week6.Article;
import com.example.week6.MultipleArticle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ArticleAdapter articleAdapter;
    private TextView usernameText, bioText, emailText;
    private ImageView image;
    private RecyclerView articleRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 싱글톤 하면서 NetworkHelper로 넘어감
        /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://conduit.productionready.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);

         */

        usernameText = findViewById(R.id.tv_username);
        bioText = findViewById(R.id.tv_bio);
        emailText = findViewById(R.id.tv_email);
        image = findViewById(R.id.iv_image);
        articleRecyclerView = findViewById(R.id.rv_article);

        articleAdapter = new ArticleAdapter();
        articleRecyclerView.setAdapter(articleAdapter);

        // intent 받아오는 부분
        User user = (User) getIntent().getSerializableExtra("user");

        String token = "Token " + NetworkHelper.getInstance().getToken();

        // 유저 데이터 설정
        if (user != null) {
            usernameText.setText(user.getUsername());
            bioText.setText(user.getBio());
            emailText.setText(user.getEmail());

            if (user.getImage().isEmpty())
            {
                user.setImage("https://static.productionready.io/images/smiley-cyrus.jpg");
            }

            Glide.with(image)
                    .load(user.getImage())
                    .into(image);
        }

        articleAdapter.setOnArticleClickListener(new ArticleAdapter.OnArticleClickListener() {
            @Override
            public void onArticleClick(int position, Article article) {
                Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("article", article);
                //editRegister.launch(intent);
                startActivity(intent);

                Toast.makeText(MainActivity.this, "" + (position+1)+ "번 클릭!", Toast.LENGTH_SHORT).show();
            }
        });

        NetworkHelper.getInstance().getService().getArticleList(token).enqueue(new Callback<MultipleArticle>() {
            @Override
            public void onResponse(Call<MultipleArticle> call, Response<MultipleArticle> response) {
                if (response.isSuccessful())
                {
                    MultipleArticle articles = response.body();
                    articleAdapter.setArticle(articles.getArticles());
                    /*
                    for (Article article : articles.getArticles()) {
                        articleAdapter.addArticle(article);
                        //articleText.setText(articleText.getText() + "\n" + article.getTitle());
                    }
                     */
                }
            }

            @Override
            public void onFailure(Call<MultipleArticle> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}