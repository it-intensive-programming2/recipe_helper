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
import com.example.recipe_helper.DataFrame.RecipeData;
import com.example.recipe_helper.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> {
    private ArrayList<RecipeData> list = new ArrayList<RecipeData>();
    private Context context;
    private OnListItemSelectedInterface mListener;

    public SearchAdapter(Context context, ArrayList<RecipeData> list, OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int recipeID);
    }

    @NonNull
    @Override
    public SearchAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_search_item, parent, false);
        SearchAdapter.Holder holder = new SearchAdapter.Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView recipe_name;
        protected ImageView recipe_img;

        public Holder(View view) {
            super(view);
            this.recipe_name = (TextView) view.findViewById(R.id.recipe_name);
            this.recipe_img = (ImageView) view.findViewById(R.id.recipe_img);

            recipe_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, list.get(getAdapterPosition()).recipeID);
                }
            });

            recipe_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, list.get(getAdapterPosition()).recipeID);
                }
            });

        }
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.Holder holder, final int position) {
        holder.recipe_name.setText(list.get(position).title);

        Glide.with(context).load(list.get(position).photo).into(holder.recipe_img);

        holder.itemView.setTag(position);
    }
}