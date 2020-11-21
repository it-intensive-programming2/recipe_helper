package com.example.recipe_helper.DataFrame;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LocalDB extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .name("UserInfo.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
