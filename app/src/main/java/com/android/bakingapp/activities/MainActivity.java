package com.android.bakingapp.activities;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.bakingapp.R;
import com.android.bakingapp.fragments.RecyclerViewFragment;
import com.android.bakingapp.idlingResource.SimpleIdlingResource;
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

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

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
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        List<Recipe> data = (List<Recipe>) object;
        headFragment.onLoadFinished(data);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }
        }, 5000);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                FragmentManager fragmentManager = getSupportFragmentManager();
                headFragment = new RecyclerViewFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.recyclerViewFrame, headFragment)
                        .commit();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


}
