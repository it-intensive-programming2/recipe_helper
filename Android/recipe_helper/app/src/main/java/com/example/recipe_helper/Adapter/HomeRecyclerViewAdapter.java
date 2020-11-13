package com.example.recipe_helper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_helper.Dataframe.DoubleHomeRecipeFrame;
import com.example.recipe_helper.R;

import java.util.ArrayList;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.Holder> {
    private ArrayList<DoubleHomeRecipeFrame> list = new ArrayList<DoubleHomeRecipeFrame>();
    private Context context;
    private OnListItemSelectedInterface mListener;

    public HomeRecyclerViewAdapter(Context context, ArrayList<DoubleHomeRecipeFrame> list, OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, String recipe_url);
    }

    @NonNull
    @Override
    public HomeRecyclerViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_item, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView title1;
        protected ImageView image1;
        protected String recipe_url1;

        protected TextView title2;
        protected ImageView image2;
        protected String recipe_url2;

        public Holder(View view) {
            super(view);
            this.title1 = (TextView) view.findViewById(R.id.title1);
            this.title2 = (TextView) view.findViewById(R.id.title2);
            this.image1 = (ImageView) view.findViewById(R.id.image1);
            this.image2 = (ImageView) view.findViewById(R.id.image2);

            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, recipe_url1);
                }
            });

            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, recipe_url2);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewAdapter.Holder holder, final int position) {
        holder.title1.setText(list.get(position).recipe1.recipe_name);
        holder.title2.setText(list.get(position).recipe2.recipe_name);

        Glide.with(context).load(list.get(position).recipe1.img_url).into(holder.image1);
        Glide.with(context).load(list.get(position).recipe2.img_url).into(holder.image2);

        holder.recipe_url1 = list.get(position).recipe1.recipe_url;
        holder.recipe_url2 = list.get(position).recipe2.recipe_url;

        holder.itemView.setTag(position);
    }


}