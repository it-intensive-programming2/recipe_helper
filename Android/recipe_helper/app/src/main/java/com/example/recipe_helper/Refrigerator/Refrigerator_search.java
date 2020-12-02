package com.example.recipe_helper.Refrigerator;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_helper.DataFrame.IngredientData;
import com.example.recipe_helper.DataFrame.IngredientResponse;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;

import java.util.ArrayList;

import io.realm.Realm;
import retrofit2.Call;

public class Refrigerator_search extends Fragment implements SearchAdapter.OnListItemSelectedInterface {

    private static final String TAG = Refrigerator_search.class.getName();
    private Realm realm;

    private EditText et_input;
    private ArrayList<IngredientData> list = new ArrayList<IngredientData>();
    private SearchAdapter adapter;
    private InputMethodManager imm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refrigerator_search, container, false);
        view.setClickable(true);

        realm = Realm.getDefaultInstance();

        et_input = view.findViewById(R.id.et_input);
        et_input.requestFocus();

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        adapter = new SearchAdapter(getContext(), list, this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setAdapter(adapter);

        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = et_input.getText().toString();
                search(text);
            }
        });

        return view;
    }

    private void search(String query) {
        list.clear();
        if (query.equals("")) {
            adapter.notifyDataSetChanged();
            return;
        }

        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<IngredientResponse> call = service.searchIngredient(query);

        call.enqueue(new retrofit2.Callback<IngredientResponse>() {
            @Override
            public void onResponse(Call<IngredientResponse> call, retrofit2.Response<IngredientResponse> response) {
                if (response.isSuccessful()) {
                    IngredientResponse result = response.body();
                    list.clear();
                    list.addAll(result.body);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<IngredientResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void addIngredient(final String name, final String image) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                IngredientData ingredientData = realm.createObject(IngredientData.class);

                ingredientData.name = name;
                ingredientData.image = image;
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
                getActivity().getSupportFragmentManager().popBackStack();
                Log.d(TAG, "onSuccess: Realm");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "onError: Realm");
            }
        });
    }

    @Override
    public void onItemSelected(View v, String ingredient_name, String image) {
        addIngredient(ingredient_name, image);
    }
}
