package com.example.week6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

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
        holder.bind(articleList.get(position));
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

        TextView titleText, descriptionText, bodyText, createdAtText, usernameText, favoritesCountText, tagText;
        ImageView image, ivThumb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.tv_title);
            descriptionText = itemView.findViewById(R.id.tv_description);
            bodyText = itemView.findViewById(R.id.tv_body);
            createdAtText = itemView.findViewById(R.id.tv_createdAt);
            usernameText = itemView.findViewById(R.id.tv_username);
            favoritesCountText = itemView.findViewById(R.id.tv_favoritesCount);
            tagText = itemView.findViewById(R.id.tv_tag);
            ivThumb = itemView.findViewById(R.id.iv_heart);
            image = itemView.findViewById(R.id.iv_image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onArticleClick(getLayoutPosition(), articleList.get(getLayoutPosition()));
                }
            });
        }

        void bind(Article article) {
            titleText.setText(article.getTitle());
            descriptionText.setText(article.getDescription());
            createdAtText.setText(article.getCreatedAt().substring(0, 10) + " " + article.getCreatedAt().substring(11, 19));
            usernameText.setText(article.getAuthor().getUsername());
            favoritesCountText.setText(""+article.getFavoritesCount());
            ivThumb.setSelected(article.isFavorited());

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
}
