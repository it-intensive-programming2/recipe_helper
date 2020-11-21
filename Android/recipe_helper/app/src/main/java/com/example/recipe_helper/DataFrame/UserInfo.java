package com.example.recipe_helper.DataFrame;

import io.realm.RealmObject;

public class UserInfo extends RealmObject {
    public long userID;
    public String nickName;
    public String email;
    public String profileImageUrl;
    public String gender;
    public String ageRange;
    public String accessToken;
    public long expTime;

    public UserInfo() {
    }

    public UserInfo(long userID, String nickName, String accessToken, String email, String profileImageUrl, String gender, String ageRange, long expTime) {
        this.userID = userID;
        this.accessToken = accessToken;
        this.nickName = nickName;
        this.email = email;
        this.gender = gender;
        this.ageRange = ageRange;
        this.expTime = expTime;
        this.profileImageUrl = profileImageUrl;
    }
}
