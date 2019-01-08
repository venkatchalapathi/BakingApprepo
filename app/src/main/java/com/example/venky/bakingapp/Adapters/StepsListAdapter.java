package com.example.venky.bakingapp.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.venky.bakingapp.Activity.StepDetailActivity;
import com.example.venky.bakingapp.Activity.StepListActivity;
import com.example.venky.bakingapp.Models.Step;
import com.example.venky.bakingapp.Others.ProjectConstants;
import com.example.venky.bakingapp.R;
import com.example.venky.bakingapp.Others.StepDetailFragment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ADMIN on 12/10/2018.
 */

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.ViewInformation> {

    StepListActivity stepsListActivity;
    List<Step> lists;
    public boolean mTwoPane;

    public StepsListAdapter(StepListActivity stepsListActivity, List<Step> lists, boolean mTwoPane) {
        this.stepsListActivity = stepsListActivity;
        this.lists = lists;
        this.mTwoPane = mTwoPane;
    }

    @NonNull
    @Override
    public StepsListAdapter.ViewInformation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StepsListAdapter.ViewInformation(LayoutInflater.from(stepsListActivity).inflate(R.layout.steps_list_entry, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull StepsListAdapter.ViewInformation holder, int position) {
        holder.steps.setText(lists.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (lists != null) {
            return lists.size();
        } else return 0;
    }

    public class ViewInformation extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView steps;

        public ViewInformation(View itemView) {
            super(itemView);
            steps = itemView.findViewById(R.id.step);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (mTwoPane) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ProjectConstants.STEP_LIST_ACTIVITY_EXTRA_KEY, (Serializable) lists);
                bundle.putInt(ProjectConstants.POSITION_KEY, position);
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setArguments(bundle);
                stepsListActivity.getSupportFragmentManager().beginTransaction().replace(R.id.step_detail_container, stepDetailFragment).commit();

            } else {
                Intent intent = new Intent(stepsListActivity, StepDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ProjectConstants.STEP_LIST_ACTIVITY_EXTRA_KEY, (Serializable) lists);
                bundle.putInt(ProjectConstants.POSITION_KEY, position);
                intent.putExtra(ProjectConstants.BUNDLE_KEY, bundle);
                stepsListActivity.startActivity(intent);
            }

        }
    }
}