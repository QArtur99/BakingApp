package com.android.bakingapp.models;

/**
 * Created by ART_F on 2017-08-06.
 */

public class Ingredient {

    public String quantity;
    public String measure;
    public String ingredient;

    public Ingredient(String quantity, String measure, String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }
}
