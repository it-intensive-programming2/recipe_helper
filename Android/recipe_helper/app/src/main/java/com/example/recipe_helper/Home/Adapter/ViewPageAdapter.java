package com.example.recipe_helper.Home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.recipe_helper.Home.Dataframe.HomeRecipeFrame;
import com.example.recipe_helper.R;

import java.util.ArrayList;

public class ViewPageAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<HomeRecipeFrame> list;
    private OnListItemSelectedInterface mListener;

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, String recipe_url);
    }

    public ViewPageAdapter(Context context, ArrayList<HomeRecipeFrame> list, OnListItemSelectedInterface mListener) {
        this.mContext = context;
        this.list = list;
        this.mListener = mListener;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.home_view_page, null);

        final String recipe_url;
        TextView recipe_name = view.findViewById(R.id.recipe_name);
        ImageView imageView = view.findViewById(R.id.imageView);

        recipe_url = list.get(position).recipe_url;
        Glide.with(mContext).load(list.get(position).img_url).into(imageView);
        recipe_name.setText(list.get(position).recipe_name);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemSelected(v, recipe_url);
            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void refresh(ArrayList<HomeRecipeFrame> items) {
        this.list = items;
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View) o);
    }
}