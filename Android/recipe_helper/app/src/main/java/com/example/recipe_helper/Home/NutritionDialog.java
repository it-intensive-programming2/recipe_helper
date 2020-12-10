package com.example.recipe_helper.Home;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.Home.Dataframe.NutritionData;
import com.example.recipe_helper.Home.Dataframe.NutritionResponse;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.R;

import java.util.Objects;

import retrofit2.Call;

public class NutritionDialog extends Dialog {
    private static final String TAG = NutritionDialog.class.getName();

    private TextView calorie;
    //    private TextView percentage;
    private TextView fat;
    private TextView carbs;
    private TextView protein;
    private Button btn_ok;

    private final int recipeID;
    private final UserInfo user;

    public NutritionDialog(@NonNull Context context, int recipeID, UserInfo user) {
        super(context);
        this.recipeID = recipeID;
        this.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_nutrition);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        calorie = findViewById(R.id.calories);
        fat = findViewById(R.id.fat);
        carbs = findViewById(R.id.carbs);
        protein = findViewById(R.id.protein);
        btn_ok = findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getCalories();
    }

    private void getCalories() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<NutritionResponse> call = service.getCalories(recipeID, user.ageRange, user.gender);

        call.enqueue(new retrofit2.Callback<NutritionResponse>() {
            @Override
            public void onResponse(Call<NutritionResponse> call, retrofit2.Response<NutritionResponse> response) {
                if (response.isSuccessful()) {
                    NutritionResponse result = response.body();
                    NutritionData data = result.body;

                    calorie.setText(data.calories);
                    carbs.setText(data.carbs);
                    protein.setText(data.protein);
                    fat.setText(data.fat);

                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<NutritionResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
