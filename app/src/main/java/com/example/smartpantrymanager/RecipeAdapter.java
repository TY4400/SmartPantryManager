package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    private final Context context;
    private final List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        super(context, R.layout.item_recipe, recipes);
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public View getView(
            int position,
            @Nullable View convertView,
            @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_recipe, parent, false);
        }

        Recipe recipe = recipes.get(position);

        TextView tvRecipeIcon =
                convertView.findViewById(R.id.tvRecipeIcon);

        TextView tvRecipeName =
                convertView.findViewById(R.id.tvRecipeName);

        TextView tvRecipeCategory =
                convertView.findViewById(R.id.tvRecipeCategory);

        TextView tvCookingTime =
                convertView.findViewById(R.id.tvCookingTime);

        tvRecipeName.setText(recipe.getName());
        tvRecipeCategory.setText(recipe.getCategory());
        tvCookingTime.setText("⏱ " + recipe.getCookingTime());

        // Choose icon based on the actual recipe
        String recipeName = recipe.getName().toLowerCase();

        if (recipeName.contains("pancake")) {
            tvRecipeIcon.setText("🥞");

        } else if (recipeName.contains("french toast") ||
                recipeName.contains("avocado toast")) {
            tvRecipeIcon.setText("🍞");

        } else if (recipeName.contains("scrambled eggs") ||
                recipeName.contains("shakshuka")) {
            tvRecipeIcon.setText("🍳");

        } else if (recipeName.contains("burrito") ||
                recipeName.contains("quesadilla")) {
            tvRecipeIcon.setText("🌯");

        } else if (recipeName.contains("taco")) {
            tvRecipeIcon.setText("🌮");

        } else if (recipeName.contains("alfredo") ||
                recipeName.contains("bolognese") ||
                recipeName.contains("pasta") ||
                recipeName.contains("mac and cheese")) {
            tvRecipeIcon.setText("🍝");

        } else if (recipeName.contains("curry")) {
            tvRecipeIcon.setText("🍛");

        } else if (recipeName.contains("salad")) {
            tvRecipeIcon.setText("🥗");

        } else if (recipeName.contains("potato")) {
            tvRecipeIcon.setText("🥔");

        } else if (recipeName.contains("teriyaki") ||
                recipeName.contains("rice bowl")) {
            tvRecipeIcon.setText("🍚");

        } else if (recipeName.contains("tuna")) {
            tvRecipeIcon.setText("🥪");

        } else if (recipeName.contains("chicken")) {
            tvRecipeIcon.setText("🍗");

        } else if (recipeName.contains("beef")) {
            tvRecipeIcon.setText("🥩");

        } else {
            tvRecipeIcon.setText("🍽️");
        }

        convertView.setOnClickListener(v -> {

            Intent intent = new Intent(context, RecipeDetailsActivity.class);

            intent.putExtra("name", recipe.getName());
            intent.putExtra("category", recipe.getCategory());
            intent.putExtra("cookingTime", recipe.getCookingTime());
            intent.putExtra("ingredients", recipe.getIngredients());
            intent.putExtra("instructions", recipe.getInstructions());

            context.startActivity(intent);
        });

        return convertView;
    }
}
