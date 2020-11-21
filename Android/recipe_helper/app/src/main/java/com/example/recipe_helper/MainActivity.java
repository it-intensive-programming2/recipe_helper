package com.example.recipe_helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.Home.Home;
import com.example.recipe_helper.HttpConnection.BaseResponse;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.Login.SignUp1;
import com.example.recipe_helper.Mypage.MyPage;
import com.example.recipe_helper.Refrigerator.Refrigerator;
import com.example.recipe_helper.Scrap.Scrap;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.kakao.usermgmt.response.model.User;

import java.security.MessageDigest;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    public UserInfo user;

    private BottomNavigationView navigation;
    private Home home;
    private MyPage my_page;
    private Refrigerator refrigerator;
    private Scrap scrap;

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

        Intent intent = getIntent();
        user = (UserInfo) intent.getSerializableExtra("OBJECT");

        Log.d(TAG, "userinfo" + user.id);

        checkUser();

        home = new Home();
        refrigerator = new Refrigerator();
        scrap = new Scrap();
        my_page = new MyPage();

        navigation = findViewById(R.id.nav_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.item_home);

        getAppKeyHash();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_main, fragment);
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate();
        transaction.commit();
    }

    public void replaceFragmentFull(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void replaceSignUp(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate();
        transaction.commit();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashkey = new String(Base64.encode(md.digest(), 0));
                Log.d("HASH", "getAppKeyHash: " + hashkey);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    public void checkUser() {
        RetrofitService service = RetrofitAdapter.getInstance(this);
        Call<BaseResponse> call = service.checkUser(user.id);

        call.enqueue(new retrofit2.Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse result = response.body();
                    if (result.checkError(getApplicationContext()) == 3) {
                        Log.d("RHC", "not registerd");
                        replaceSignUp(new SignUp1(user, 0));
                    }
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}