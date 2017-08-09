package com.android.bakingapp.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.android.bakingapp.models.Ingredient;
import com.android.bakingapp.models.Recipe;
import com.android.bakingapp.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader extends AsyncTaskLoader<Object> {
    private List<Recipe> list;

    public DataLoader(Context context) {
        super(context);
    }

    @Override
    public Object loadInBackground() {
        list = new ArrayList<>();

        try {
            getRecipes();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        Object object = list;
        return object;
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

            list.add(new Recipe(id, name, getIngredients(ingredients), getSteps(steps), servings));
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
