package com.example.recipe_helper.DataFrame;

import io.realm.RealmObject;

public class IngredientData extends RealmObject {
    public String ig_profile;
    public String ig_name;

    public IngredientData(String ig_name) {
//        this.ig_profile = ig_profile;
        this.ig_name = ig_name;
    }

    public IngredientData(String ig_profile, String ig_name) {
        this.ig_profile = ig_profile;
        this.ig_name = ig_name;
    }

    public IngredientData() {
    }
}
