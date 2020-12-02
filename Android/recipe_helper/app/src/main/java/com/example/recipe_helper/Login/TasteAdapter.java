package com.example.recipe_helper.Login;

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

public class TasteAdapter extends RecyclerView.Adapter<TasteAdapter.Holder> {
    private ArrayList<RecipeData> list = new ArrayList<RecipeData>();
    private Context context;
    private TasteAdapter.OnListItemSelectedInterface mListener;

    public TasteAdapter(Context context, ArrayList<RecipeData> list, TasteAdapter.OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int recipeID, int classNum, int position);
    }

    @NonNull
    @Override
    public TasteAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.signup2_recycler_item, parent, false);
        TasteAdapter.Holder holder = new TasteAdapter.Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected ImageView image;
        protected int recipeID;
        protected int classNum;
        protected boolean isChecked;

        public Holder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.image = (ImageView) view.findViewById(R.id.image);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, recipeID, classNum, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TasteAdapter.Holder holder, final int position) {
        holder.title.setText(list.get(position).title);
        Glide.with(context).load(list.get(position).photo).into(holder.image);
        holder.recipeID = list.get(position).recipeID;
        holder.isChecked = false;
        holder.classNum = list.get(position).classNum;

        holder.itemView.setTag(position);
    }
}