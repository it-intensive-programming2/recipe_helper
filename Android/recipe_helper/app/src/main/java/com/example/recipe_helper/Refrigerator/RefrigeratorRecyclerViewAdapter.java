package com.example.recipe_helper.Refrigerator;

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

public class RefrigeratorRecyclerViewAdapter extends RecyclerView.Adapter<RefrigeratorRecyclerViewAdapter.Holder> {

    private ArrayList<RecipeData> arrayList = new ArrayList<RecipeData>();
    private Context context;
    private OnListItemSelectedInterface mListener;

    public RefrigeratorRecyclerViewAdapter(Context context, ArrayList<RecipeData> arrayList, OnListItemSelectedInterface mListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.mListener = mListener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, String recipe_url);
    }

    @NonNull
    @Override
    public RefrigeratorRecyclerViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.refrigerator_recycler_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RefrigeratorRecyclerViewAdapter.Holder holder, int position) {

        Glide.with(context).load(arrayList.get(position).photo).into(holder.refrige_recipe_img);

        holder.refrige_recipe_ingredient.setText(arrayList.get(position).ingredientList);
        holder.refrige_recipe_time.setText(arrayList.get(position).time);
        holder.refrige_recipe_name.setText(arrayList.get(position).title);

        holder.recipeUrl = String.valueOf(arrayList.get(position).recipeID);
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected ImageView refrige_recipe_img;
        protected TextView refrige_recipe_name;
        protected TextView refrige_recipe_ingredient;
        protected TextView refrige_recipe_time;

        protected String recipeUrl;

        public Holder(@NonNull View itemView) {
            super(itemView);
            this.refrige_recipe_img = (ImageView) itemView.findViewById(R.id.refrige_recipe_img);
            this.refrige_recipe_name = (TextView) itemView.findViewById(R.id.refrige_recipe_name);
            this.refrige_recipe_ingredient = (TextView) itemView.findViewById(R.id.refrige_recipe_ingredient);
            this.refrige_recipe_time = (TextView) itemView.findViewById(R.id.refrige_recipe_time);

            this.refrige_recipe_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, recipeUrl);
                }
            });
        }
    }
}
