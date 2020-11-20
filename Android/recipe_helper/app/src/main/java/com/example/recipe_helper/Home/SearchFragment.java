package com.example.recipe_helper.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recipe_helper.R;

public class SearchFragment extends Fragment {

    private String recipe_name;

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
}
