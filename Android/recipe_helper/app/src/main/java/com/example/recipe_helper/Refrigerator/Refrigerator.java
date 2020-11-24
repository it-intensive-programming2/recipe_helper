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


        //Todo Helper 의 솔루션

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