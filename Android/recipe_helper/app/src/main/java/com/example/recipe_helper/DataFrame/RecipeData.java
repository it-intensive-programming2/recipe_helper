package com.example.recipe_helper.DataFrame;

public class RecipeData {
    public String recipeName;
    public String ingredientName;
    public String cookTime;
    public String photoURL;
    public String recipeURl;

    public RecipeData(String photoURL, String recipeName, String recipeURl, String cookTime, String ingredientName) {
        this.recipeName = recipeName;
        this.ingredientName = ingredientName;
        this.cookTime = cookTime;
        this.photoURL = photoURL;
        this.recipeURl = recipeURl;
    }
}
