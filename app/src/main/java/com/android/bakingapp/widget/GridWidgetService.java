package com.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.bakingapp.R;
import com.android.bakingapp.activities.MainActivity;
import com.android.bakingapp.models.Ingredient;
import com.android.bakingapp.models.Recipe;
import com.android.bakingapp.models.Step;
import com.android.bakingapp.network.RecipeAPI;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<Recipe> data;


    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
    }


    @Override
    public void onDataSetChanged() {
        data = new ArrayList<>();

        try {
            getRecipes();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        data.clear();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (data == null || data.size() == 0) {
            return null;
        }

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_row_item);
        remoteViews.setTextViewText(R.id.widgetTitle, data.get(position).name);

        String widgetContentString = "";
        List<Ingredient> ingredientList = data.get(position).ingredients;
        for (Ingredient ingredient : ingredientList) {
            widgetContentString += " - "
                    + ingredient.ingredient + " "
                    + ingredient.quantity + " "
                    + ingredient.measure + "\n";
        }
        remoteViews.setTextViewText(R.id.widgetContent, widgetContentString);

        String jsonString = new Gson().toJson(data.get(position));
        Bundle extras = new Bundle();
        extras.putString(MainActivity.RECIPE_KEY, jsonString);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.row, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void getRecipes() throws IOException, JSONException {
        String jsonString = RecipeAPI.getQueryJSONObject();

        JSONArray jsonArray = new JSONArray(jsonString);
        int recipeAmount = jsonArray.length();
        for (int i = 0; recipeAmount > i; i++) {
            JSONObject jsonBookData = jsonArray.getJSONObject(i);

            String id = jsonBookData.getString("id");
            String name = jsonBookData.getString("name");
            JSONArray ingredients = jsonBookData.getJSONArray("ingredients");
            JSONArray steps = jsonBookData.getJSONArray("steps");
            String servings = jsonBookData.getString("servings");

            data.add(new Recipe(id, name, getIngredients(ingredients), getSteps(steps), servings));
        }

    }

    private List<Ingredient> getIngredients(JSONArray jsonArray) throws JSONException {
        String quantity = "", measure = "", ingredient = "";
        List<Ingredient> list = new ArrayList<>();
        for (int i = 0; jsonArray.length() > i; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (jsonObject.has("quantity")) {
                quantity = jsonObject.getString("quantity");
            }

            if (jsonObject.has("measure")) {
                measure = jsonObject.getString("measure");
            }

            if (jsonObject.has("ingredient")) {
                ingredient = jsonObject.getString("ingredient");
            }

            list.add(new Ingredient(quantity, measure, ingredient));
        }
        return list;
    }

    private List<Step> getSteps(JSONArray jsonArray) throws JSONException {
        String id = "", shortDescription = "", description = "", videoURL = "";
        List<Step> list = new ArrayList<>();
        for (int i = 0; jsonArray.length() > i; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (jsonObject.has("id")) {
                id = jsonObject.getString("id");
            }

            if (jsonObject.has("shortDescription")) {
                shortDescription = jsonObject.getString("shortDescription");
            }

            if (jsonObject.has("description")) {
                description = jsonObject.getString("description");
            }

            if (jsonObject.has("videoURL")) {
                videoURL = jsonObject.getString("videoURL");
            }

            list.add(new Step(id, shortDescription, description, videoURL));
        }
        return list;
    }


}

