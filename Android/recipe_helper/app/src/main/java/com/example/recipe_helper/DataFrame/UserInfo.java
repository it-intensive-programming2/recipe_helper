package com.example.recipe_helper.DataFrame;

import java.io.Serializable;

import io.realm.RealmObject;

public class UserInfo extends RealmObject implements Serializable {
    public long id;
    public String nickName;
    public String email;
    public String profileImageUrl;
    public String gender;
    public String ageRange;
    public String accessToken;
    public String allergy;
    public String disease;
    public long expTime;

    public UserInfo() {
    }

    public UserInfo(long id, String nickName, String accessToken, String email, String profileImageUrl, String gender, String ageRange, long expTime, String allergy, String disease) {
        this.id = id;
        this.accessToken = accessToken;
        this.nickName = nickName;
        this.email = email;
        this.gender = gender;
        this.ageRange = ageRange;
        this.expTime = expTime;
        this.profileImageUrl = profileImageUrl;
        this.allergy = allergy;
        this.disease = disease;
    }
}
