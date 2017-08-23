package com.android.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bakingapp.R;
import com.android.bakingapp.models.Ingredient;
import com.android.bakingapp.models.Step;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepAdapter extends RecyclerView.Adapter<StepAdapter.MyViewHolder> {


    final private StepAdapter.ListItemClickListener mOnClickListener;
    private List<Step> stepList;
    private List<Ingredient> ingredientList;
    private Context context;
    private boolean isSteps = true;

    public StepAdapter(Context context, Object object, StepAdapter.ListItemClickListener listener, boolean isSteps) {
        this.context = context;
        if (isSteps) {
            stepList = (List<Step>) object;
        } else {
            ingredientList = (List<Ingredient>) object;
        }

        mOnClickListener = listener;
        this.isSteps = isSteps;
    }

    @Override
    public StepAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (isSteps) {
            view = inflater.inflate(R.layout.row_step, parent, false);
        } else {
            view = inflater.inflate(R.layout.row_ingredient, parent, false);
        }
        return new StepAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepAdapter.MyViewHolder holder, int position) {
        holder.bind(position);
    }


    public Object getDataAtPosition(int position) {
        if (isSteps) {
            return stepList.get(position);
        } else {
            return ingredientList.get(position);
        }
    }


    @Override
    public int getItemCount() {
        if (isSteps) {
            return stepList.size();
        } else {
            return ingredientList.size();
        }
    }

    public List<Step> getStepList() {
        return stepList;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, View view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable
        @BindView(R.id.itemImageStep)
        ImageView itemImageStep;
        @Nullable
        @BindView(R.id.step)
        TextView step;
        @Nullable
        @BindView(R.id.ingredient)
        TextView ingredient;
        @Nullable
        @BindView(R.id.measure)
        TextView measure;
        @Nullable
        @BindView(R.id.stepRelativeLayout)
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            if (isSteps) {
                Step stepVariable = (Step) getDataAtPosition(position);
                step.setText(stepVariable.shortDescription);
                if(!stepVariable.thumbnailURL.isEmpty()){
                    Glide.with(context)
                            .load(stepVariable.thumbnailURL)
                            .thumbnail(Glide.with(context).load(R.drawable.baking_app))
                            .into(itemImageStep);
                }
            } else {
                Ingredient ingredientVariable = (Ingredient) getDataAtPosition(position);
                ingredient.setText(ingredientVariable.ingredient);
                String measureString = ingredientVariable.quantity + " " + ingredientVariable.measure;
                measure.setText(measureString);
            }


        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            if (isSteps) {
                mOnClickListener.onListItemClick(clickedPosition, v);
            }
        }

    }
}