package com.example.recipe_helper.Refrigerator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.recipe_helper.DataFrame.IngredientData;
import com.example.recipe_helper.DataFrame.RecipeData;
import com.example.recipe_helper.DataFrame.RecipeResponse;
import com.example.recipe_helper.Home.Adapter.ViewPageAdapter;
import com.example.recipe_helper.Home.Dataframe.DoubleHomeRecipeFrame;
import com.example.recipe_helper.Home.WebViewFragment;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;
import com.example.recipe_helper.RecipeRecyclerAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;

public class Refrigerator extends Fragment implements RecipeRecyclerAdapter.OnListItemSelectedInterface {

    private RecipeRecyclerAdapter adapter;
    private ArrayList<RecipeData> list = new ArrayList<>();

    private IngredientRecyclerViewAdapter2 ingredientAdapter;
    private ArrayList<IngredientData> ingredientList = new ArrayList<>();

    private Realm realm;

    private Toolbar toolbar;
    private String TAG = "RHC";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refrigerator_, container, false);
        view.setClickable(true);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("나의 냉장고");

        realm = Realm.getDefaultInstance();

        ingredientAdapter = new IngredientRecyclerViewAdapter2(getContext(), ingredientList);

        RecyclerView ingredientRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_ingredient);
        ingredientRecyclerView.setHasFixedSize(true);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);

        ingredientRecyclerView.setLayoutManager(layoutManager);
        ingredientRecyclerView.setAdapter(ingredientAdapter);


        adapter = new RecipeRecyclerAdapter(getContext(), list, this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentAddBackStack(new Refrigerator_ingredient());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserRecommendRecipe();
        loadIngredient();
    }

    @Override
    public void onItemSelected(View v, int recipeID) {
        ((MainActivity) getActivity()).replaceFragmentFull(new WebViewFragment(String.valueOf(recipeID)));
    }

    void loadIngredient() {
        final RealmResults<IngredientData> results = realm.where(IngredientData.class).findAll();
        ingredientList.clear();
        ingredientList.addAll(realm.copyFromRealm(results));
        ingredientAdapter.notifyDataSetChanged();
    }

    private void loadUserRecommendRecipe() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<RecipeResponse> call = service.loadRecommendRecipe2();

        call.enqueue(new retrofit2.Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, retrofit2.Response<RecipeResponse> response) {
                if (response.isSuccessful()) {
                    RecipeResponse result = response.body();
                    list.clear();
                    list.addAll(result.body);
                    adapter.notifyDataSetChanged();
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
}