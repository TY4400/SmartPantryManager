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

        // Change icon depending on recipe category
        switch (recipe.getCategory()) {

            case "Breakfast":
                tvRecipeIcon.setText("🍳");
                break;

            case "Lunch":
                tvRecipeIcon.setText("🥗");
                break;

            case "Dinner":
                tvRecipeIcon.setText("🍲");
                break;

            default:
                tvRecipeIcon.setText("🍽️");
                break;
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
