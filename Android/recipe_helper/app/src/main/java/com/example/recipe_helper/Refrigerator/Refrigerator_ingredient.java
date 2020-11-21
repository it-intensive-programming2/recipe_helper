package com.example.recipe_helper.Refrigerator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.recipe_helper.DataFrame.IngredientData;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Refrigerator_ingredient extends Fragment {

    private IngredientRecyclerViewAdapter adapter;
    private ArrayList<IngredientData> list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refrigerator_add, container, false);
        view.setClickable(true);
        list.clear();


        IngredientData frame1 = new IngredientData("사과");
        IngredientData frame2 = new IngredientData("양파");
        IngredientData frame3 = new IngredientData("감자");
        IngredientData frame4 = new IngredientData("고구마");
        IngredientData frame5 = new IngredientData("소고기");

        list.add(frame1);
        list.add(frame2);
        list.add(frame3);
        list.add(frame4);
        list.add(frame5);

        adapter = new IngredientRecyclerViewAdapter(getContext(), list);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_ingredient);
        recyclerView.setHasFixedSize(true);

        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        int numberOfColumns = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        TextView textView = (TextView) view.findViewById(R.id.search_box);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentFull(new Refrigerator_search());
            }
        });

        return view;
    }
}
