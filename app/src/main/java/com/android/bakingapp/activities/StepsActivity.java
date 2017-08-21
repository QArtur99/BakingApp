package com.android.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.bakingapp.R;
import com.android.bakingapp.fragments.StepDetailsFragment;
import com.android.bakingapp.fragments.StepsFragment;
import com.android.bakingapp.models.Recipe;
import com.android.bakingapp.models.Step;
import com.google.gson.Gson;

import java.util.List;

import static com.android.bakingapp.activities.MainActivity.RECIPE_KEY;
import static com.android.bakingapp.activities.MainActivity.STEP_KEY;
import static com.android.bakingapp.activities.MainActivity.STEP_NO_KEY;


public class StepsActivity extends AppCompatActivity implements StepsFragment.OnImageClickListener {
    private Recipe recipe;
    private StepDetailsFragment stepDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        if (savedInstanceState == null) {
            recipe = new Gson().fromJson(getIntent().getStringExtra(RECIPE_KEY), Recipe.class);
        } else {
            recipe = new Gson().fromJson(savedInstanceState.getString(RECIPE_KEY), Recipe.class);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.detailsViewFrame) == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(recipe.name);
            setSupportActionBar(toolbar);
        } else {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.gradient_tool_bar));
            actionBar.setTitle(recipe.name);
            stepDetailsFragment = new StepDetailsFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.detailsViewFrame, stepDetailsFragment)
                    .commit();
        }

        StepsFragment headFragment = new StepsFragment();
        headFragment.setMovie(recipe);
        fragmentManager.beginTransaction()
                .add(R.id.recyclerViewFrame, headFragment)
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        String jsonString = new Gson().toJson(recipe);
        savedInstanceState.putString(RECIPE_KEY, jsonString);
    }

    @Override
    public void onImageSelected(List<Step> steps, int stepNo) {
        if (stepDetailsFragment != null) {
            stepDetailsFragment.setStep(steps, stepNo);
        } else {
            String jsonString = new Gson().toJson(steps);
            Intent intent = new Intent(StepsActivity.this, StepDetailsActivity.class);
            intent.putExtra(STEP_KEY, jsonString);
            intent.putExtra(STEP_NO_KEY, stepNo);
            startActivity(intent);
        }
    }
}
