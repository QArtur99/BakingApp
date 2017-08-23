package com.android.bakingapp.activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.bakingapp.R;
import com.android.bakingapp.fragments.StepDetailsFragment;
import com.android.bakingapp.models.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import static com.android.bakingapp.activities.MainActivity.STEP_KEY;
import static com.android.bakingapp.activities.MainActivity.STEP_NO_KEY;


public class StepDetailsActivity extends AppCompatActivity implements StepDetailsFragment.OnSwipeListener {

    private StepDetailsFragment stepDetailsFragment;
    private List<Step> steps;
    private int stepNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.finish();
            return;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        steps = new Gson().fromJson(getIntent().getStringExtra(STEP_KEY), new TypeToken<List<Step>>() {
        }.getType());
        stepNo = getIntent().getIntExtra(STEP_NO_KEY, 0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        stepDetailsFragment = new StepDetailsFragment();
        fragmentManager.beginTransaction()
                .add(R.id.recyclerViewFrame, stepDetailsFragment)
                .commit();
        stepDetailsFragment.setData(steps, stepNo);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onLayoutSwipe(int stepNo) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putInt(getString(R.string.pref_lastClicked), stepNo).apply();
    }
}