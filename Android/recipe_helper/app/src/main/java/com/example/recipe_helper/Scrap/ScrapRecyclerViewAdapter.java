package com.example.recipe_helper.Scrap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_helper.Home.Adapter.HomeRecyclerViewAdapter;
import com.example.recipe_helper.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ScrapRecyclerViewAdapter extends RecyclerView.Adapter<ScrapRecyclerViewAdapter.Holder> {

    private ArrayList<like_recipe> list;
    private OnListItemSelectedInterface mlistener;
    private Context context;

    public ScrapRecyclerViewAdapter(Context context, ArrayList<like_recipe> list, OnListItemSelectedInterface mlistener) {
        this.context = context;
        this.list = list;
        this.mlistener = mlistener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    @NonNull
    @Override
    public ScrapRecyclerViewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scrap_recycler_item, parent, false);
        ScrapRecyclerViewAdapter.Holder holder = new ScrapRecyclerViewAdapter.Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {

        protected ImageView scrap_img;
        protected ImageView scrap_star;
        protected TextView scrap_recipe_name;
        protected TextView scrap_recipe_owner;
        protected LinearLayout link;


        public Holder(View view) {
            super(view);
            this.scrap_img = (ImageView) view.findViewById(R.id.scrap_img);
            this.scrap_star = (ImageView) view.findViewById(R.id.scrap_star);
            this.scrap_recipe_name = (TextView) view.findViewById(R.id.scrap_recipe_name);
            this.scrap_recipe_owner = (TextView) view.findViewById(R.id.scrap_recipe_owner);
            this.link = (LinearLayout) view.findViewById(R.id.scrap_layout);

            /* clicked star */
            scrap_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.onItemSelected(v, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ScrapRecyclerViewAdapter.Holder holder, final int position) {

        Glide.with(context).load(list.get(position).getScrap_img()).into(holder.scrap_img);
        holder.scrap_recipe_name.setText(list.get(position).getScrap_recipe_name());
        holder.scrap_recipe_owner.setText(list.get(position).getScrap_recipe_owner());

        holder.itemView.setTag(position);
    }
}
