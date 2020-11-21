package com.example.recipe_helper.Mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recipe_helper.R;

public class MyPage extends Fragment {
    private int user_state = 0;
    private ImageView iv;
    private ImageView iv2;
    private ImageView iv3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage, container, false);
        view.setClickable(true);
        final int[] ImageId = {R.drawable.ic_sad, R.drawable.ic_neutral, R.drawable.ic_smiling};


        iv = (ImageView) view.findViewById(R.id.HBP1);
        iv2 = (ImageView) view.findViewById(R.id.HBP2);
        iv3 = (ImageView) view.findViewById(R.id.HBP3);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_state++;
                if (user_state > 2) {
                    user_state = 0;
                }
                iv.setImageResource(ImageId[user_state]);
            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_state++;
                if (user_state > 2) {
                    user_state = 0;
                }
                iv2.setImageResource(ImageId[user_state]);
            }
        });

        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_state++;
                if (user_state > 2) {
                    user_state = 0;
                }
                iv3.setImageResource(ImageId[user_state]);
            }
        });

        //Todo Something
        return view;
    }

}
