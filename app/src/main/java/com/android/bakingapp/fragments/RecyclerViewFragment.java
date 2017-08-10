package com.android.bakingapp.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.bakingapp.R;
import com.android.bakingapp.activities.MainActivity;
import com.android.bakingapp.adapters.RecipeAdapter;
import com.android.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.bakingapp.activities.MainActivity.isLandscape;

/**
 * Created by ART_F on 2017-08-06.
 */

public class RecyclerViewFragment extends Fragment implements RecipeAdapter.ListItemClickListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.loadingIndicator) ProgressBar loadingIndicator;
    @BindView(R.id.emptyView) RelativeLayout emptyView;
    @BindView(R.id.empty_subtitle_text) TextView emptySubtitleText;
    @BindView(R.id.empty_title_text) TextView emptyTitleText;

    private OnImageClickListener mCallback;
    private RecipeAdapter recipeAdapter;

    public RecyclerViewFragment() {
    }

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
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, rootView);
        if (checkConnection()) {
            MainActivity mainActiivty = ((MainActivity) getActivity());
            emptyView.setVisibility(View.GONE);
            setAdapter(new ArrayList<Recipe>());
            getActivity().getSupportLoaderManager().initLoader(0, null, mainActiivty).forceLoad();
        } else {
            setInfoNoConnection();
        }
        return rootView;
    }

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    private void setInfoNoConnection() {
        recyclerView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        emptyTitleText.setText(getString(R.string.no_connection));
        emptySubtitleText.setText(getString(R.string.no_connection_sub_text));
    }

    public void setAdapter(List<Recipe> recipeList) {
        GridLayoutManager layoutManager;
        if (isLandscape) {
            layoutManager = new GridLayoutManager(getContext(), 3);
        } else {
            layoutManager = new GridLayoutManager(getContext(), 1);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recipeAdapter = new RecipeAdapter(getContext(), recipeList, this);
        recyclerView.setAdapter(recipeAdapter);
    }

    public void onLoadFinished(final List<Recipe> data) {
        loadingIndicator.setVisibility(View.GONE);
        recipeAdapter.clearMovies();

        if (data != null && !data.isEmpty()) {

            emptyView.setVisibility(View.GONE);
            recipeAdapter.setMovies(data);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            emptyTitleText.setText(getString(R.string.server_problem));
            emptySubtitleText.setText(getString(R.string.server_problem_sub_text));
        }

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Recipe movie = (Recipe) recipeAdapter.getDataAtPosition(clickedItemIndex);
        mCallback.onImageSelected(movie);
    }


    public interface OnImageClickListener {
        void onImageSelected(Recipe movie);
    }
}
