package com.example.recipe_helper.HttpConnection;

import com.example.recipe_helper.Commnuity.DataFrame.Comments2Response;
import com.example.recipe_helper.Commnuity.DataFrame.Post2Response;
import com.example.recipe_helper.DataFrame.RecipeResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {
//    @GET("/reply/read")
//    Call<ArticleCommentResponse> readArticleComment(@Query("articleID") int articleID, @Query("communityType") int communityType, @Query("communityID") int communityID);
//
//    @POST("/reply/write")
//    Call<BaseResponse> uploadComment(@Body JsonObject body);

    @GET("/recipe/test")
    Call<RecipeResponse> getText(@Query("recipeName") String recipeName, @Query("ingredientName") String ingredientName);

    @GET("/recipe/checkUser")
    Call<BaseResponse> checkUser(@Query("id") long id);

    @POST("recipe/signUp")
    Call<BaseResponse> signUp(@Body JsonObject body);

    @GET("recipe/loadPost")
    Call<Post2Response> loadPost();

    @GET("recipe/loadComent")
    Call<Comments2Response> loadComment();


}