package com.example.recipe_helper.Commnuity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.HttpConnection.BaseResponse;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;

import retrofit2.Call;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Create_post extends Fragment {

    private EditText tv_title;
    private EditText tv_content;
    private static final String TAG = "TAG";
    private InputMethodManager imm;
    private UserInfo user;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_writing, container, false);

        view.setClickable(true);
        setHasOptionsMenu(true);
        imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);

        user = ((MainActivity) getActivity()).user;

        tv_title = view.findViewById(R.id.post_enter_title);
        tv_content = view.findViewById(R.id.post_enter_content);

        tv_title.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);


        view.findViewById(R.id.item_post_check);

        return view;
    }

    public void Upload_post(long id, String content, String title) {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<BaseResponse> call = service.uploadPost(id, content, title);

        call.enqueue(new retrofit2.Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse result = response.body();
                    if (result.checkError(getContext()) != 0) {
                        Log.d(TAG, "ERROR");
                    }
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(getContext(), "연결실패", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.write_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        boolean status_1;
        boolean status_2;

        switch (item.getItemId()) {
            case android.R.id.home:
                //select back button
                getActivity().onBackPressed();
                return true;

            case R.id.item_post_check:
                String title = tv_title.getText().toString();
                String content = tv_content.getText().toString();

                status_1 = content.isEmpty();
                status_2 = title.isEmpty();

                if (status_1) {
                    tv_content.setError("내용을 입력해 주세요");
                }

                if (status_2) {
                    tv_title.setError("제목을 입력해 주세요");
                }

                if (!(status_1 || status_2)) {
                    Upload_post(user.userID, content, title);

                    imm.hideSoftInputFromWindow(tv_content.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(tv_title.getWindowToken(), 0);

                    getActivity().getSupportFragmentManager().popBackStack();
                }
                Log.d(TAG, "onOptionsItemSelected: ");
        }
        return super.onOptionsItemSelected(item);
    }

}
