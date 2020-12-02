package com.example.recipe_helper.Home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_helper.Home.Dataframe.DoubleHomeRecipeFrame;
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
        void onItemSelected(View v, int recipeID, int classNum);
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
        protected int recipeID1;
        protected int recipeClass1;

        protected TextView title2;
        protected ImageView image2;
        protected int recipeID2;
        protected int recipeClass2;

        public Holder(View view) {
            super(view);
            this.title1 = (TextView) view.findViewById(R.id.title1);
            this.title2 = (TextView) view.findViewById(R.id.title2);
            this.image1 = (ImageView) view.findViewById(R.id.image1);
            this.image2 = (ImageView) view.findViewById(R.id.image2);

            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, recipeID1, recipeClass1);
                }
            });

            image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, recipeID2, recipeClass2);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewAdapter.Holder holder, final int position) {
        holder.title1.setText(list.get(position).recipe1.title);
        holder.title2.setText(list.get(position).recipe2.title);

        Glide.with(context).load(list.get(position).recipe1.photo).into(holder.image1);
        Glide.with(context).load(list.get(position).recipe2.photo).into(holder.image2);

        holder.recipeID1 = list.get(position).recipe1.recipeID;
        holder.recipeID2 = list.get(position).recipe2.recipeID;

        holder.recipeClass1 = list.get(position).recipe1.classNum;
        holder.recipeClass2 = list.get(position).recipe2.classNum;

        holder.itemView.setTag(position);
    }
}