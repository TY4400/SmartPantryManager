package com.example.smartpantrymanager;

public class Recipe {

    private String name;
    private String category;
    private String cookingTime;
    private String ingredients;
    private String instructions;

    public Recipe(String name,
                  String category,
                  String cookingTime,
                  String ingredients,
                  String instructions) {

        this.name = name;
        this.category = category;
        this.cookingTime = cookingTime;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }
}