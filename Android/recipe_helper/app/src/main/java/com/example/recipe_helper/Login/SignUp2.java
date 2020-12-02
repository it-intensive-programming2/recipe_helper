package com.example.recipe_helper.Login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_helper.DataFrame.RecipeData;
import com.example.recipe_helper.DataFrame.RecipeResponse;
import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.HttpConnection.BaseResponse;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;

import static com.example.recipe_helper.R.drawable.ic_image_check;

public class SignUp2 extends Fragment implements TasteAdapter.OnListItemSelectedInterface {
    private static final String TAG = "RHC";
    private UserInfo user;
    private String allergyInfo;
    private String diseaseInfo;

    private int cnt = 0;
    private ProgressBar progressBar;
    private TextView ment;
    private ShimmerFrameLayout shimmerFrameLayout;

    private TasteAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<RecipeData> list = new ArrayList<>();
    private ArrayList<String> selectList = new ArrayList<>();
    private ArrayList<Integer> selectClassList = new ArrayList<>();


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
        setHasOptionsMenu(true);

        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        shimmerFrameLayout.startShimmer();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);

        loadTasteRecipe();

        progressBar = view.findViewById(R.id.progress_bar);
        ment = view.findViewById(R.id.ment);

        adapter = new TasteAdapter(getContext(), list, this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void signUp() {
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("id", user.userID);
        paramObject.addProperty("nickName", user.nickName);
        paramObject.addProperty("email", user.email);
        paramObject.addProperty("gender", user.gender);
        paramObject.addProperty("ageRange", user.ageRange);
        paramObject.addProperty("profileUrl", user.profileUrl);
        paramObject.addProperty("allergy", allergyInfo);
        paramObject.addProperty("disease", diseaseInfo);
        paramObject.addProperty("selectList", selectList.toString().replaceAll("[^0-9 ]", ""));
        paramObject.addProperty("selectClassList", selectClassList.toString().replaceAll("[^0-9 ]", ""));

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

    private void loadTasteRecipe() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<RecipeResponse> call = service.loadTasteRecipe();

        call.enqueue(new retrofit2.Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, retrofit2.Response<RecipeResponse> response) {
                if (response.isSuccessful()) {
                    RecipeResponse result = response.body();
                    list.clear();
                    list.addAll(result.body);
                    adapter.notifyDataSetChanged();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
//        menu.findItem(R.id.item_success).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.signup_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //select back button
                getActivity().onBackPressed();
                return true;

            case R.id.item_success:
                if (cnt >= 10) {
                    signUp();
                } else {
                    Toast.makeText(getContext(), "10개 이상 체크해주세요.", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onItemSelected(View v, int recipeID, int classNum, int position) {
        final TasteAdapter.Holder holder = (TasteAdapter.Holder) recyclerView.findViewHolderForAdapterPosition(position);
        if (holder.isChecked) {
            cnt--;
            holder.image.setForeground(getResources().getDrawable(R.drawable.transparent));
            selectList.remove(String.valueOf(recipeID));
            selectClassList.remove(classNum);
            holder.isChecked = false;
        } else {
            cnt++;
            holder.image.setForeground(getResources().getDrawable(ic_image_check));
            selectList.add(String.valueOf(recipeID));
            selectClassList.add(classNum);
            holder.isChecked = true;
        }
        progressBar.setProgress(cnt);
        if (cnt > 50) {
            progressBar.setMax(200);
            ment.setText("당신의 열정에 놀랐어요!! :)");
        } else if (cnt > 40) {
            progressBar.setMax(50);
            ment.setText("이제 웬만한 친구보다 제가 당신의 취향을 더 잘 알껄요?");
        } else if (cnt > 33) {
            progressBar.setMax(40);
            ment.setText("한번 마음먹으면 하는 분이시네요");
        } else if (cnt > 30) {
            progressBar.setMax(33);
            ment.setText("음식의 맛을 한번 상상해보세요 ㅎㅎ");
        } else if (cnt > 25) {
            progressBar.setMax(30);
            ment.setText("30개 평가가 눈앞이에요");
        } else if (cnt > 20) {
            progressBar.setMax(25);
            ment.setText("아하 이런 스타일이시군요!");
        } else if (cnt > 13) {
            progressBar.setMax(20);
            ment.setText("어떤 음식을 좋아하실지 조금씩 감이 와요");
        } else if (cnt > 10) {
            progressBar.setMax(15);
            ment.setText("더 하시기로 마음 먹으셨군요! 좋아요:)");
        } else if (cnt == 10) {
            ment.setText("10개 달성! 더 평가하면 추천이 더욱 좋아져요");
        }

        Log.d("RHC", selectList.toString());
    }
}
