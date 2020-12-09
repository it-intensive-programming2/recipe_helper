package com.example.recipe_helper.Refrigerator;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.recipe_helper.DataFrame.IngredientData;
import com.example.recipe_helper.DataFrame.RecipeData;
import com.example.recipe_helper.DataFrame.RecipeResponse;
import com.example.recipe_helper.DataFrame.RecipeResponse2;
import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.Home.Adapter.ViewPageAdapter;
import com.example.recipe_helper.Home.Dataframe.DoubleHomeRecipeFrame;
import com.example.recipe_helper.Home.WebViewFragment;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;
import com.example.recipe_helper.RecipeRecyclerAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;

public class Refrigerator extends Fragment implements RecipeRecyclerAdapter.OnListItemSelectedInterface {

    private RecipeRecyclerAdapter adapter;
    private ArrayList<RecipeData> list = new ArrayList<>();

    private IngredientRecyclerViewAdapter2 ingredientAdapter;
    private ArrayList<IngredientData> ingredientList = new ArrayList<>();

    private Realm realm;

    private Toolbar toolbar;
    private String TAG = "RHC";

    private TextView none_ingredient;

    private UserInfo user;
    private Button btn_search;
    private TextView result_text;
    private TextView delete_text;
    private ShimmerFrameLayout shimmerFrameLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refrigerator_, container, false);
        view.setClickable(true);

        user = ((MainActivity) getActivity()).user;
        btn_search = view.findViewById(R.id.btn_search);
        result_text = view.findViewById(R.id.result_text);
        delete_text = view.findViewById(R.id.delete_string);

        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);

        none_ingredient = view.findViewById(R.id.none_ingredient);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("나의 냉장고");

        realm = Realm.getDefaultInstance();

        ingredientAdapter = new IngredientRecyclerViewAdapter2(getContext(), ingredientList);

        RecyclerView ingredientRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_ingredient);
        ingredientRecyclerView.setHasFixedSize(true);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);

        ingredientRecyclerView.setLayoutManager(layoutManager);
        ingredientRecyclerView.setAdapter(ingredientAdapter);

        adapter = new RecipeRecyclerAdapter(getContext(), list, this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentAddBackStack(new Refrigerator_ingredient());
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (ingredientList.isEmpty()) {
                    Toast.makeText(getContext(), "식재료를 선택해주세요!", Toast.LENGTH_SHORT).show();
                } else if (ingredientList.size() == 1) {
                    Toast.makeText(getContext(), "최소 2가지의 식재료를 선택해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    btn_search.setVisibility(View.INVISIBLE);
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.startShimmer();
                    result_text.setText("검색중...");
                    String ingredientString = "";
                    for (IngredientData ingredientData : ingredientList) {
                        ingredientString = ingredientString + " " + ingredientData.name;
                    }
                    loadUserRecommendRecipe(ingredientString);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        delete_text.setText("");
        result_text.setText("");
        shimmerFrameLayout.setVisibility(View.INVISIBLE);
        btn_search.setVisibility(View.VISIBLE);
        loadIngredient();
    }

    @Override
    public void onItemSelected(View v, int recipeID, int classNum) {
        ((MainActivity) getActivity()).replaceFragmentFull(new WebViewFragment(String.valueOf(recipeID), classNum));
    }

    void loadIngredient() {
        final RealmResults<IngredientData> results = realm.where(IngredientData.class).findAll();
        ingredientList.clear();
        ingredientList.addAll(realm.copyFromRealm(results));
        ingredientAdapter.notifyDataSetChanged();
        if (ingredientList.isEmpty()) {
            none_ingredient.setVisibility(View.VISIBLE);
        }
    }

    private void loadUserRecommendRecipe(String ingredientString) {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<RecipeResponse2> call = service.loadRecommendRecipe3(ingredientString);

        call.enqueue(new retrofit2.Callback<RecipeResponse2>() {
            @Override
            public void onResponse(Call<RecipeResponse2> call, retrofit2.Response<RecipeResponse2> response) {
                if (response.isSuccessful()) {
                    RecipeResponse2 result = response.body();
                    list.clear();
                    assert result != null;
                    list.addAll(result.body);
                    adapter.notifyDataSetChanged();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    if (list.isEmpty()) {
                        btn_search.setVisibility(View.VISIBLE);
                        btn_search.setText("다시 찾기");
                    } else {
                        result_text.setText(String.format("검색결과 %d개", list.size()));
                        if (result.delete_list.length() > 2) {
                            delete_text.setText(String.format("%s (이)가 빠질 수 있어요", result.delete_list));
                        }
                        btn_search.setVisibility(View.GONE);
                    }
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                    btn_search.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("서버 에러 ㅠ-ㅠ").setMessage("다시 시도해주세요!").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse2> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                btn_search.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("서버 에러 ㅠ-ㅠ").setMessage("다시 시도해주세요!").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}