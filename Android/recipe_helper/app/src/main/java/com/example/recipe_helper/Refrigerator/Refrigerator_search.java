package com.example.recipe_helper.Refrigerator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recipe_helper.DataFrame.IngredientData;
import com.example.recipe_helper.R;

import io.realm.Realm;

public class Refrigerator_search extends Fragment {

    private Realm realm;

    private EditText et_input;
    private ImageView search_icon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refrigerator_search, container, false);
        view.setClickable(true);

        realm = Realm.getDefaultInstance();

        et_input = view.findViewById(R.id.et_input);
        search_icon = view.findViewById(R.id.search_icon);

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_input.getText().toString().equals("")) et_input.setError("재료를 입력하세요");
                else {
                    addIngredient(et_input.getText().toString());
                }
            }
        });

        return view;
    }

    public void addIngredient(final String name) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                IngredientData ingredientData = realm.createObject(IngredientData.class);

                ingredientData.ig_name = name;
                ingredientData.ig_profile = "이미지";
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("NDH", "onSuccess: Realm");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("NDH", "onError: Realm");
            }
        });
    }
}
