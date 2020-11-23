package com.example.recipe_helper.Commnuity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_helper.Commnuity.Community_comment;
import com.example.recipe_helper.Commnuity.DataFrame.Comments;
import com.example.recipe_helper.R;

import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private ArrayList<Comments> comment_list;
    private Context context;

    public CommentAdapter(Context context, ArrayList<Comments> list, Community_comment community_comment){

        this.comment_list = list;
        this.context = context;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, String user_id, String user_pic, String post_content);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        protected ImageView comment_user_pic;
        protected TextView comment_user_id;
        protected TextView comment_user_say;


        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.comment_user_pic = (ImageView) itemView.findViewById(R.id.comment_user_pic);
            this.comment_user_id = (TextView) itemView.findViewById(R.id.comment_user_id);
            this.comment_user_say = (TextView) itemView.findViewById(R.id.comment_user_say);
        }
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_recycler_item, parent, false);

        CommentViewHolder viewHolder = new CommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, final int position) {

        Glide.with(context).load(comment_list.get(position).writerProfileUrl).circleCrop().into(holder.comment_user_pic);
        holder.comment_user_id.setText(comment_list.get(position).writerNickname);
        holder.comment_user_say.setText(comment_list.get(position).content);
        holder.itemView.setTag(position);


    }

    @Override
    public int getItemCount() { return (null != comment_list ? comment_list.size() : 0); }


}
