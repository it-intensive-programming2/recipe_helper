package com.example.recipe_helper.Refrigerator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_helper.DataFrame.RecipeData;
import com.example.recipe_helper.Home.Adapter.ViewPageAdapter;
import com.example.recipe_helper.Home.WebViewFragment;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Refrigerator extends Fragment implements RefrigeratorRecyclerViewAdapter.OnListItemSelectedInterface {

    private RefrigeratorRecyclerViewAdapter adapter;
    private ArrayList<RecipeData> list = new ArrayList<>();

    private Toolbar toolbar;
    private String TAG = "RHC";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refrigerator_, container, false);
        view.setClickable(true);
        list.clear();

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("나의 냉장고");

//        this.recipeName = recipeName;
//        this.ingredientName = ingredientName;
//        this.cookTime = cookTime;
//        this.photoURL = photoURL;
//        this.recipeURl = recipeURl;

        String BaseUrl = "https://www.10000recipe.com/recipe/";
        RecipeData frame1 = new RecipeData("https://recipe1.ezmember.co.kr/cache/recipe/2020/08/28/c37e0db0e0386d453eda2be45e26b9c01.jpg", "닭봉간장조림~ 십년째 만들어 먹는 양념 공유해요!", BaseUrl + "6940325", "15", "식재료");
        RecipeData frame2 = new RecipeData("https://recipe1.ezmember.co.kr/cache/recipe/2020/10/29/89b11b0ce7f7e177f3f449546dc4edc11.jpg", "여러가지 매력을 가진 치즈감자호떡", BaseUrl + "6945267", "15", "식재료");
        RecipeData frame3 = new RecipeData("https://recipe1.ezmember.co.kr/cache/recipe/2019/05/25/39ac1e73e998e88da300d38663242f0a1.jpg", "버섯으로 관자 느낌 내는 방법! 새송이버섯간장버터구이 만들기", BaseUrl + "6912734", "15", "식재료");
        RecipeData frame4 = new RecipeData("https://recipe1.ezmember.co.kr/cache/recipe/2018/08/17/d1d0c5999686a5ec11e426b79abf4a1a1.jpg", "고기처럼 쫄깃한 밥도둑 반찬 '새송이버섯 간장버터구이'레시피", BaseUrl + "6894323", "15", "식재료");
        final RecipeData frame5 = new RecipeData("https://recipe1.ezmember.co.kr/cache/recipe/2017/12/18/58dd1a9a0d0ef8ff0c60dc35965ea09d1.jpg", "완전맛있는 차돌박이찜! 미소된장마요소스도 함께~", BaseUrl + "6881099", "15", "식재료");
        final RecipeData frame6 = new RecipeData("https://recipe1.ezmember.co.kr/cache/recipe/2017/07/10/092788fb72da830f79991bde7d9c68831.jpg", "새우요리 갈릭마요새우~ 요거 완전 맛있지~", BaseUrl + "6872474", "15", "식재료");
//        RecipeData frame7 = new RecipeData("https://recipe1.ezmember.co.kr/cache/recipe/2020/02/01/c559f7fe91e19d4d9fbe0d7e88e768fa1.jpg", "즉석 목살 양념구이~ 달큰한 양념소스가 정말 맛있습니다.", BaseUrl + "6925795", "15", "식재료");
//        RecipeData frame8 = new RecipeData("https://recipe1.ezmember.co.kr/cache/recipe/2020/08/24/4beccfef39cf9dcdd9a016819ce013051.jpg", "쫄깃 매콤한 백종원 느타리 두루치기", BaseUrl + "6939980", "15", "식재료");
//        RecipeData frame9 = new RecipeData("https://recipe1.ezmember.co.kr/cache/recipe/2019/07/01/097a119634a9657ee34d81b011a97b131.jpg", "밥도둑 반찬 고추장 달걀조림 만들기!TV 알토란 레시피", BaseUrl + "6915088", "15", "식재료");
//        RecipeData frame10 = new RecipeData("https://recipe1.ezmember.co.kr/cache/recipe/2018/11/03/f700f1964219a06e73be2c8d314ab4641.jpg", "해물찜처럼 맛있는 백종원 소시지콩나물찜", BaseUrl + "6899265", "15", "식재료");

        //Todo Helper 의 솔루션

        list.add(frame1);
        list.add(frame2);
        list.add(frame3);
        list.add(frame4);
        list.add(frame5);
        list.add(frame6);
        adapter = new RefrigeratorRecyclerViewAdapter(getContext(), list, this);

        RecyclerView recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler_view1);
        recyclerView1.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentFull(new Refrigerator_ingredient());
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(View v, String recipe_url) {
        ((MainActivity) getActivity()).replaceFragmentFull(new WebViewFragment(recipe_url));
    }
}