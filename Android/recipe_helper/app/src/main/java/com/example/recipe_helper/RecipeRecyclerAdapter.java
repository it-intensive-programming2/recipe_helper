package com.example.recipe_helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_helper.DataFrame.RecipeData;

import java.util.ArrayList;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.Holder> {
    private ArrayList<RecipeData> list = new ArrayList<RecipeData>();
    private Context context;
    private OnListItemSelectedInterface mListener;

    public RecipeRecyclerAdapter(Context context, ArrayList<RecipeData> list, RecipeRecyclerAdapter.OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int recipeID, int classNum);
    }

    @NonNull
    @Override
    public RecipeRecyclerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_recycler_item, parent, false);
        RecipeRecyclerAdapter.Holder holder = new RecipeRecyclerAdapter.Holder(view);

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
        protected TextView time;
        protected TextView level;
        protected ImageView recipeImage;
        protected int classNum;


        public Holder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.ingredient = (TextView) view.findViewById(R.id.ingredient);
            this.author = (TextView) view.findViewById(R.id.author);
            this.cat1 = (TextView) view.findViewById(R.id.cat1);
            this.time = (TextView) view.findViewById(R.id.time);
            this.level = (TextView) view.findViewById(R.id.level);

            this.recipeImage = (ImageView) view.findViewById(R.id.recipe_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, list.get(getAdapterPosition()).recipeID, classNum);
                }
            });

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeRecyclerAdapter.Holder holder, final int position) {
        holder.title.setText(list.get(position).title);
        holder.author.setText("by " + list.get(position).author);
        String category = list.get(position).className;
        if (category.equals("카레")) {
            holder.cat1.setText(category);
        } else {
            holder.cat1.setText(category.split("_")[1]);
        }
        holder.time.setText(list.get(position).time);
        holder.level.setText(list.get(position).level);
        holder.ingredient.setText(list.get(position).ingredientList);
        holder.classNum = list.get(position).classNum;

        holder.title.setBackground(null);
        holder.author.setBackground(null);
        holder.cat1.setBackground(null);
        holder.time.setBackground(null);
        holder.level.setBackground(null);
        holder.ingredient.setBackground(null);

        Glide.with(context).load(list.get(position).photo).into(holder.recipeImage);


        holder.itemView.setTag(position);
    }
}
