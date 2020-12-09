package com.example.recipe_helper.Commnuity.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.devs.readmoreoption.ReadMoreOption;
import com.example.recipe_helper.Commnuity.DataFrame.Post;
import com.example.recipe_helper.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<Post> post_list;
    private Context context;
    private OnListItemSelectedInterface mListener;

    public PostAdapter(Context context, ArrayList<Post> list, OnListItemSelectedInterface mlistener) {

        this.post_list = list;
        this.context = context;
        this.mListener = mlistener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, String user_id, String user_pic, String post_content, int postID);

        //니중에 img_src 필요
        // void upload_onItemSelected(View v);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        protected TextView post_title;
        protected TextView post_user_id;
        protected TextView post_content;
        protected TextView extend_comment;
        protected TextView heart_cnt;
        protected TextView comment_cnt;
        protected ImageView user_profile;
        protected ImageView heart;
        protected ImageView balloon;
        protected ImageView background;
        protected View tmp;
        protected ViewPager2 viewPager2;
        protected PostImageAdapter adapter;

        protected int postID;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            this.post_user_id = (TextView) itemView.findViewById(R.id.post_user_id);
            this.post_title = (TextView) itemView.findViewById(R.id.post_title);
            this.post_content = (TextView) itemView.findViewById(R.id.post_content);
            this.heart_cnt = (TextView) itemView.findViewById(R.id.heart_cnt);
            this.comment_cnt = (TextView) itemView.findViewById(R.id.comment_cnt);
            this.user_profile = (ImageView) itemView.findViewById(R.id.user_profile);
            this.heart = (ImageView) itemView.findViewById(R.id.heart);
            this.balloon = (ImageView) itemView.findViewById(R.id.go_comment);
            this.extend_comment = (TextView) itemView.findViewById(R.id.extend_comment);
            this.background = (ImageView) itemView.findViewById((R.id.background));
            this.viewPager2 = itemView.findViewById(R.id.view_pager2);
            this.tmp = itemView.findViewById(R.id.tmp);
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


        Glide.with(context).load(post_list.get(position).getWriterProfileUrl()).circleCrop().into(holder.user_profile);

        holder.post_user_id.setText(post_list.get(position).getWriterNickname());
        holder.post_title.setText(post_list.get(position).getTitle());

        try {
            readMoreOption.addReadMoreTo(holder.post_content, post_list.get(position).getContent());
        } catch (NullPointerException e) {
        }

        holder.post_content.setText(post_list.get(position).getContent());

        holder.balloon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemSelected(v, post_list.get(position).getWriterNickname(), post_list.get(position).getWriterProfileUrl(), post_list.get(position).getContent(), post_list.get(position).getPostID());

            }
        });
        holder.extend_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemSelected(v, post_list.get(position).getWriterNickname(), post_list.get(position).getWriterProfileUrl(), post_list.get(position).getContent(), post_list.get(position).getPostID());
            }
        });

        holder.postID = post_list.get(position).getPostID();

        holder.comment_cnt.setText(String.valueOf(post_list.get(position).getComment()));
        holder.heart_cnt.setText(String.valueOf(post_list.get(position).getHeart()));

        //Viewpager
        if (post_list.get(position).getImages().size() == 0) {
            holder.viewPager2.setVisibility(View.GONE);
            holder.tmp.setVisibility(View.GONE);
        } else {
            holder.adapter = new PostImageAdapter(post_list.get(position).getImages());
            holder.viewPager2.setId(position + 1);
            holder.viewPager2.setAdapter(holder.adapter);
        }
    }

    @Override
    public int getItemCount() {
        return (null != post_list ? post_list.size() : 0);
    }
}
