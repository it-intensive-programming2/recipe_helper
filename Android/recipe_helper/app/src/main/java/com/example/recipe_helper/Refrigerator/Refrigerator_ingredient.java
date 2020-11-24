package com.example.recipe_helper.Refrigerator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_helper.DataFrame.IngredientData;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class Refrigerator_ingredient extends Fragment {

    private IngredientRecyclerViewAdapter adapter;
    private ArrayList<IngredientData> list = new ArrayList<>();

    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refrigerator_add, container, false);
        view.setClickable(true);

        realm = Realm.getDefaultInstance();

        readRealm();

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

    void readRealm() {
        final RealmResults<IngredientData> results = realm.where(IngredientData.class).findAll();
        list.clear();
        list.addAll(realm.copyFromRealm(results));
        Log.d("NDH", "readRealm: " + list.size());

//        realm형태의 리스트를 arraylist로 바꿔주는 작업
//        ArrayList<IngredientData> tmp = new ArrayList<>();
//        tmp = (ArrayList<IngredientData>) realm.copyFromRealm(results);

    }
}
