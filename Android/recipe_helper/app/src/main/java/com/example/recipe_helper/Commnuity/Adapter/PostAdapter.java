package com.example.recipe_helper.Commnuity.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devs.readmoreoption.ReadMoreOption;
import com.example.recipe_helper.Commnuity.DataFrame.Post;
import com.example.recipe_helper.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<Post> post_list;
    private Context context;
    private OnListItemSelectedInterface mlistener;

    public PostAdapter(Context context, ArrayList<Post> list, OnListItemSelectedInterface mlistener) {

        this.post_list = list;
        this.context = context;
        this.mlistener = mlistener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, String user_id, String user_pic, String post_content);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        protected TextView post_title;
        protected TextView post_user_id;
        protected TextView post_content;
        protected TextView extend_comment;
        protected ImageView post_pics;
        protected ImageView heart;
        protected ImageView balloon;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            this.post_user_id = (TextView) itemView.findViewById(R.id.post_user_id);
            this.post_title = (TextView) itemView.findViewById(R.id.post_title);
            this.post_content = (TextView) itemView.findViewById(R.id.post_content);
            this.post_pics = (ImageView) itemView.findViewById(R.id.post_pic);
            this.heart = (ImageView) itemView.findViewById(R.id.heart);
            this.balloon = (ImageView) itemView.findViewById(R.id.go_comment);
            this.extend_comment = (TextView) itemView.findViewById(R.id.extend_comment);
        }


    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_recycler_item, parent, false);

        PostViewHolder viewHolder = new PostViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {

        ReadMoreOption readMoreOption = new ReadMoreOption.Builder(context).textLength(30, ReadMoreOption.TYPE_CHARACTER) // OR
                .moreLabel("more")
                .lessLabel("...less")
                .moreLabelColor(Color.rgb(128, 128, 128))
                .lessLabelColor(Color.rgb(128, 128, 128))
                .labelUnderLine(false)
                .expandAnimation(true)
                .build();
        Glide.with(context).load(post_list.get(position).getImg_url()).into(holder.post_pics);
        holder.post_user_id.setText(post_list.get(position).getUser_id());
        holder.post_title.setText(post_list.get(position).getPost_title());
        readMoreOption.addReadMoreTo(holder.post_content, post_list.get(position).getPost_content());
        holder.post_content.setText(post_list.get(position).getPost_content());

        holder.balloon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onItemSelected(v, post_list.get(position).getUser_id(), post_list.get(position).getUser_img(), post_list.get(position).getPost_content());

            }
        });
        holder.extend_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onItemSelected(v, post_list.get(position).getUser_id(), post_list.get(position).getUser_img(), post_list.get(position).getPost_content());
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != post_list ? post_list.size() : 0);
    }
}
