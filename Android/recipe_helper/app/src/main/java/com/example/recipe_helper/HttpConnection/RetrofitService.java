package com.example.recipe_helper.HttpConnection;

import com.example.recipe_helper.Commnuity.DataFrame.Comments2Response;
import com.example.recipe_helper.Commnuity.DataFrame.PostResponse;
import com.example.recipe_helper.DataFrame.IngredientResponse;
import com.example.recipe_helper.DataFrame.RecipeResponse;
import com.example.recipe_helper.DataFrame.UserInfoResponse;
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
    Call<UserInfoResponse> checkUser(@Query("id") long id);

    @POST("recipe/signUp")
    Call<BaseResponse> signUp(@Body JsonObject body);

    @GET("recipe/loadPost")
    Call<PostResponse> loadPost();

    @GET("recipe/uploadPost")
    Call<BaseResponse> uploadPost(@Query("id") long id, @Query("content") String content, @Query("title") String title);

    @GET("recipe/loadComment")
    Call<Comments2Response> loadComment(@Query("postID") int postID);

    @GET("recipe/uploadComment")
    Call<BaseResponse> uploadComment(@Query("id") long userID, @Query("postID") int postID, @Query("content") String content);

    @GET("recipe/searchRecipe")
    Call<RecipeResponse> searchRecipe(@Query("recipeTitle") String recipeTitle);

    @GET("recipe/searchIngredient")
    Call<IngredientResponse> searchIngredient(@Query("ingredientName") String ingredientName);

    @GET("recipe/loadHotRecipe")
    Call<RecipeResponse> loadHotRecipe();

    @GET("recipe/loadRecommendRecipe1")
    Call<RecipeResponse> loadRecommendRecipe1();

    @GET("recipe/loadRecommendRecipe2")
    Call<RecipeResponse> loadRecommendRecipe2();

    @GET("recipe/loadRecommendRecipe3")
    Call<RecipeResponse> loadRecommendRecipe3();

    @GET("recipe/loadTasteRecipe")
    Call<RecipeResponse> loadTasteRecipe();

    @GET("recipe/loadScrap")
    Call<RecipeResponse> loadScrap(@Query("userID") long userID);

    @GET("recipe/setScrap")
    Call<BaseResponse> setScrap(@Query("userID") long userID, @Query("recipeID") int recipeID);

    @GET("recipe/isScrap")
    Call<BaseResponse> isScrap(@Query("userID") long userID, @Query("recipeID") int recipeID);

    @GET("recipe/changeUserInfo")
    Call<UserInfoResponse> changeUserInfo(@Query("userID") long userID, @Query("allergy") String allergy, @Query("disease") String disease);
}