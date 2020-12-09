package com.example.recipe_helper.Commnuity.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_helper.R;

import java.util.ArrayList;


public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.Holder> {
    private ArrayList<Bitmap> list;
    private Context context;
    private OnListItemSelectedInterface mListener;

    public AddImageAdapter(Context context, ArrayList<Bitmap> list, OnListItemSelectedInterface mListener) {
        this.context = context;
        this.list = list;
        this.mListener = mListener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    @NonNull
    @Override
    public AddImageAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_writing_image_item, parent, false);
        AddImageAdapter.Holder holder = new AddImageAdapter.Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected ImageView photo;

        public Holder(View view) {
            super(view);
            this.photo = view.findViewById(R.id.photo);
            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mListener.onItemSelected(v, position);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AddImageAdapter.Holder holder, final int position) {
        holder.photo.setImageBitmap(list.get(position));
        holder.itemView.setTag(position);
    }
}