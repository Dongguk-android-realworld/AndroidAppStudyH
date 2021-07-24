package com.example.week6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleActivity extends AppCompatActivity {

    private CommentAdapter commentAdapter;
    private TextView titleText, descriptionText, bodyText, createdAtText, usernameText, favoritesCountText, tagText;
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
        favoritesCountText = findViewById(R.id.tv_favoritesCount);
        tagText = findViewById(R.id.tv_tag);
        image = findViewById(R.id.iv_image);

        commentRecyclerView = findViewById(R.id.rv_comment);

        commentAdapter = new CommentAdapter();
        commentRecyclerView.setAdapter(commentAdapter);


        Article article = (Article) getIntent().getSerializableExtra("article");
        int position = getIntent().getIntExtra("position", -1);
        if (article != null)
        {
            titleText.setText(article.getTitle());
            descriptionText.setText(article.getDescription());
            bodyText.setText(article.getBody());
            createdAtText.setText(article.getCreatedAt().substring(0, 10) + " " + article.getCreatedAt().substring(11, 19));
            usernameText.setText(article.getAuthor().getUsername());
            favoritesCountText.setText(""+article.getFavoritesCount());
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


        NetworkHelper.getInstance().getService().getCommentList(article.getSlug()).enqueue(new Callback<MultipleComment>() {
            @Override
            public void onResponse(Call<MultipleComment> call, Response<MultipleComment> response) {
                if (response.isSuccessful())
                {
                    //Toast.makeText(ArticleActivity.this, "HI~", Toast.LENGTH_SHORT).show();
                    MultipleComment comments = response.body();
                    if (comments.getComments().size() != 0)
                    {
                        commentAdapter.setCommentList(comments.getComments());
                        /*
                        commentText.setText("");
                        for (Comment comment : comments.getComments()) {
                            commentText.setText(commentText.getText() + "\n" + comment.getBody());
                            //articleText.setText(articleText.getText() + "\n" + article.getTitle());
                        }
                         */
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