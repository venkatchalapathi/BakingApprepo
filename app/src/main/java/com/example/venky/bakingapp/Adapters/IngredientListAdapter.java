package com.example.venky.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.venky.bakingapp.Models.Ingredient;
import com.example.venky.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by VENKY on 12/10/2018.
 */

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.ViewInformation> {
    Context context;
    List<Ingredient> lists;

    public IngredientListAdapter(Context context, List<Ingredient> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public IngredientListAdapter.ViewInformation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientListAdapter.ViewInformation(LayoutInflater.from(context).inflate(R.layout.row_ingredients_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientListAdapter.ViewInformation holder, int position) {
        holder.ingredient.setText(lists.get(position).getIngredient());
        holder.measure.setText(lists.get(position).getMeasure());
        holder.quantity.setText(String.valueOf(lists.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        if (lists!=null) {
            return lists.size();
        }
        else return 0;
    }

    public class ViewInformation extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient)
        TextView ingredient;
        @BindView(R.id.quantity)
        TextView measure;
        @BindView(R.id.measure)
        TextView quantity;

        public ViewInformation(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}