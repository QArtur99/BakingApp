package com.android.bakingapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.bakingapp.R;
import com.android.bakingapp.adapters.StepAdapter;
import com.android.bakingapp.models.Recipe;
import com.android.bakingapp.models.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ART_F on 2017-08-08.
 */

public class StepsFragment extends Fragment implements StepAdapter.ListItemClickListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.switchButton) Button switchButton;
    private OnImageClickListener mCallback;
    private StepAdapter recipeAdapter;
    private Recipe recipe;
    private boolean isSteps = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, rootView);

        if (isSteps) {
            switchButton.setText(R.string.steps);
        } else {
            switchButton.setText(R.string.ingredients);
        }

        if (recipe != null) {
            setAdapter(recipe.steps);
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mCallback.onImageSelected(recipeAdapter.getStepList(), 0);
            }
        }
        return rootView;
    }

    public void setAdapter(Object object) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recipeAdapter = new StepAdapter(getContext(), object, this, isSteps);
        recyclerView.setAdapter(recipeAdapter);
    }

    public void setMovie(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
//        StepDetailsFragment stepDetailsFragment = (StepDetailsFragment) getActivity().getSupportFragmentManager().findFragmentByTag("Step");
//        if(stepDetailsFragment != null) {
//            stepDetailsFragment.setStep(recipeAdapter.getStepList(), clickedItemIndex);
//        }
        mCallback.onImageSelected(recipeAdapter.getStepList(), clickedItemIndex);
    }

    @OnClick(R.id.switchButton)
    public void switchView() {
        if (isSteps) {
            isSteps = false;
            setAdapter(recipe.ingredients);
            switchButton.setText(R.string.ingredients);
        } else {
            isSteps = true;
            setAdapter(recipe.steps);
            switchButton.setText(R.string.steps);
        }
    }


    public interface OnImageClickListener {
        void onImageSelected(List<Step> steps, int stepNo);
    }

}
