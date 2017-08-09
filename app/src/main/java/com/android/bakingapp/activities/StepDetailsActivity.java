package com.android.bakingapp.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.bakingapp.R;
import com.android.bakingapp.fragments.StepDetailsFragment;
import com.android.bakingapp.models.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import static com.android.bakingapp.activities.MainActivity.STEP_KEY;
import static com.android.bakingapp.activities.MainActivity.STEP_NO_KEY;


public class StepDetailsActivity extends AppCompatActivity {

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
}