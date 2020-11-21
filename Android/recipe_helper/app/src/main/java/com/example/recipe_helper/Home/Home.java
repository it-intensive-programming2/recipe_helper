package com.example.recipe_helper.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.recipe_helper.Home.Adapter.HomeRecyclerViewAdapter;
import com.example.recipe_helper.Home.Adapter.ViewPageAdapter;
import com.example.recipe_helper.Home.Dataframe.DoubleHomeRecipeFrame;
import com.example.recipe_helper.Home.Dataframe.HomeRecipeFrame;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.Mypage.MyPage;
import com.example.recipe_helper.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Home extends Fragment implements ViewPageAdapter.OnListItemSelectedInterface, HomeRecyclerViewAdapter.OnListItemSelectedInterface {

    private ViewPageAdapter pageAdapter;
    private HomeRecyclerViewAdapter adapter1;
    private HomeRecyclerViewAdapter adapter2;

    private ArrayList<HomeRecipeFrame> hot_list = new ArrayList<>();
    private ArrayList<DoubleHomeRecipeFrame> list1 = new ArrayList<>();
    private ArrayList<DoubleHomeRecipeFrame> list2 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        view.setClickable(true);
        hot_list.clear();
        list1.clear();
        list2.clear();

        //Temp Data
//        HomeRecipeFrame frame = new HomeRecipeFrame("","",BaseUrl + "");

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

        //Todo Hot 레시피
        hot_list.add(frame1);
        hot_list.add(frame2);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);

        pageAdapter = new ViewPageAdapter(getContext(), hot_list, this);
        viewPager.setAdapter(pageAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

        //Todo Recommend 1
        list1.add(new DoubleHomeRecipeFrame(frame3, frame4));
        adapter1 = new HomeRecyclerViewAdapter(getContext(), list1, this);

        RecyclerView recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler_view1);
        recyclerView1.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setAdapter(adapter1);

        final ImageView btn_recommend1 = view.findViewById(R.id.btn_recommend1);
        final TextView more_info1 = view.findViewById(R.id.more_info1);
        btn_recommend1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list1.add(new DoubleHomeRecipeFrame(frame5, frame6));
                adapter1.notifyDataSetChanged();
                if (list1.size() > 2) {
                    btn_recommend1.setVisibility(View.GONE);
                    more_info1.setVisibility(View.VISIBLE);
                } else {
                    btn_recommend1.setVisibility(View.VISIBLE);
                    more_info1.setVisibility(View.GONE);
                }
            }
        });
        more_info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new MyPage());
            }
        });

        //Todo Recommend 2
        list2.add(new DoubleHomeRecipeFrame(frame7, frame8));
        list2.add(new DoubleHomeRecipeFrame(frame9, frame10));
        adapter2 = new HomeRecyclerViewAdapter(getContext(), list2, this);

        RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recycler_view2);
        recyclerView2.setHasFixedSize(true);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setAdapter(adapter2);

        //Todo Search
        CardView cardview_search = view.findViewById(R.id.cardview_search);
        cardview_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentFull(new Search(""));
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(View v, String recipe_url) {
        ((MainActivity) getActivity()).replaceFragmentFull(new WebViewFragment(recipe_url));
    }

}
