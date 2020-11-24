package com.example.recipe_helper.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recipe_helper.DataFrame.RecipeData;
import com.example.recipe_helper.R;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getName();
    private String recipe_name;
    private ArrayList<RecipeData> list;

    public SearchFragment(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_search_fragment, container, false);
        view.setClickable(true);

        TextView tv_input = view.findViewById(R.id.tv_input);
        tv_input.setText(recipe_name);
        tv_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        return view;
    }

//    private void search(String query) {
//        list.clear();
//        if (query.equals("")) {
//            adapter.notifyDataSetChanged();
//            return;
//        }
//
//        RetrofitService service = RetrofitAdapter.getInstance(getContext());
//        Call<RecipeResponse> call = service.searchRecipe(query);
//
//        call.enqueue(new retrofit2.Callback<RecipeResponse>() {
//            @Override
//            public void onResponse(Call<RecipeResponse> call, retrofit2.Response<RecipeResponse> response) {
//                if (response.isSuccessful()) {
//                    RecipeResponse result = response.body();
//                    list.clear();
//                    list.addAll(result.body);
//                    adapter.notifyDataSetChanged();
//                } else {
//                    Log.d(TAG, "onResponse: Fail " + response.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RecipeResponse> call, Throwable t) {
//                Log.d(TAG, "onFailure: " + t.getMessage());
//            }
//        });
//    }
}
