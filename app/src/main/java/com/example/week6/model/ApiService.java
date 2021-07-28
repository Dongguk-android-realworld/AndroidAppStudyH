package com.example.week6.model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("api/articles/{slug}")
    Call<SingleArticle> getArticle(
            @Path("slug") String slug
    );

    @GET("api/articles")
    Call<MultipleArticle> getArticleList(
            @Header("Authorization") String token
    );

    @GET("api/articles")
    Call<MultipleArticle> getArticleList();

    @POST("api/articles/{slug}/favorite")
    Call<SingleArticle> favoriteArticle(
            @Header("Authorization") String token,
            @Path("slug") String slug
    );

    @DELETE("api/articles/{slug}/favorite")
    Call<SingleArticle> unfavoriteArticle(
            @Header("Authorization") String token,
            @Path("slug") String slug
    );

    @GET("api/articles/{slug}/comments")
    Call<MultipleComment> getCommentList(
            @Path("slug") String slug
    );

    @POST("api/users/login")
    Call<UserBody> login(
            @Body UserBody requestBody
    );

    @POST("api/users")
    Call<UserBody> register(
            @Body UserBody requestBody
    );
}
