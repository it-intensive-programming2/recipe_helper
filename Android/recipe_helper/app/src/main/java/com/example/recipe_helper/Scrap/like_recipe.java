package com.example.recipe_helper.Scrap;

import android.widget.LinearLayout;

public class like_recipe {

    private int scrap_img; // 음식사진
    private int scrap_btn; // 별
    private String scrap_recipe_name; // 레시피 명
    private String scrap_recipe_owner; // 레시피 작성자
    private String link; // 링크

    public like_recipe(int scrap_img, int scrap_btn, String scrap_recipe_name, String scrap_recipe_owner, String link) {
        this.scrap_img = scrap_img;
        this.scrap_btn = scrap_btn;
        this.scrap_recipe_name = scrap_recipe_name;
        this.scrap_recipe_owner = scrap_recipe_owner;
        this.link =  link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getScrap_btn() {
        return scrap_btn;
    }

    public void setScrap_btn(int scrap_btn) {
        this.scrap_btn = scrap_btn;
    }

    public int getScrap_img() {
        return scrap_img;
    }

    public void setScrap_img(int scrap_img) {
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
