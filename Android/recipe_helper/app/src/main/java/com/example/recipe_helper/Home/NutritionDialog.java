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

    private String calorieString;
    private String carbsString;
    private String proteinString;
    private String fatString;

    public NutritionDialog(@NonNull Context context, String calorieString, String carbsString, String proteinString, String fatString) {
        super(context);
        this.calorieString = calorieString;
        this.carbsString = carbsString;
        this.proteinString = proteinString;
        this.fatString = fatString;
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

        calorie.setText(calorieString.trim());
        carbs.setText(carbsString.trim());
        protein.setText(proteinString.trim());
        fat.setText(fatString.trim());

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
