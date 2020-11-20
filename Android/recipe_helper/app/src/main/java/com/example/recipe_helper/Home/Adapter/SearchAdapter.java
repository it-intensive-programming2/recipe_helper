package com.example.recipe_helper.Home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.recipe_helper.Home.Dataframe.HomeRecipeFrame;
import com.example.recipe_helper.R;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HomeRecipeFrame> list;
    private LayoutInflater inflate;
    private SearchAdapter.ViewHolder viewHolder;

    public SearchAdapter(Context context, ArrayList<HomeRecipeFrame> list) {
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.home_search_item, null);
            viewHolder = new SearchAdapter.ViewHolder();
            viewHolder.recipe_name = (TextView) convertView.findViewById(R.id.recipe_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SearchAdapter.ViewHolder) convertView.getTag();
        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.recipe_name.setText(list.get(position).recipe_name);
        return convertView;
    }

    class ViewHolder {
        public TextView recipe_name;
    }
}