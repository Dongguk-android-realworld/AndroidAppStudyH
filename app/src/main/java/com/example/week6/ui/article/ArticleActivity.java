package com.example.week6.ui.article;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.week6.model.Comment;
import com.example.week6.model.MultipleComment;
import com.example.week6.model.SingleArticle;
import com.example.week6.model.SingleComment;
import com.example.week6.ui.login.LoginActivity;
import com.example.week6.util.NetworkHelper;
import com.example.week6.R;
import com.example.week6.model.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleActivity extends AppCompatActivity {

    private CommentAdapter commentAdapter;
    private TextView titleText, descriptionText, bodyText, createdAtText, usernameText, tagText;
    private EditText commentEditText;
    private Button favoritedButton, commentAddButton;
    private ImageView image;
    private RecyclerView commentRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        titleText = findViewById(R.id.tv_title);
        descriptionText = findViewById(R.id.tv_description);
        bodyText = findViewById(R.id.tv_body);
        createdAtText = findViewById(R.id.tv_createdAt);
        usernameText = findViewById(R.id.tv_username);
        tagText = findViewById(R.id.tv_tag);
        commentEditText = findViewById(R.id.et_comment);
        favoritedButton = findViewById(R.id.btn_favorited);
        commentAddButton = findViewById(R.id.btn_comment);
        image = findViewById(R.id.iv_image);

        commentRecyclerView = findViewById(R.id.rv_comment);

        commentAdapter = new CommentAdapter();
        commentRecyclerView.setAdapter(commentAdapter);


        Article article = (Article) getIntent().getSerializableExtra("article");
        int position = getIntent().getIntExtra("position", -1);
        String token = "Token " + NetworkHelper.getInstance().getToken();
        String slug = getIntent().getStringExtra("slug");

        if (slug != null)
        {
            NetworkHelper.getInstance().getService().getArticle(slug).enqueue(new Callback<SingleArticle>() {
                @Override
                public void onResponse(Call<SingleArticle> call, Response<SingleArticle> response) {
                    if (response.isSuccessful()) {
                        titleText.setText(article.getTitle());
                        descriptionText.setText(article.getDescription());
                        bodyText.setText(article.getBody());
                        createdAtText.setText(article.getCreatedAt().substring(0, 10) + " " + article.getCreatedAt().substring(11, 19));
                        usernameText.setText(article.getAuthor().getUsername());
                        favoritedButton.setText(""+article.getFavoritesCount());
                        favoritedButton.setSelected(article.isFavorited());
                        String tagString = "";

                        for (int i=0;i<article.getTagList().size();i++)
                        {
                            tagString = tagString + "#" + article.getTagList().get(i);
                        }

                        tagText.setText(tagString);
                        Glide.with(image)
                                .load(article.getAuthor().getImage())
                                .into(image);
                    }
                }

                @Override
                public void onFailure(Call<SingleArticle> call, Throwable t) {

                }
            });

            // 좋아요 버튼 클릭 리스너
            favoritedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 현재 좋아요 상태에 따라 좋아요 설정/해제 구분
                    if (article.isFavorited()) {
                        // 좋아요 해제
                        NetworkHelper.getInstance().getService().unfavoriteArticle(token, slug).enqueue(new Callback<SingleArticle>() {
                            @Override
                            public void onResponse(Call<SingleArticle> call, Response<SingleArticle> response) {
                                article.setFavoritesCount(article.getFavoritesCount()-1);
                                article.setFavorited(!(article.isFavorited()));

                                favoritedButton.setText(""+article.getFavoritesCount());
                                favoritedButton.setSelected(article.isFavorited());
                            }

                            @Override
                            public void onFailure(Call<SingleArticle> call, Throwable t) {

                            }
                        });
                    }
                    else {
                        // 좋아요 설정
                        NetworkHelper.getInstance().getService().favoriteArticle(token, slug).enqueue(new Callback<SingleArticle>() {
                            @Override
                            public void onResponse(Call<SingleArticle> call, Response<SingleArticle> response) {
                                article.setFavoritesCount(article.getFavoritesCount()+1);
                                article.setFavorited(!(article.isFavorited()));

                                favoritedButton.setText(""+article.getFavoritesCount());
                                favoritedButton.setSelected(article.isFavorited());
                            }

                            @Override
                            public void onFailure(Call<SingleArticle> call, Throwable t) {

                            }
                        });
                    }
                }
            });

            // 댓글 게시 버튼 클릭 리스너
            commentAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String commentBody = commentEditText.getText().toString();

                    if (commentBody.isEmpty()) {
                        Toast.makeText(ArticleActivity.this, "내용이 없습니다", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Comment comment = new Comment(commentBody);
                        SingleComment body = new SingleComment(comment);
                        NetworkHelper.getInstance().getService().addComment(token, slug, body).enqueue(new Callback<SingleComment>() {
                            @Override
                            public void onResponse(Call<SingleComment> call, Response<SingleComment> response) {
                                Comment responseComment = response.body().getComment();
                                commentAdapter.addComment(responseComment);
                            }

                            @Override
                            public void onFailure(Call<SingleComment> call, Throwable t) {

                            }
                        });
                    }
                }
            });

            NetworkHelper.getInstance().getService().getCommentList(slug).enqueue(new Callback<MultipleComment>() {
                @Override
                public void onResponse(Call<MultipleComment> call, Response<MultipleComment> response) {
                    if (response.isSuccessful())
                    {
                        List<Comment> comments = response.body().getComments();
                        if (comments.size() != 0)
                        {
                            commentAdapter.setCommentList(comments);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MultipleComment> call, Throwable t) {

                }
            });
        }
    }
}