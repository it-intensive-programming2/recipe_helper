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
    private String writerNickname;
    private String title;
    private String writerProfileUrl;
    private int heart;
    private int comment;
    private long postID;
    private String content;
    private String post_content;

    public Post(String writerNickname, String title, String writerProfileUrl, int heart, int comment, long postID, String content, String post_content) {
        this.writerNickname = writerNickname;
        this.title = title;
        this.writerProfileUrl = writerProfileUrl;
        this.heart = heart;
        this.comment = comment;
        this.postID = postID;
        this.content = content;
        this.post_content = post_content;
    }

    public String getWriterNickname() {
        return writerNickname;
    }

    public void setWriterNickname(String writerNickname) {
        this.writerNickname = writerNickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriterProfileUrl() {
        return writerProfileUrl;
    }

    public void setWriterProfileUrl(String writerProfileUrl) {
        this.writerProfileUrl = writerProfileUrl;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public long getPostID() {
        return postID;
    }

    public void setPostID(long postID) {
        this.postID = postID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }
}
