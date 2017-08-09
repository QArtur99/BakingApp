package com.android.bakingapp.activities;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.bakingapp.R;
import com.android.bakingapp.fragments.RecyclerViewFragment;
import com.android.bakingapp.models.Recipe;
import com.android.bakingapp.network.DataLoader;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewFragment.OnImageClickListener, LoaderManager.LoaderCallbacks {

    public static final String RECIPE_KEY = "Recipe";
    public static final String STEP_KEY = "Step";
    public static final String STEP_NO_KEY = "StepNo";
    public static Boolean isLandscape = true;
    private RecyclerViewFragment headFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            isLandscape = false;
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        } else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setVisibility(View.GONE);
            isLandscape = true;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        headFragment = new RecyclerViewFragment();
        fragmentManager.beginTransaction()
                .add(R.id.recyclerViewFrame, headFragment)
                .commit();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new DataLoader(this);
    }

    @Override
    public void onLoadFinished(Loader loader, Object object) {
        List<Recipe> data = (List<Recipe>) object;
        headFragment.onLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onImageSelected(Recipe movie) {
        String jsonString = new Gson().toJson(movie);
        Intent intent = new Intent(MainActivity.this, StepsActivity.class);
        intent.putExtra(RECIPE_KEY, jsonString);
        startActivity(intent);
    }
}
