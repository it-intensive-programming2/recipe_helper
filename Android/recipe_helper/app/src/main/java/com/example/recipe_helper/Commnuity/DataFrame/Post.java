package com.example.recipe_helper.Commnuity.DataFrame;

public class Post {
    private String writerNickname;
    private String title;
    private String writerProfileUrl;
    private int heart;
    private int comment;
    private int postID;
    private String content;

    public Post(String writerNickname, String title, String writerProfileUrl, int heart, int comment, int postID, String content) {
        this.writerNickname = writerNickname;
        this.title = title;
        this.writerProfileUrl = writerProfileUrl;
        this.heart = heart;
        this.comment = comment;
        this.postID = postID;
        this.content = content;
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

    public int getPostID() {
        return postID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
