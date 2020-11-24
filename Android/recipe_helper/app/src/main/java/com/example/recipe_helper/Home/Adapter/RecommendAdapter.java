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

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.Holder> {
    private ArrayList<RecipeData> list = new ArrayList<RecipeData>();
    private Context context;
    private OnListItemSelectedInterface mListener;

    public RecommendAdapter(Context context, ArrayList<RecipeData> list, RecommendAdapter.OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int recipeID);
    }

    @NonNull
    @Override
    public RecommendAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_recycler_item, parent, false);
        RecommendAdapter.Holder holder = new RecommendAdapter.Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView ingredient;
        protected TextView author;
        protected TextView cat1;
        protected TextView cat2;
        protected TextView time;
        protected TextView level;
        protected ImageView recipeImage;


        public Holder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.ingredient = (TextView) view.findViewById(R.id.ingredient);
            this.author = (TextView) view.findViewById(R.id.author);
            this.cat1 = (TextView) view.findViewById(R.id.cat1);
            this.cat2 = (TextView) view.findViewById(R.id.cat2);
            this.time = (TextView) view.findViewById(R.id.time);
            this.level = (TextView) view.findViewById(R.id.level);

            this.recipeImage = (ImageView) view.findViewById(R.id.recipe_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, list.get(getAdapterPosition()).recipeID);
                }
            });

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendAdapter.Holder holder, final int position) {
        holder.title.setText(list.get(position).title);
        holder.ingredient.setText(list.get(position).ingredientList);
        holder.author.setText(list.get(position).author);
        holder.cat1.setText(list.get(position).cat1);
        holder.cat2.setText(list.get(position).cat2);
        holder.time.setText(list.get(position).time);
        holder.level.setText(list.get(position).level);

        Glide.with(context).load(list.get(position).photo).into(holder.recipeImage);


        holder.itemView.setTag(position);
    }
}
