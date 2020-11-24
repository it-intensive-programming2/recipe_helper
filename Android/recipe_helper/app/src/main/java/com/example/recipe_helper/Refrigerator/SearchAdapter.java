package com.example.recipe_helper.Refrigerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_helper.DataFrame.IngredientData;
import com.example.recipe_helper.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> {
    private ArrayList<IngredientData> list = new ArrayList<IngredientData>();
    private Context context;
    private OnListItemSelectedInterface mListener;

    public SearchAdapter(Context context, ArrayList<IngredientData> list, OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, String ingredient_name);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.refrigerator_search_item, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView ingredient_name;
        protected ImageView ingredient_image;

        public Holder(View view) {
            super(view);
            this.ingredient_name = (TextView) view.findViewById(R.id.ingredient_name);
            this.ingredient_image = (ImageView) view.findViewById(R.id.ingredient_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, ingredient_name.getText().toString());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.ingredient_name.setText(list.get(position).name);

//        Glide.with(context).load(list.get(position).image).into(holder.ingredient_image);

        holder.itemView.setTag(position);
    }
}