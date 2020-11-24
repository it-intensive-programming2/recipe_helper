package com.example.recipe_helper.Refrigerator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.recipe_helper.DataFrame.IngredientData;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

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
        setHasOptionsMenu(true);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        ((MainActivity) getActivity()).setNavigationVisible(false);

        realm = Realm.getDefaultInstance();

        adapter = new IngredientRecyclerViewAdapter(getContext(), list);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_ingredient);
        recyclerView.setHasFixedSize(true);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        TextView textView = (TextView) view.findViewById(R.id.search_box);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentAddBackStack(new Refrigerator_search());
            }
        });

        return view;
    }

    void loadIngredient() {
        final RealmResults<IngredientData> results = realm.where(IngredientData.class).findAll();
        list.clear();
        list.addAll(realm.copyFromRealm(results));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.write_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_post_check:
                getActivity().onBackPressed();
                Log.d("RHC", "onOptionsItemSelected: ");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).setNavigationVisible(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadIngredient();
    }
}
