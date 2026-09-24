package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private ListView listSuggestedRecipes;
    private TextView tvSuggestedCount;
    private LinearLayout layoutEmptyState;
    private Button btnBack;
    private Button btnGoToPantry;
    private PantryItemDao pantryItemDao;
    private RecipeDao recipeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        AppDatabase database = AppDatabase.getDatabase(this);
        pantryItemDao = database.pantryItemDao();
        recipeDao = database.recipeDao();

        listSuggestedRecipes = findViewById(R.id.listSuggestedRecipes);
        tvSuggestedCount = findViewById(R.id.tvSuggestedCount);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        btnBack = findViewById(R.id.btnBack);
        btnGoToPantry = findViewById(R.id.btnGoToPantry);

        loadSuggestedRecipes();


        btnBack.setOnClickListener(v -> finish());

        btnGoToPantry.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    MyPantryActivity.class
            );
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
    }

    public static boolean canMakeRecipe(Recipe recipe, java.util.List<PantryItem> pantryItems) {

        String requirements = recipe.getIngredientRequirements();

        if (requirements == null || requirements.trim().isEmpty()) {
            return false;
        }

        String[] requiredIngredients = requirements.split(";");

        for (String requirement : requiredIngredients) {

            String[] parts = requirement.split("\\|");

            if (parts.length != 3) {
                return false;
            }

            String requiredName = parts[0].trim();
            double requiredQuantity;

            try {
                requiredQuantity = Double.parseDouble(parts[1].trim());
            } catch (NumberFormatException e) {
                return false;
            }

            String requiredUnit = parts[2].trim();

            boolean ingredientFound = false;

            for (PantryItem pantryItem : pantryItems) {

                if (normalizeIngredientName(pantryItem.getName())
                        .equals(normalizeIngredientName(requiredName))) {

                    ingredientFound = true;

                    if (!hasEnoughQuantity(
                            pantryItem,
                            requiredQuantity,
                            requiredUnit)) {

                        return false;
                    }

                    break;
                }
            }

            if (!ingredientFound) {
                return false;
            }
        }

        return true;
    }

    private static boolean hasEnoughQuantity(
            PantryItem pantryItem,
            double requiredQuantity,
            String requiredUnit) {

        double pantryQuantity = pantryItem.getQuantity();
        String pantryUnit = pantryItem.getUnit();

        if (pantryUnit == null || requiredUnit == null) {
            return false;
        }

        pantryUnit = pantryUnit.trim().toLowerCase();
        requiredUnit = requiredUnit.trim().toLowerCase();

        // Treat Item and Items as the same unit
        if (pantryUnit.equals("items")) {
            pantryUnit = "item";
        }

        if (requiredUnit.equals("items")) {
            requiredUnit = "item";
        }

        // Direct unit match
        if (pantryUnit.equals(requiredUnit)) {
            return pantryQuantity >= requiredQuantity;
        }

        // Convert kilograms to grams
        if (pantryUnit.equals("kg") && requiredUnit.equals("g")) {
            return pantryQuantity * 1000 >= requiredQuantity;
        }

        // Convert grams to kilograms
        if (pantryUnit.equals("g") && requiredUnit.equals("kg")) {
            return pantryQuantity >= requiredQuantity * 1000;
        }

        // Convert litres to millilitres
        if (pantryUnit.equals("l") && requiredUnit.equals("ml")) {
            return pantryQuantity * 1000 >= requiredQuantity;
        }

        // Convert millilitres to litres
        if (pantryUnit.equals("ml") && requiredUnit.equals("l")) {
            return pantryQuantity >= requiredQuantity * 1000;
        }

        return false;
    }

    private void loadSuggestedRecipes() {

        java.util.List<PantryItem> pantryItems =
                pantryItemDao.getAllItems();

        java.util.List<Recipe> allRecipes =
                recipeDao.getAllRecipes();

        java.util.ArrayList<Recipe> suggestedRecipes =
                new java.util.ArrayList<>();

        for (Recipe recipe : allRecipes) {

            if (canMakeRecipe(recipe, pantryItems)) {
                suggestedRecipes.add(recipe);
            }
        }

        int count = suggestedRecipes.size();

        tvSuggestedCount.setText(
                count + (count == 1 ? " recipe" : " recipes")
        );

        if (suggestedRecipes.isEmpty()) {

            listSuggestedRecipes.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);

        } else {

            layoutEmptyState.setVisibility(View.GONE);
            listSuggestedRecipes.setVisibility(View.VISIBLE);

            RecipeAdapter adapter = new RecipeAdapter(
                    SuggestedRecipesActivity.this,
                    suggestedRecipes
            );

            listSuggestedRecipes.setAdapter(adapter);
        }
    }

    private static String normalizeIngredientName(String name) {

        if (name == null) {
            return "";
        }

        String normalized = name.trim().toLowerCase();

        // Handle simple singular/plural ingredient names
        if (normalized.endsWith("s") && normalized.length() > 1) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        return normalized;
    }
}