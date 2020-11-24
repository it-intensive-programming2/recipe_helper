package com.example.recipe_helper.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_helper.DataFrame.RecipeData;
import com.example.recipe_helper.RecipeRecyclerAdapter;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements RecipeRecyclerAdapter.OnListItemSelectedInterface {

    private static final String TAG = SearchFragment.class.getName();
    private String recipe_name;
    private ArrayList<RecipeData> list;
    private RecipeRecyclerAdapter adapter;

    public SearchFragment(String recipe_name, ArrayList<RecipeData> list) {
        this.recipe_name = recipe_name;
        this.list = list;
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

        adapter = new RecipeRecyclerAdapter(getContext(), list, this);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void onItemSelected(View v, int recipeID) {
        ((MainActivity) getActivity()).replaceFragmentFull(new WebViewFragment(String.valueOf(recipeID)));
    }
}
