package com.example.recipe_helper.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.HttpConnection.BaseResponse;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;
import com.google.gson.JsonObject;

import retrofit2.Call;

public class SignUp2 extends Fragment {
    private static final String TAG = "RHC";
    private UserInfo user;
    private String allergyInfo;
    private String diseaseInfo;

    public SignUp2(UserInfo user, String allergyInfo, String diseaseInfo) {
        this.user = user;
        this.allergyInfo = allergyInfo;
        this.diseaseInfo = diseaseInfo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup2, container, false);
        view.setClickable(true);

        ImageView btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        return view;
    }

    public void signUp() {
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("id", user.id);
        paramObject.addProperty("nickName", user.nickName);
        paramObject.addProperty("email", user.email);
        paramObject.addProperty("gender", user.gender);
        paramObject.addProperty("ageRange", user.ageRange);
        paramObject.addProperty("profileUrl", user.profileImageUrl);
        paramObject.addProperty("allergy", allergyInfo);
        paramObject.addProperty("disease", diseaseInfo);

        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<BaseResponse> call = service.signUp(paramObject);

        call.enqueue(new retrofit2.Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse result = response.body();
                    if (result.checkError(getActivity()) != 0) return;
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("OBJECT", user);
                    startActivity(intent);
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
