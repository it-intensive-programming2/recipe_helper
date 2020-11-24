package com.example.recipe_helper.Scrap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_helper.DataFrame.RecipeData;
import com.example.recipe_helper.DataFrame.RecipeResponse;
import com.example.recipe_helper.Home.WebViewFragment;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;

import java.util.ArrayList;

import retrofit2.Call;

public class Scrap extends Fragment implements ScrapRecyclerViewAdapter.OnListItemSelectedInterface {

    private static final String TAG = Scrap.class.getName();
    private ArrayList<RecipeData> likes = new ArrayList<>();
    private ScrapRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scrap_, container, false);
        view.setClickable(true);

        loadMyScrap();

        adapter = new ScrapRecyclerViewAdapter(getContext(), likes, this);

        RecyclerView recyclerView1 = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView1.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemSelected2(View v, int recipeID) {
        ((MainActivity) getActivity()).replaceFragmentFull(new WebViewFragment(String.valueOf(recipeID)));
    }

    @Override
    public void onItemSelected(View v, int position) {
        likes.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void loadMyScrap() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<RecipeResponse> call = service.loadRecommendRecipe3();

        call.enqueue(new retrofit2.Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, retrofit2.Response<RecipeResponse> response) {
                if (response.isSuccessful()) {
                    RecipeResponse result = response.body();
                    likes.clear();
                    likes.addAll(result.body);
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
