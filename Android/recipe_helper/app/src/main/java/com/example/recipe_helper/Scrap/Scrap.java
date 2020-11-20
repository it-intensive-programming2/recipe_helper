package com.example.recipe_helper.Scrap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.recipe_helper.Home.Adapter.HomeRecyclerViewAdapter;
import com.example.recipe_helper.Home.Adapter.ViewPageAdapter;
import com.example.recipe_helper.Home.Dataframe.HomeRecipeFrame;
import com.example.recipe_helper.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Scrap extends Fragment implements ScrapRecyclerViewAdapter.OnListItemSelectedInterface {

    private ArrayList<like_recipe> likes = new ArrayList<>();
    private ScrapRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scrap_, container, false);
        view.setClickable(true);

        //Temp data
        String BaseUrl = "https://www.10000recipe.com/recipe/";
        HomeRecipeFrame frame1 = new HomeRecipeFrame("https://recipe1.ezmember.co.kr/cache/recipe/2020/08/28/c37e0db0e0386d453eda2be45e26b9c01.jpg", "닭봉간장조림~ 십년째 만들어 먹는 양념 공유해요!", BaseUrl + "6940325");
        HomeRecipeFrame frame2 = new HomeRecipeFrame("https://recipe1.ezmember.co.kr/cache/recipe/2020/10/29/89b11b0ce7f7e177f3f449546dc4edc11.jpg", "여러가지 매력을 가진 치즈감자호떡", BaseUrl + "6945267");
        HomeRecipeFrame frame3 = new HomeRecipeFrame("https://recipe1.ezmember.co.kr/cache/recipe/2019/05/25/39ac1e73e998e88da300d38663242f0a1.jpg", "버섯으로 관자 느낌 내는 방법! 새송이버섯간장버터구이 만들기", BaseUrl + "6912734");
        HomeRecipeFrame frame4 = new HomeRecipeFrame("https://recipe1.ezmember.co.kr/cache/recipe/2018/08/17/d1d0c5999686a5ec11e426b79abf4a1a1.jpg", "고기처럼 쫄깃한 밥도둑 반찬 '새송이버섯 간장버터구이'레시피", BaseUrl + "6894323");
        final HomeRecipeFrame frame5 = new HomeRecipeFrame("https://recipe1.ezmember.co.kr/cache/recipe/2017/12/18/58dd1a9a0d0ef8ff0c60dc35965ea09d1.jpg", "완전맛있는 차돌박이찜! 미소된장마요소스도 함께~", BaseUrl + "6881099");
        final HomeRecipeFrame frame6 = new HomeRecipeFrame("https://recipe1.ezmember.co.kr/cache/recipe/2017/07/10/092788fb72da830f79991bde7d9c68831.jpg", "새우요리 갈릭마요새우~ 요거 완전 맛있지~", BaseUrl + "6872474");
        HomeRecipeFrame frame7 = new HomeRecipeFrame("https://recipe1.ezmember.co.kr/cache/recipe/2020/02/01/c559f7fe91e19d4d9fbe0d7e88e768fa1.jpg", "즉석 목살 양념구이~ 달큰한 양념소스가 정말 맛있습니다.", BaseUrl + "6925795");
        HomeRecipeFrame frame8 = new HomeRecipeFrame("https://recipe1.ezmember.co.kr/cache/recipe/2020/08/24/4beccfef39cf9dcdd9a016819ce013051.jpg", "쫄깃 매콤한 백종원 느타리 두루치기", BaseUrl + "6939980");
        HomeRecipeFrame frame9 = new HomeRecipeFrame("https://recipe1.ezmember.co.kr/cache/recipe/2019/07/01/097a119634a9657ee34d81b011a97b131.jpg", "밥도둑 반찬 고추장 달걀조림 만들기!TV 알토란 레시피", BaseUrl + "6915088");
        HomeRecipeFrame frame10 = new HomeRecipeFrame("https://recipe1.ezmember.co.kr/cache/recipe/2018/11/03/f700f1964219a06e73be2c8d314ab4641.jpg", "해물찜처럼 맛있는 백종원 소시지콩나물찜", BaseUrl + "6899265");

        likes.add(new like_recipe(frame1.img_url, frame1.recipe_name, "작성자", frame1.recipe_url));
        likes.add(new like_recipe(frame2.img_url, frame2.recipe_name, "작성자", frame2.recipe_url));
        likes.add(new like_recipe(frame3.img_url, frame3.recipe_name, "작성자", frame3.recipe_url));
        likes.add(new like_recipe(frame4.img_url, frame4.recipe_name, "작성자", frame4.recipe_url));
        likes.add(new like_recipe(frame5.img_url, frame5.recipe_name, "작성자", frame5.recipe_url));
        likes.add(new like_recipe(frame6.img_url, frame6.recipe_name, "작성자", frame6.recipe_url));
        likes.add(new like_recipe(frame7.img_url, frame7.recipe_name, "작성자", frame7.recipe_url));
        likes.add(new like_recipe(frame8.img_url, frame8.recipe_name, "작성자", frame8.recipe_url));
        likes.add(new like_recipe(frame9.img_url, frame9.recipe_name, "작성자", frame9.recipe_url));
        likes.add(new like_recipe(frame10.img_url, frame10.recipe_name, "작성자", frame10.recipe_url));

        adapter = new ScrapRecyclerViewAdapter(getContext(), likes, this);

        RecyclerView recyclerView1 = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView1.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemSelected(View v, int position) {
        likes.remove(position);
        adapter.notifyDataSetChanged();
    }
}
