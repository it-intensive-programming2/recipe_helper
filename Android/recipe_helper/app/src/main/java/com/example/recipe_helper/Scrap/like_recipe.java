package com.example.recipe_helper.Scrap;

import android.widget.LinearLayout;

public class like_recipe {

    private String scrap_img; // 음식사진
    private String scrap_recipe_name; // 레시피 명
    private String scrap_recipe_owner; // 레시피 작성자
    private String link; // 링크

    public like_recipe(String scrap_img, String scrap_recipe_name, String scrap_recipe_owner, String link) {
        this.scrap_img = scrap_img;
        this.scrap_recipe_name = scrap_recipe_name;
        this.scrap_recipe_owner = scrap_recipe_owner;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getScrap_img() {
        return scrap_img;
    }

    public void setScrap_img(String scrap_img) {
        this.scrap_img = scrap_img;
    }

    public String getScrap_recipe_name() {
        return scrap_recipe_name;
    }

    public void setScrap_recipe_name(String scrap_recipe_name) {
        this.scrap_recipe_name = scrap_recipe_name;
    }

    public String getScrap_recipe_owner() {
        return scrap_recipe_owner;
    }

    public void setScrap_recipe_owner(String scrap_recipe_owner) {
        this.scrap_recipe_owner = scrap_recipe_owner;
    }


}
