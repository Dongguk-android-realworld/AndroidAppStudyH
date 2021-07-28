package com.example.week6.ui.article;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.week6.model.Comment;
import com.example.week6.model.MultipleComment;
import com.example.week6.model.SingleArticle;
import com.example.week6.util.NetworkHelper;
import com.example.week6.R;
import com.example.week6.model.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleActivity extends AppCompatActivity {

    private CommentAdapter commentAdapter;
    private TextView titleText, descriptionText, bodyText, createdAtText, usernameText, favoritesCountText, tagText;
    private Button favoritedButton;
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
        favoritedButton = findViewById(R.id.btn_favorited);
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

            favoritedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (article.isFavorited()) {
                        NetworkHelper.getInstance().getService().unfavoriteArticle(token, slug).enqueue(new Callback<SingleArticle>() {
                            @Override
                            public void onResponse(Call<SingleArticle> call, Response<SingleArticle> response) {
                                article.setFavoritesCount(article.getFavoritesCount()-1);
                                article.setFavorited(!(article.isFavorited()));

                                favoritedButton.setText(""+article.getFavoritesCount());
                                favoritedButton.setSelected(article.isFavorited());
                                //notifyItemChanged(position);
                            }

                            @Override
                            public void onFailure(Call<SingleArticle> call, Throwable t) {

                            }
                        });
                    }
                    else {
                        NetworkHelper.getInstance().getService().favoriteArticle(token, slug).enqueue(new Callback<SingleArticle>() {
                            @Override
                            public void onResponse(Call<SingleArticle> call, Response<SingleArticle> response) {
                                article.setFavoritesCount(article.getFavoritesCount()+1);
                                article.setFavorited(!(article.isFavorited()));

                                favoritedButton.setText(""+article.getFavoritesCount());
                                favoritedButton.setSelected(article.isFavorited());
                                //notifyItemChanged(position);
                            }

                            @Override
                            public void onFailure(Call<SingleArticle> call, Throwable t) {

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
                        //Toast.makeText(ArticleActivity.this, "HI~", Toast.LENGTH_SHORT).show();
                        List<Comment> comments = response.body().getComments();
                        if (comments.size() != 0)
                        {
                            commentAdapter.setCommentList(comments);
                        }
                    }
                    //Toast.makeText(ArticleActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<MultipleComment> call, Throwable t) {
                    //Toast.makeText(ArticleActivity.this, "OOPS~", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}