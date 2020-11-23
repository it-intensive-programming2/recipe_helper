package com.example.recipe_helper.Commnuity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.recipe_helper.Commnuity.DataFrame.Comments2;
import com.example.recipe_helper.Commnuity.DataFrame.Comments2Response;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.R;

import java.util.ArrayList;

import retrofit2.Call;

public class Community_comment extends Fragment implements CommentAdapter.OnListItemSelectedInterface{

    private static final String TAG = "Community_comment";
    private ArrayList<Comments2> comment_list = new ArrayList<Comments2>();
    private CommentAdapter adapter;

    private String id;
    private String content;
    private String pic;

    public static Community_comment newInstance(String user_id, String user_pic, String post_content) {
        // 나중에 댓글
        Bundle args = new Bundle();
        args.putString("id", user_id);
        args.putString("content", post_content);
        args.putString("pic", user_pic);
        Community_comment comment = new Community_comment();
        comment.setArguments(args);
        return comment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment, container, false);
        view.setClickable(true);
        setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
            content = bundle.getString("content");
            pic = bundle.getString("pic");
        }

        TextView writer_id = view.findViewById(R.id.writer_id);
        TextView writer_post = view.findViewById(R.id.writer_post);
        ImageView writer_img = view.findViewById(R.id.writer_img);

        writer_id.setText(id);
        writer_post.setText(content);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
//        actionbar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionbar.setDisplayHomeAsUpEnabled(true);

        return view;
    }

    public void loadComment() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<Comments2Response> call = service.loadComment();

        call.enqueue(new retrofit2.Callback<Comments2Response>() {
            @Override
            public void onResponse(Call<Comments2Response> call, retrofit2.Response<Comments2Response> response) {
                if (response.isSuccessful()) {
                    Comments2Response result = response.body();
                    if (result.checkError(getContext()) != 0) {
                        Log.d(TAG, "ERROR");
                    }
                    //Todo
                    ArrayList<Comments2> results = result.body;

                    comment_list.addAll(results);

                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<Comments2Response> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //select back button
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
