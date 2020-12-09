package com.example.recipe_helper.Login;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;
import com.example.recipe_helper.databinding.Signup1Binding;

public class SignUp1 extends Fragment {

    private UserInfo user;
    private Signup1Binding vi;

    private String allergy = "";
    private String disease = "";

    private int is_first;
    private boolean is_null;

    public SignUp1(UserInfo user, int is_first, boolean is_null) {
        this.user = user;
        this.is_first = is_first;
        this.is_null = is_null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vi = DataBindingUtil.inflate(inflater, R.layout.signup1, container, false);
        vi.getRoot().setClickable(true);

        if (is_null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("정보 부족");
            builder.setMessage("나이정보 혹은 성별 정보가 없는 경우 랜덤하게 나이가 적용됩니다.");
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
        vi.userNickName.setText(user.nickName);
        vi.userEmail.setText(user.email);
        if (user.gender.equals("male"))
            vi.userGender.setText("남성");
        else
            vi.userGender.setText("여성");
        vi.userAgeRange.setText(user.ageRange + "세");

        vi.ckHBP0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vi.ckHBP1.setChecked(false);
                vi.ckHBP2.setChecked(false);
            }
        });
        vi.ckHBP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vi.ckHBP0.setChecked(false);
                vi.ckHBP2.setChecked(false);
            }
        });
        vi.ckHBP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vi.ckHBP0.setChecked(false);
                vi.ckHBP1.setChecked(false);
            }
        });

        vi.ckDiabetes0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vi.ckDiabetes1.setChecked(false);
                vi.ckDiabetes2.setChecked(false);
            }
        });
        vi.ckDiabetes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vi.ckDiabetes0.setChecked(false);
                vi.ckDiabetes2.setChecked(false);
            }
        });
        vi.ckDiabetes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vi.ckDiabetes0.setChecked(false);
                vi.ckDiabetes1.setChecked(false);
            }
        });

        vi.ckHyperlipidemia0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vi.ckHyperlipidemia1.setChecked(false);
                vi.ckHyperlipidemia2.setChecked(false);
            }
        });
        vi.ckHyperlipidemia1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vi.ckHyperlipidemia0.setChecked(false);
                vi.ckHyperlipidemia2.setChecked(false);
            }
        });
        vi.ckHyperlipidemia2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vi.ckHyperlipidemia0.setChecked(false);
                vi.ckHyperlipidemia1.setChecked(false);
            }
        });

        vi.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAllergeString();
                makeDiseaseString();
                if (is_first == 1) {
                    ((StartActivity) getActivity()).replaceFragmentFull(new SignUp2(user, allergy, disease));
                } else {
                    ((MainActivity) getActivity()).replaceFragmentFull(new SignUp2(user, allergy, disease));
                }

            }
        });
        return vi.getRoot();
    }

    public void makeAllergeString() {
        if (vi.checkbox.ck0.isChecked()) allergy += "1";
        else allergy += "0";
        if (vi.checkbox.ck1.isChecked()) allergy += "1";
        else allergy += "0";
        if (vi.checkbox.ck2.isChecked()) allergy += "1";
        else allergy += "0";
        if (vi.checkbox.ck3.isChecked()) allergy += "1";
        else allergy += "0";
        if (vi.checkbox.ck4.isChecked()) allergy += "1";
        else allergy += "0";
        if (vi.checkbox.ck5.isChecked()) allergy += "1";
        else allergy += "0";
        if (vi.checkbox.ck6.isChecked()) allergy += "1";
        else allergy += "0";
        if (vi.checkbox.ck7.isChecked()) allergy += "1";
        else allergy += "0";
        if (vi.checkbox.ck8.isChecked()) allergy += "1";
        else allergy += "0";
        if (vi.checkbox.ck9.isChecked()) allergy += "1";
        else allergy += "0";
    }

    public void makeDiseaseString() {
        if (vi.ckHBP0.isChecked()) disease += "0";
        else if (vi.ckHBP1.isChecked()) disease += "1";
        else if (vi.ckHBP2.isChecked()) disease += "2";

        if (vi.ckDiabetes0.isChecked()) disease += "0";
        else if (vi.ckDiabetes1.isChecked()) disease += "1";
        else if (vi.ckDiabetes2.isChecked()) disease += "2";

        if (vi.ckHyperlipidemia0.isChecked()) disease += "0";
        else if (vi.ckHyperlipidemia1.isChecked()) disease += "1";
        else if (vi.ckHyperlipidemia2.isChecked()) disease += "2";
    }
}