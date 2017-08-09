package com.android.bakingapp.models;

import java.util.List;

/**
 * Created by ART_F on 2017-08-06.
 */

public class Recipe {
    public String id;
    public String name;
    public List<Ingredient> ingredients;
    public List<Step> steps;
    public String servings;

    public Recipe(String id, String name, List<Ingredient> ingredients, List<Step> steps, String servings){
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
    }
}
