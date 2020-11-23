package com.example.recipe_helper.Commnuity.DataFrame;

public class Comments {
    public String content;
    public int postID;
    public String writerNickname;
    public String writerProfileUrl;
    public int id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getWriterNickname() {
        return writerNickname;
    }

    public void setWriterNickname(String writerNickname) {
        this.writerNickname = writerNickname;
    }

    public String getWriterProfileUrl() {
        return writerProfileUrl;
    }

    public void setWriterProfileUrl(String writerProfileUrl) {
        this.writerProfileUrl = writerProfileUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Comments(String content, int postID, String writerNickname, String writerProfileUrl, int id) {
        this.content = content;
        this.postID = postID;
        this.writerNickname = writerNickname;
        this.writerProfileUrl = writerProfileUrl;
        this.id = id;


    }
}
