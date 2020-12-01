package com.example.recipe_helper.Scrap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_helper.DataFrame.RecipeData;
import com.example.recipe_helper.DataFrame.RecipeResponse;
import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.Home.WebViewFragment;
import com.example.recipe_helper.HttpConnection.BaseResponse;
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
    private TextView none_text;
    private RecyclerView recyclerView;
    private UserInfo user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scrap_, container, false);
        view.setClickable(true);

        user = ((MainActivity) getActivity()).user;

        none_text = view.findViewById(R.id.none_text);
        loadMyScrap();

        adapter = new ScrapRecyclerViewAdapter(getContext(), likes, this);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemSelected2(View v, int recipeID) {
        ((MainActivity) getActivity()).replaceFragmentFull(new WebViewFragment(String.valueOf(recipeID)));
    }

    @Override
    public void onItemSelected(View v, int position, int recipeID) {
        ScrapRecyclerViewAdapter.Holder holder = (ScrapRecyclerViewAdapter.Holder) recyclerView.findViewHolderForAdapterPosition(position);
        if (holder.isChecked) {
            holder.scrapStar.setBackgroundResource(R.drawable.ic_icon_before_scrap);
            holder.isChecked = false;
        } else {
            holder.scrapStar.setBackgroundResource(R.drawable.ic_icon_after_scrap);
            holder.isChecked = true;
        }
        setScrap(recipeID);
    }

    private void setScrap(int recipeID) {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<BaseResponse> call = service.setScrap(user.userID, recipeID);

        call.enqueue(new retrofit2.Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse result = response.body();
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

    private void loadMyScrap() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<RecipeResponse> call = service.loadScrap(user.userID);

        call.enqueue(new retrofit2.Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, retrofit2.Response<RecipeResponse> response) {
                if (response.isSuccessful()) {
                    RecipeResponse result = response.body();
                    likes.clear();
                    likes.addAll(result.body);
                    adapter.notifyDataSetChanged();
                    if (likes.isEmpty()) {
                        none_text.setVisibility(View.VISIBLE);
                    }
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
