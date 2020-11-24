package com.example.recipe_helper.Mypage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.recipe_helper.DataFrame.RecipeResponse;
import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.DataFrame.UserInfoResponse;
import com.example.recipe_helper.HttpConnection.BaseResponse;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;
import com.example.recipe_helper.databinding.MypageBinding;

import retrofit2.Call;

public class MyPage extends Fragment {
    private static final String TAG = MyPage.class.getName();
    private int disease_1;
    private int disease_2;
    private int disease_3;
    private ImageView iv;
    private ImageView iv2;
    private ImageView iv3;
    private TextView allergybox;

    private static final String[] allergy = {"계란", "우유", "밀", "갑각류", "생선", "호두", "돼지고기", "땅콩", "조개", "복숭아"};
    private UserInfo user;
    static boolean[] cur_user_allergy_info = new boolean[10];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage, container, false);
        view.setClickable(true);

        final int[] ImageId = {R.drawable.ic_smiling, R.drawable.ic_neutral, R.drawable.ic_sad};
        user = ((MainActivity) getActivity()).user;

        ImageView profile = view.findViewById(R.id.profile);
        TextView username = view.findViewById(R.id.user_name);
        TextView useremail = view.findViewById(R.id.user_email);
        TextView userage = view.findViewById(R.id.user_age);
        TextView usergender = view.findViewById(R.id.user_gender);
        allergybox = view.findViewById(R.id.allergybox);
        TextView favor_user_name = view.findViewById(R.id.mypage_user_id);
        Button edit_btn = view.findViewById(R.id.editbutton);

        for (int i = 0; i < user.allergy.length(); i++)
            if (user.allergy.charAt(i) == '1') cur_user_allergy_info[i] = true;
        displayAllergy();

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("선택").setMultiChoiceItems(allergy, cur_user_allergy_info,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                cur_user_allergy_info[which] = isChecked;
                            }
                        })
                        .setNeutralButton("closed", null).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String allergy_string = "";
                        for (int i = 0; i < cur_user_allergy_info.length; i++) {
                            if (cur_user_allergy_info[i] == true) {
                                allergy_string += "1";
                            } else {
                                allergy_string += "0";
                            }
                        }
                        changeUserInfo(user.userID, allergy_string, "");
                        displayAllergy();
                    }
                }).setNegativeButton("cancel", null).show();

            }
        });

        iv = (ImageView) view.findViewById(R.id.HBP1);
        iv2 = (ImageView) view.findViewById(R.id.HBP2);
        iv3 = (ImageView) view.findViewById(R.id.HBP3);

        disease_1 = Character.getNumericValue(user.disease.charAt(0));
        disease_2 = Character.getNumericValue(user.disease.charAt(1));
        disease_3 = Character.getNumericValue(user.disease.charAt(2));


        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);
        Glide.with(this).load(user.profileUrl).into(profile);
        username.setText(user.nickName);
        favor_user_name.setText(user.nickName + "님의 취향분석");
        useremail.setText(user.email);
        userage.setText(user.ageRange + "대");
        usergender.setText(user.gender);


        iv.setImageResource(ImageId[disease_1]);
        iv2.setImageResource(ImageId[disease_2]);
        iv3.setImageResource(ImageId[disease_3]);


        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disease_1++;

                if (disease_1 > 2) {
                    disease_1 = 0;
                }

                iv.setImageResource(ImageId[disease_1]);
                changeUserInfo(user.userID, "", String.format("%d%d%d", disease_1, disease_2, disease_3));
            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disease_2++;
                if (disease_2 > 2) {
                    disease_2 = 0;
                }

                iv2.setImageResource(ImageId[disease_2]);

                changeUserInfo(user.userID, "", String.format("%d%d%d", disease_1, disease_2, disease_3));
            }
        });

        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disease_3++;
                if (disease_3 > 2) {
                    disease_3 = 0;
                }

                iv3.setImageResource(ImageId[disease_3]);
                changeUserInfo(user.userID, "", String.format("%d%d%d", disease_1, disease_2, disease_3));
            }
        });


        return view;
    }

    private void changeUserInfo(long userID, String allergy, String disease) {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<UserInfoResponse> call = service.changeUserInfo(userID, allergy, disease);

        call.enqueue(new retrofit2.Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, retrofit2.Response<UserInfoResponse> response) {
                if (response.isSuccessful()) {
                    UserInfoResponse result = response.body();
                    ((MainActivity) getActivity()).user = result.body;
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void displayAllergy() {
        String allergy_info = "";

        for (int i = 0; i < cur_user_allergy_info.length; i++) {
            if (cur_user_allergy_info[i] == true)
                allergy_info += (allergy[i] + " ");
            if ((i != 0) && (i % 3 == 0)) allergy_info += '\n';
        }

        if (allergy_info.trim().length() == 0) allergy_info = "알레르기가 없습니다.";
        allergybox.setText(allergy_info);
    }

}
