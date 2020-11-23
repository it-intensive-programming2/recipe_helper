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

import com.bumptech.glide.Glide;
import com.example.recipe_helper.R;

public class Community_comment extends Fragment {

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
        actionbar.setDisplayHomeAsUpEnabled(true);

        return view;
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
