package com.example.recipe_helper.DataFrame;

import java.io.Serializable;

import io.realm.RealmObject;

public class UserInfo extends RealmObject implements Serializable {
    public long userID;
    public String nickName;
    public String email;
    public String profileUrl;
    public String gender;
    public String ageRange;
    public String accessToken;
    public String allergy;
    public String disease;
    public long expTime;

    public UserInfo() {
    }

    public UserInfo(long userID, String nickName, String accessToken, String email, String profileUrl, String gender, String ageRange, long expTime, String allergy, String disease) {
        this.userID = userID;
        this.accessToken = accessToken;
        this.nickName = nickName;
        this.email = email;
        this.gender = gender;
        this.ageRange = ageRange;
        this.expTime = expTime;
        this.profileUrl = profileUrl;
        this.allergy = allergy;
        this.disease = disease;
    }
}
