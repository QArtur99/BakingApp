package com.android.bakingapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import com.android.bakingapp.activities.StepsActivity;
import com.android.bakingapp.adapters.StepAdapter;
import com.android.bakingapp.models.Recipe;
import com.android.bakingapp.models.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepsFragment extends Fragment implements StepAdapter.ListItemClickListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.switchButton) Button switchButton;
    private OnImageClickListener mCallback;
    private StepAdapter recipeAdapter;
    private Recipe recipe;
    private boolean isSteps = true;
    private View previousView;
    private int stepNo = 0;
    private Bundle bundle;
    private SharedPreferences sharedPreferences;
    private GridLayoutManager layoutManager;

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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        stepNo = sharedPreferences.getInt(getString(R.string.pref_lastClicked), 0);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        bundle = this.getArguments();

        if (isSteps) {
            switchButton.setText(R.string.steps);
        } else {
            switchButton.setText(R.string.ingredients);
        }

        if (recipe != null) {
            setAdapter(recipe.steps);
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (bundle == null) {
                            onListItemClick(0, layoutManager.findViewByPosition(0));
                        } else {
                            onListItemClick(stepNo, layoutManager.findViewByPosition(stepNo));
                            recyclerView.scrollToPosition(stepNo);
                            setBundleData();
                        }
                    }
                }, 100);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (bundle != null) {
                            layoutManager.findViewByPosition(stepNo).setSelected(true);
                            previousView = layoutManager.findViewByPosition(stepNo);
                            recyclerView.scrollToPosition(stepNo);
                            setBundleData();
                        } else {
                            recyclerView.getChildAt(0).setSelected(true);
                            previousView = layoutManager.findViewByPosition(0);
                        }
                    }
                }, 100);
            }
        }

        return rootView;
    }

    private void setBundleData() {
        isSteps = bundle.getBoolean(StepsActivity.BUNDLE_IS_STEPS, true);
        if(!isSteps) {
            setAdapter(recipe.ingredients);
            switchButton.setText(R.string.ingredients);
        }
    }

    public void setAdapter(Object object) {
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recipeAdapter = new StepAdapter(getContext(), object, this, isSteps);
        recyclerView.setAdapter(recipeAdapter);
    }

    public View getRecyclerViewChildAt(int position) {
        return layoutManager.findViewByPosition(position);
    }

    public void scrollToPosition(int position) {
        recyclerView.scrollToPosition(position);
    }

    public void setMovie(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onListItemClick(final int clickedItemIndex, final View view) {
        sharedPreferences.edit().putInt(getString(R.string.pref_lastClicked), clickedItemIndex).apply();
        if (previousView != null) {
            previousView.setSelected(false);
        }

        if (view != null) {
            view.setSelected(true);
            previousView = view;
        }

        mCallback.onImageSelected(recipeAdapter.getStepList(), clickedItemIndex);

    }

    public Boolean getIsSteps(){
        return isSteps;
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
