package com.example.week6.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.week6.R;
import com.example.week6.model.Article;
import com.example.week6.model.SingleArticle;
import com.example.week6.util.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> articleList = new ArrayList<>();

    interface OnArticleClickListener {
        void onArticleClick(int position, Article article);
    }

    private OnArticleClickListener listener;

    void setOnArticleClickListener(OnArticleClickListener listener) {
        this.listener = listener;
    }

    //public ArticleAdapter() {};

    public List<Article> getArticleList() {
        return articleList;
    }

    // 초기화
    public void setArticle(List<Article> articles)
    {
        articleList.clear();
        articleList.addAll(articles);
        notifyDataSetChanged();
    }

    // 하나 추가
    public void addArticle(Article article)
    {
        articleList.add(article);
        notifyDataSetChanged();
    }

    // 여러개 추가
    public void addArticle(List<Article> articles)
    {
        articleList.addAll(articles);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(articleList.get(position), position);
        // bind 함수로 대체
        /*
        holder.titleText.setText(articleList.get(position).getTitle());
        holder.descriptionText.setText(articleList.get(position).getDescription());
        //holder.bodyText.setText(articleList.get(position).getBody());
        //holder.createdAtText.setText(articleList.get(position).getCreatedAt());
        holder.createdAtText.setText(articleList.get(position).getCreatedAt().substring(0, 10) + " " + articleList.get(position).getCreatedAt().substring(11, 19));
        holder.usernameText.setText(articleList.get(position).getAuthor().getUsername());
        holder.favoritesCountText.setText(""+articleList.get(position).getFavoritesCount());

        String tagString = "";

        for (int i=0;i<articleList.get(position).getTagList().size();i++)
        {
            tagString = tagString + "#" + articleList.get(position).getTagList().get(i);
        }

        holder.tagText.setText(tagString);
        Glide.with(holder.image)
                .load(articleList.get(position).getAuthor().getImage())
                .into(holder.image);
        //holder.image.setImageURI((android.net.Uri)articleList.get(position).getAuthor().getImage());
         */
    }

    @Override
    public int getItemCount() {
        if (articleList == null)
        {
            return 0;
        }
        return articleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, descriptionText, bodyText, createdAtText, usernameText, tagText;
        Button favoritedButton;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.tv_title);
            descriptionText = itemView.findViewById(R.id.tv_description);
            bodyText = itemView.findViewById(R.id.tv_body);
            createdAtText = itemView.findViewById(R.id.tv_createdAt);
            usernameText = itemView.findViewById(R.id.tv_username);
            tagText = itemView.findViewById(R.id.tv_tag);
            favoritedButton = itemView.findViewById(R.id.btn_favorited);
            image = itemView.findViewById(R.id.iv_image);


            // 여기에 있지 않고 bind에 있으면 잘못
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getLayoutPosition() == RecyclerView.NO_POSITION) return;
                    listener.onArticleClick(getLayoutPosition(), articleList.get(getLayoutPosition()));
                }
            });
        }

        void bind(Article article, int position) {
            titleText.setText(article.getTitle());
            descriptionText.setText(article.getDescription());
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

            String token = "Token " + NetworkHelper.getInstance().getToken();
            String slug = article.getSlug();

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
                                notifyItemChanged(position);
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
                                notifyItemChanged(position);
                            }

                            @Override
                            public void onFailure(Call<SingleArticle> call, Throwable t) {

                            }
                        });
                    }
                }
            });
        }
    }
}
