package com.example.recipe_helper.Commnuity.DataFrame;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.recipe_helper.R;

import java.util.ArrayList;

public class Post {
    private String post_title;
    private String user_img;
    private String img_url;



    private String user_id;
    private String post_content;
    private ArrayList<Comments> comments;

    public Post(String post_title, String img_url, String user_id, String post_content, ArrayList<Comments> comments, String user_img) {
        this.post_title = post_title;
        this.img_url = img_url;
        this.user_id = user_id;
        this.post_content = post_content;
        this.comments = comments;
        this.user_img = user_img;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public ArrayList<Comments> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comments> comments) {
        this.comments = comments;
    }
    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

}
