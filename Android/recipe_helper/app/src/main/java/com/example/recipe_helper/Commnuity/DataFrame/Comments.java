package com.example.recipe_helper.Commnuity.DataFrame;

public class Comments {
    private String user_id;
    private String user_img;
    private String content;

    public Comments(String user_id, String user_img, String content) {
        this.user_id = user_id;
        this.user_img = user_img;
        this.content = content;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
