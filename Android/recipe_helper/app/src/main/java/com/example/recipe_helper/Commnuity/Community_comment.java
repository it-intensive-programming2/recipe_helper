package com.example.recipe_helper.Commnuity;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_helper.Commnuity.Adapter.CommentAdapter;
import com.example.recipe_helper.Commnuity.DataFrame.Comments;
import com.example.recipe_helper.Commnuity.DataFrame.Comments2Response;
import com.example.recipe_helper.DataFrame.UserInfo;
import com.example.recipe_helper.HttpConnection.BaseResponse;
import com.example.recipe_helper.HttpConnection.RetrofitAdapter;
import com.example.recipe_helper.HttpConnection.RetrofitService;
import com.example.recipe_helper.MainActivity;
import com.example.recipe_helper.R;

import java.util.ArrayList;

import retrofit2.Call;

public class Community_comment extends Fragment {

    private static final String TAG = "Community_comment";
    private ArrayList<Comments> comment_list = new ArrayList<>();
    private CommentAdapter adapter;

    private String id;
    private String content;
    private String pic;
    private int postID;
    private EditText user_comments;

    private UserInfo user;

    public static Community_comment newInstance(String user_id, String user_pic, String post_content, int postID) {
        // 나중에 댓글
        Bundle args = new Bundle();
        args.putString("id", user_id);
        args.putString("content", post_content);
        args.putString("pic", user_pic);
        args.putInt("postID", postID);
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

        //Todo 서버로부터 달려있는 댓글 불러오기
        user = ((MainActivity) getActivity()).user;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
            content = bundle.getString("content");
            pic = bundle.getString("pic");
            postID = bundle.getInt("postID");
        }

        loadComment();

        TextView writer_id = view.findViewById(R.id.writer_id);
        TextView writer_post = view.findViewById(R.id.writer_post);
        ImageView writer_img = view.findViewById(R.id.writer_img);
        ImageView user_img = view.findViewById(R.id.user_img);

        writer_id.setText(id);
        writer_post.setText(content);

        writer_img.setBackground(new ShapeDrawable(new OvalShape()));
        writer_img.setClipToOutline(true);
        Glide.with(getContext()).load(pic).circleCrop().into(writer_img);

        user_img.setBackground(new ShapeDrawable(new OvalShape()));
        user_img.setClipToOutline(true);
        Glide.with(getContext()).load(user.profileUrl).circleCrop().into(user_img);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);

        adapter = new CommentAdapter(getContext(), comment_list);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.comment_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setAdapter(adapter);

        //Todo POST 버튼 눌러서 댓글 달기
        user_comments = view.findViewById(R.id.user_comments);
        Button comments_post_btn = view.findViewById(R.id.comments_post_btn);

        comments_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_comments.getText().toString().equals(""))
                    user_comments.setError("내용을 입력하세요.");
                else {
                    uploadComment(user.userID, postID, user_comments.getText().toString());
                }
            }
        });

        return view;
    }

    public void uploadComment(long userID, int postID, String content) {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<BaseResponse> call = service.uploadComment(userID, postID, content);

        call.enqueue(new retrofit2.Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse result = response.body();
                    if (result.checkError(getContext()) != 0) {
                        Log.d(TAG, "ERROR");
                    }
                    user_comments.setText("");
                    user_comments.clearFocus();
                    refresh();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast toast = Toast.makeText(getContext(), "연결 실패", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 200, 200);
                toast.show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void loadComment() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<Comments2Response> call = service.loadComment(postID);

        call.enqueue(new retrofit2.Callback<Comments2Response>() {
            @Override
            public void onResponse(Call<Comments2Response> call, retrofit2.Response<Comments2Response> response) {
                if (response.isSuccessful()) {
                    Comments2Response result = response.body();
                    if (result.checkError(getContext()) != 0) {
                        Log.d(TAG, "ERROR");
                    }
                    //Todo
                    ArrayList<Comments> results = result.body;
                    comment_list.clear();
                    comment_list.addAll(results);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<Comments2Response> call, Throwable t) {
                Toast toast = Toast.makeText(getContext(), "연결 실패", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 200, 200);
                toast.show();
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

    public void refresh() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }
}
