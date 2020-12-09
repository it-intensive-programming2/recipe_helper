package com.example.recipe_helper.Commnuity.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_helper.R;

import java.util.ArrayList;

public class PostImageAdapter extends RecyclerView.Adapter<PostImageAdapter.Holder> {
    private ArrayList<String> list;

    public PostImageAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view_pager_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected ImageView image;

        public Holder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.image.setImageBitmap(getBitmapFromString(list.get(position)));

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}