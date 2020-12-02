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

import io.realm.Realm;

import static com.example.recipe_helper.R.drawable.ic_apple;

public class IngredientRecyclerViewAdapter2 extends RecyclerView.Adapter<IngredientRecyclerViewAdapter2.Holder> {

    private ArrayList<IngredientData> arrayList = new ArrayList<IngredientData>();
    private Context context;
    private Realm realm;

    public IngredientRecyclerViewAdapter2(Context context, ArrayList<IngredientData> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientRecyclerViewAdapter2.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.refrigerator_recycler_ingredient_item2, parent, false);
        realm = Realm.getDefaultInstance();
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.ingredient_image.setImageDrawable(context.getResources().getDrawable(ic_apple));
        holder.ingredient_name.setText(arrayList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected ImageView ingredient_image;
        protected TextView ingredient_name;

        public Holder(@NonNull final View itemView) {
            super(itemView);
            this.ingredient_image = (ImageView) itemView.findViewById(R.id.ingredient_image);
            this.ingredient_name = (TextView) itemView.findViewById(R.id.ingredient_name);
        }

    }
}
