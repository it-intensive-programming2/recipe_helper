package com.example.recipe_helper.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.HttpConnection.BaseResponse;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;

import retrofit2.Call;

public class WebViewFragment extends Fragment {

    private static final String TAG = WebViewFragment.class.getName();
    private String recipeID;
    private int recipeClass;

    private ImageView scrap_star;
    private LinearLayout ll_1;
    private LinearLayout ll_2;
    private LinearLayout ll_3;
    private UserInfo user;
    private boolean isScrap = false;

    public WebViewFragment(String recipeID, int recipeClass) {
        this.recipeID = recipeID;
        this.recipeClass = recipeClass;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview, container, false);
        view.setClickable(true);
        user = ((MainActivity) getActivity()).user;

        HTTPisScrap(Integer.parseInt(recipeID));

        WebView mWebView = (WebView) view.findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        WebSettings mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부

        mWebView.loadUrl("https://www.10000recipe.com/recipe/" + recipeID); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

        ll_1 = view.findViewById(R.id.ll_1);
        ll_2 = view.findViewById(R.id.ll_2);
        ll_3 = view.findViewById(R.id.ll_3);
        scrap_star = view.findViewById(R.id.scrap_star);

        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });

        ll_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isScrap) {
                    scrap_star.setBackgroundResource(R.drawable.ic_icon_before_scrap);
                    isScrap = false;
                } else {
                    scrap_star.setBackgroundResource(R.drawable.ic_icon_after_scrap);
                    isScrap = true;
                }
                setScrap(Integer.parseInt(recipeID));
            }
        });
        addHistory(Integer.parseInt(recipeID), recipeClass);
        return view;
    }

    private void addHistory(int recipeID, int recipeClass) {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<BaseResponse> call = service.addHistory(user.userID, recipeID, recipeClass);

        call.enqueue(new retrofit2.Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse result = response.body();
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


    private void HTTPisScrap(int recipeID) {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<BaseResponse> call = service.isScrap(user.userID, recipeID);

        call.enqueue(new retrofit2.Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse result = response.body();
                    if (result.checkError(getActivity()) != 0) isScrap = false;
                    else {
                        scrap_star.setBackgroundResource(R.drawable.ic_icon_after_scrap);
                        isScrap = true;
                    }
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

    private void setScrap(int recipeID) {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<BaseResponse> call = service.setScrap(user.userID, recipeID, recipeClass);

        call.enqueue(new retrofit2.Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse result = response.body();
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
