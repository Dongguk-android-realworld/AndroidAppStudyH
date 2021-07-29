package com.example.week6.ui.article;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.week6.R;
import com.example.week6.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> commentList = new ArrayList<>();

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> comments) {
        commentList.clear();
        commentList.addAll(comments);
        notifyDataSetChanged();
    }

    // 하나 추가
    public void addComment(Comment comment)
    {
        commentList.add(0, comment);
        notifyDataSetChanged();
    }

    // 여러개 추가
    public void addComment(List<Comment> comments)
    {
        commentList.addAll(0, comments);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        holder.bind(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        if (commentList == null)
        {
            return 0;
        }
        return commentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView bodyText, createdAtText, usernameText, bioText;
        ImageView image;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bodyText = itemView.findViewById(R.id.tv_body);
            createdAtText = itemView.findViewById(R.id.tv_createdAt);
            usernameText = itemView.findViewById(R.id.tv_username);
            bioText = itemView.findViewById(R.id.tv_bio);
            image = itemView.findViewById(R.id.iv_image);
        }

        void bind(Comment comment) {
            bodyText.setText(comment.getBody());
            createdAtText.setText(comment.getCreatedAt().substring(0, 10) + " " + comment.getCreatedAt().substring(11, 19));
            usernameText.setText(comment.getAuthor().getUsername());
            bioText.setText(comment.getAuthor().getBio());

            Glide.with(image)
                    .load(comment.getAuthor().getImage())
                    .into(image);
        }
    }
}
