package com.example.recipe_helper.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.R;
import com.example.recipe_helper.databinding.Signup1Binding;

public class SignUp1 extends Fragment {

    private UserInfo user;
    private Signup1Binding vi;

    public SignUp1(UserInfo user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vi = DataBindingUtil.inflate(inflater, R.layout.signup1, container, false);
        vi.getRoot().setClickable(true);

        vi.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StartActivity) getActivity()).replaceFragmentFull(new SignUp2());
            }
        });
        return vi.getRoot();
    }
}
