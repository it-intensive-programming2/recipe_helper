package com.example.recipe_helper.Home;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.recipe_helper.Home.Adapter.SearchAdapter;
import com.example.recipe_helper.Home.Dataframe.HomeRecipeFrame;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;

import java.util.ArrayList;

import retrofit2.Call;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Search extends Fragment {
    private String input;

    public Search(String input) {
        this.input = input;
    }

    private EditText et_input;
    private ListView listView;
    private ArrayList<HomeRecipeFrame> list = new ArrayList<HomeRecipeFrame>();
    private SearchAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_search, container, false);
        view.setClickable(true);

        et_input = view.findViewById(R.id.et_input);
        et_input.setText(input);
        et_input.requestFocus();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        출처: https://alaveiw.tistory.com/133 [어허]

        listView = view.findViewById(R.id.listView);

        list.add(new HomeRecipeFrame("ul", "recipename~~", "recipeurl"));

        adapter = new SearchAdapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeRecipeFrame recipe = list.get(i);
                InputMethodManager imm = (InputMethodManager) ((MainActivity) getActivity()).getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
                replaceSearchFragment(new SearchFragment(recipe.recipe_name));
                et_input.clearFocus();
            }
        });
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
//                search(text);
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
//        RetrofitAdapter rAdapter = new RetrofitAdapter();
//        RetrofitService service = rAdapter.getInstance(getActivity());
//        Call<UnivResponse> call = service.searchCollageName(query);
//
//        call.enqueue(new retrofit2.Callback<UnivResponse>() {
//            @Override
//            public void onResponse(Call<UnivResponse> call, retrofit2.Response<UnivResponse> response) {
//                if (response.isSuccessful()) {
//                    UnivResponse result = response.body();
//                    list.clear();
//                    list.addAll(result.body);
//                    adapter.notifyDataSetChanged();
//                } else {
//                    Log.d(TAG, "onResponse: Fail " + response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UnivResponse> call, Throwable t) {
//                Log.d(TAG, "onFailure: " + t.getMessage());
//            }
//        });
//    }

    public void replaceSearchFragment(Fragment fragment) {
        FragmentTransaction transaction = ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}