package com.example.recipe_helper.HttpConnection;

import com.example.recipe_helper.DataFrame.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
//    @GET("/reply/read")
//    Call<ArticleCommentResponse> readArticleComment(@Query("articleID") int articleID, @Query("communityType") int communityType, @Query("communityID") int communityID);
//
//    @POST("/reply/write")
//    Call<BaseResponse> uploadComment(@Body JsonObject body);

    @GET("/recipe/test")
    Call<RecipeResponse> getText(@Query("recipeName") String recipeName, @Query("ingredientName") String ingredientName);
}