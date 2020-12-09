package com.example.recipe_helper.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.recipe_helper.DataFrame.RecipeData;
import com.example.recipe_helper.DataFrame.RecipeResponse;
import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.Home.Adapter.HomeRecyclerViewAdapter;
import com.example.recipe_helper.Home.Adapter.ViewPageAdapter;
import com.example.recipe_helper.Home.Dataframe.DoubleHomeRecipeFrame;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.kakao.usermgmt.response.model.User;

import java.util.ArrayList;

import retrofit2.Call;

public class Home extends Fragment implements ViewPageAdapter.OnListItemSelectedInterface, HomeRecyclerViewAdapter.OnListItemSelectedInterface {

    private static final String TAG = "RHC";
    private ViewPageAdapter pageAdapter;
    private HomeRecyclerViewAdapter adapter1;
    private HomeRecyclerViewAdapter adapter2;

    private ArrayList<RecipeData> hot_list = new ArrayList<>();
    private ArrayList<RecipeData> list1 = new ArrayList<>();
    private ArrayList<DoubleHomeRecipeFrame> dList1 = new ArrayList<>();
    private ArrayList<RecipeData> list2 = new ArrayList<>();
    private ArrayList<DoubleHomeRecipeFrame> dList2 = new ArrayList<>();

    private UserInfo user;

    private int list1_index = 2;
    private int list2_index = 2;
    private TextView title1;
    private TextView title2;
    private TextView ttitle1;
    private TextView ttitle2;

    private ShimmerFrameLayout shimmerFrameLayout;
    private RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        view.setClickable(true);
        user = ((MainActivity) getActivity()).user;

        ttitle1 = view.findViewById(R.id.ttitle1);
        ttitle2 = view.findViewById(R.id.ttitle2);
        title1 = view.findViewById(R.id.home_title1);
        title2 = view.findViewById(R.id.home_title2);
        ttitle1.setText(String.format("'%s'님이 좋아하실 만한 레시피", user.nickName));
        title1.setText(String.format("'%s'님이 좋아하실 만한 레시피", user.nickName));

        String age;
        String gender;
        if (user.gender.equals("male")) gender = "남자";
        else gender = "여자";
        if (user.ageRange.length() > 3) {
            age = user.ageRange.substring(0, 2);
        } else {
            age = user.ageRange;
        }

        ttitle2.setText(String.format("%s대 %s가 좋아할 만한 레시피", age, gender));
        title2.setText(String.format("%s대 %s가 좋아할 만한 레시피", age, gender));

        relativeLayout = view.findViewById(R.id.relative_layout);
        relativeLayout.setVisibility(View.INVISIBLE);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);

        shimmerFrameLayout.startShimmer();

        dList1.clear();
        dList2.clear();

        //Todo Hot 레시피
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);

        pageAdapter = new ViewPageAdapter(getContext(), hot_list, this);
        viewPager.setAdapter(pageAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

        //Todo Recommend 1
        adapter1 = new HomeRecyclerViewAdapter(getContext(), dList1, this);

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
                dList1.add(new DoubleHomeRecipeFrame(list1.get(list1_index), list1.get(list1_index + 1)));
                list1_index += 2;
                adapter1.notifyDataSetChanged();
                if (dList1.size() > 2) {
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
                ((MainActivity) getActivity()).replaceFragmentFull(new Recommend(list1, title1.getText().toString()));
            }
        });

        //Todo Recommend 2
        adapter2 = new HomeRecyclerViewAdapter(getContext(), dList2, this);
        RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recycler_view2);
        recyclerView2.setHasFixedSize(true);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setAdapter(adapter2);

        final ImageView btn_recommend2 = view.findViewById(R.id.btn_recommend2);
        final TextView more_info2 = view.findViewById(R.id.more_info2);
        btn_recommend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dList2.add(new DoubleHomeRecipeFrame(list2.get(list2_index), list2.get(list2_index + 1)));
                list2_index += 2;
                adapter2.notifyDataSetChanged();
                if (dList2.size() > 2) {
                    btn_recommend2.setVisibility(View.GONE);
                    more_info2.setVisibility(View.VISIBLE);
                } else {
                    btn_recommend2.setVisibility(View.VISIBLE);
                    more_info2.setVisibility(View.GONE);
                }
            }
        });
        more_info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentFull(new Recommend(list2, title2.getText().toString()));
            }
        });

        loadHotRecipe();
        loadRecommendRecipe1();
        loadRecommendRecipe2();

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

    private void loadHotRecipe() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<RecipeResponse> call = service.loadHotRecipe();

        call.enqueue(new retrofit2.Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, retrofit2.Response<RecipeResponse> response) {
                if (response.isSuccessful()) {
                    RecipeResponse result = response.body();
                    hot_list.clear();
                    hot_list.addAll(result.body);
                    pageAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadRecommendRecipe1() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<RecipeResponse> call = service.loadRecommendRecipe1(user.userID);

        call.enqueue(new retrofit2.Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, retrofit2.Response<RecipeResponse> response) {
                if (response.isSuccessful()) {
                    RecipeResponse result = response.body();
                    list1.clear();
                    list1.addAll(result.body);
                    dList1.add(new DoubleHomeRecipeFrame(list1.get(0), list1.get(1)));
                    adapter1.notifyDataSetChanged();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadRecommendRecipe2() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());

        Call<RecipeResponse> call = service.loadRecommendRecipe2(Integer.parseInt(user.ageRange.substring(0, 2)));

        call.enqueue(new retrofit2.Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, retrofit2.Response<RecipeResponse> response) {
                if (response.isSuccessful()) {
                    RecipeResponse result = response.body();
                    list2.clear();
                    list2.addAll(result.body);
                    dList2.add(new DoubleHomeRecipeFrame(list2.get(0), list2.get(1)));
                    adapter2.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onItemSelected(View v, int recipeID, int classNum) {
        ((MainActivity) getActivity()).replaceFragmentFull(new WebViewFragment(String.valueOf(recipeID), classNum));
    }
}
