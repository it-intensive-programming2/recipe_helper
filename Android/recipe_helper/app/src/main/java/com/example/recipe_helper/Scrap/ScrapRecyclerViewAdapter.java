package com.example.recipe_helper.Scrap;

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

public class ScrapRecyclerViewAdapter extends RecyclerView.Adapter<ScrapRecyclerViewAdapter.Holder> {
    private ArrayList<RecipeData> list = new ArrayList<RecipeData>();
    private Context context;
    private OnListItemSelectedInterface mListener;

    public ScrapRecyclerViewAdapter(Context context, ArrayList<RecipeData> list, OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected2(View v, int recipeID, int classNum);

        void onItemSelected(View v, int position, int recipeID, int classNum);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scrap_recycler_item, parent, false);
        Holder holder = new Holder(view);

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
        protected ImageView scrapStar;

        protected boolean isChecked;


        public Holder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.ingredient = (TextView) view.findViewById(R.id.ingredient);
            this.author = (TextView) view.findViewById(R.id.author);
            this.cat1 = (TextView) view.findViewById(R.id.cat1);
            this.time = (TextView) view.findViewById(R.id.time);
            this.level = (TextView) view.findViewById(R.id.level);
            this.scrapStar = (ImageView) view.findViewById(R.id.scrap_star);

            this.recipeImage = (ImageView) view.findViewById(R.id.recipe_image);

            recipeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected2(v, list.get(getAdapterPosition()).recipeID, list.get(getAdapterPosition()).classNum);
                }
            });

            scrapStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, getAdapterPosition(), list.get(getAdapterPosition()).recipeID, list.get(getAdapterPosition()).classNum);
                }
            });

        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
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
        holder.isChecked = true;
        holder.scrapStar.setBackgroundResource(R.drawable.ic_icon_after_scrap);

        String ingredient = list.get(position).ingredientList;
//        ingredient = ingredient.replace("]", "");
//        ingredient = ingredient.replace("[", "");
//        ingredient = ingredient.replace("'", "");
        holder.ingredient.setText(ingredient);

        Glide.with(context).load(list.get(position).photo).into(holder.recipeImage);

        holder.itemView.setTag(position);
    }
}