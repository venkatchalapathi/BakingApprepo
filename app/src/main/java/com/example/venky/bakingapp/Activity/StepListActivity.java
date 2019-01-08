package com.example.venky.bakingapp.Activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;


import com.example.venky.bakingapp.Adapters.IngredientListAdapter;
import com.example.venky.bakingapp.Adapters.StepsListAdapter;
import com.example.venky.bakingapp.Models.Ingredient;

import com.example.venky.bakingapp.Others.Baking_App_widgetInfo;
import com.example.venky.bakingapp.R;
import com.example.venky.bakingapp.Models.Step;
import com.example.venky.bakingapp.Others.ProjectConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private String title;
    @BindView(R.id.step_saiRecycler)
    RecyclerView  list_recyclerview;

    private AlertDialog.Builder dialog;
    @BindView(R.id.ingredients_list)
    CardView ing_recyclerView;
    List<Ingredient> ingredients;
    List<Step> steps;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        ButterKnife.bind(this);
        dialog = new AlertDialog.Builder(this);

        title = getIntent().getStringExtra(ProjectConstants.RECIPIE_TITLE);
        setTitle(title);
        if(findViewById(R.id.step_detail_container)!=null)
        {
            mTwoPane = true;
        }

        ing_recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callIngreds();
            }
        });

        ingredients = (List<Ingredient>) getIntent().getSerializableExtra(ProjectConstants.INGREDIENTS_LIST_KEY);
        StringBuilder  stringBuilder = new StringBuilder();
        int stepVal =1;
        for(int i=0;i<ingredients.size();i++)
        {
            stringBuilder.append(""+(stepVal)+". "+ingredients.get(i).getIngredient()+"\n");
            stepVal++;
        }
        String total_ingredients = stringBuilder.toString();
        sharedPreferences = getSharedPreferences(ProjectConstants.BAKING_APP_SHARED_PREFERENCES_KEY,0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(ProjectConstants.SP_TEXT_INGRED_KEY,total_ingredients);
        edit.putString(ProjectConstants.RECIPE_NAME_KEY,getIntent().getStringExtra(ProjectConstants.RECIPIE_TITLE));
        edit.apply();

        Intent intent = new Intent(this,Baking_App_widgetInfo.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(
                new ComponentName(getApplication(),Baking_App_widgetInfo.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);

        steps = (List<Step>) getIntent().getSerializableExtra(ProjectConstants.STEPS_LIST_KEY);
        list_recyclerview.setAdapter(new StepsListAdapter(this,steps,mTwoPane));
        list_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        list_recyclerview.addItemDecoration(new DividerItemDecoration(list_recyclerview.getContext(),DividerItemDecoration.VERTICAL));

    }

    private void callIngreds() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.ingredient_list, null);

        RecyclerView ingreds = v.findViewById(R.id.ingredRecycler);
        ingreds.setLayoutManager(new LinearLayoutManager(this));
        IngredientListAdapter adapter = new IngredientListAdapter(this,ingredients);
        ingreds.setAdapter(adapter);

        dialog.setView(v);
        dialog.setTitle(title+" Ingredients:");
        dialog.setCancelable(false);

        dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        dialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            navigateUpTo(new Intent(this,MainActivity.class));
        }
        return true;
    }
}
