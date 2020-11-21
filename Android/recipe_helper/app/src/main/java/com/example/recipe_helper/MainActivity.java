package com.example.recipe_helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.recipe_helper.Commnuity.Community_comment;
import com.example.recipe_helper.Commnuity.Community_main;
import com.example.recipe_helper.Home.Home;
import com.example.recipe_helper.Mypage.MyPage;
import com.example.recipe_helper.Refrigerator.Refrigerator;
import com.example.recipe_helper.Scrap.Scrap;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private Home home;
    private MyPage my_page;
    private Refrigerator refrigerator;
    private Scrap scrap;
    private Community_main community_main;
    private Community_comment community_comment;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.item_home:
                    replaceFragment(home);
                    return true;
                case R.id.item_fridge:
                    replaceFragment(refrigerator);
                    return true;
                case R.id.item_scrap:
                    replaceFragment(scrap);
                    return true;
                case R.id.item_community_main:
                    replaceFragment(community_main);
                    return true;
                case R.id.item_mypage:
                    replaceFragment(my_page);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home = new Home();
        refrigerator = new Refrigerator();
        scrap = new Scrap();
        my_page = new MyPage();
        community_main = new Community_main();
        community_comment = new Community_comment();

        navigation = findViewById(R.id.nav_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.item_home);

    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.fragment_main, fragment);
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate();
        transaction.commit();
    }

    public void replaceFragmentFull(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}