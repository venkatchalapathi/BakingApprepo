package com.example.venky.bakingapp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.venky.bakingapp.Adapters.MainRecyclerViewAdapter;
import com.example.venky.bakingapp.Others.ProjectConstants;
import com.example.venky.bakingapp.R;
import com.example.venky.bakingapp.Models.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recyclerview1)
    RecyclerView mainRecyclerview;
    List<Recipe> recipesList = new ArrayList<>();
    Recipe[] recipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainRecyclerview.setLayoutManager(new GridLayoutManager(MainActivity.this,2));

        if(savedInstanceState!=null)
        {
            if(savedInstanceState.getSerializable(ProjectConstants.MAIN_ACTIVITY_SERIALIZABLE_KEY)!=null){
                recipesList = (List<Recipe>)  savedInstanceState
                        .getSerializable(ProjectConstants.MAIN_ACTIVITY_SERIALIZABLE_KEY);

            }else if(isNetworkAvailable())
            {
                loadData();
            }
            else{
                showAlert();
            }
        }
        else if(isNetworkAvailable()){
            loadData();
        }else{
            showAlert();
        }
    }
    private void showAlert(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.nointernet)
                .setMessage(R.string.nointernetmsg)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(
                                Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private boolean isNetworkAvailable(){
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }
    private void loadData(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.getdata));
        dialog.setMessage(getString(R.string.plswait));
        dialog.show();
        dialog.setCancelable(true);
        StringRequest request = new StringRequest(ProjectConstants.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                recipes = gson.fromJson(response, Recipe[].class);
                recipesList.addAll(Arrays.asList(recipes));

                mainRecyclerview.setAdapter(new MainRecyclerViewAdapter(MainActivity.this,recipesList));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if(recipesList!=null)
        {
            outState.putSerializable(ProjectConstants.MAIN_ACTIVITY_SERIALIZABLE_KEY, (Serializable) recipesList);
        }
    }
}
