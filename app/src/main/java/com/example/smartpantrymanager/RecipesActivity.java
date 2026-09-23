package com.example.smartpantrymanager;

import android.os.Bundle;

import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import java.util.ArrayList;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipesActivity extends AppCompatActivity {
    private ListView listRecipes;
    private ArrayList<Recipe> recipeList;
    private RecipeAdapter recipeAdapter;
    private TextView tvRecipeCount;
    private Button btnAll;
    private Button btnBreakfast;
    private Button btnLunch;
    private Button btnDinner;
    private ArrayList<Recipe> displayedRecipes;
    private EditText etSearchRecipes;
    private TextView btnClearSearch;

    private String selectedCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipes);

        listRecipes = findViewById(R.id.listRecipes);
        tvRecipeCount = findViewById(R.id.tvRecipeCount);
        btnAll = findViewById(R.id.btnAllRecipes);
        btnBreakfast = findViewById(R.id.btnBreakfast);
        btnLunch = findViewById(R.id.btnLunch);
        btnDinner = findViewById(R.id.btnDinner);
        etSearchRecipes = findViewById(R.id.etSearchRecipes);
        btnClearSearch = findViewById(R.id.btnClearSearch);

        recipeList = new ArrayList<>();

        displayedRecipes = new ArrayList<>();

        recipeAdapter = new RecipeAdapter(
                this,
                displayedRecipes
        );

        listRecipes.setAdapter(recipeAdapter);

        // Starter recipes
        recipeList.add(new Recipe(
                "Avocado Toast",
                "Breakfast",
                "10 mins",
                "Bread, Avocado, Lemon Juice, Salt, Black Pepper, Olive Oil",
                "Toast the bread until golden. Cut the avocado in half, remove the pit and mash the flesh in a bowl. Mix in a little lemon juice, salt and black pepper. Spread the avocado mixture over the toast and finish with a light drizzle of olive oil."
        ));

        recipeList.add(new Recipe(
                "Chicken Salad",
                "Lunch",
                "20 mins",
                "Chicken Breast, Lettuce, Tomato, Cucumber, Red Onion, Olive Oil, Lemon Juice, Salt, Black Pepper",
                "Season the chicken breast with salt and black pepper. Cook it in a pan until golden and fully cooked, then slice it into strips. Chop the lettuce, tomato, cucumber and red onion and place them in a bowl. Add the sliced chicken. Drizzle with olive oil and lemon juice, toss everything together and serve."
        ));

        recipeList.add(new Recipe(
                "Vegetable Tomato Pasta",
                "Dinner",
                "30 mins",
                "Pasta, Tomato Pasta Sauce, Onion, Garlic, Bell Pepper, Carrot, Olive Oil, Italian Herbs, Salt, Black Pepper",
                "Cook the pasta according to the packet instructions and drain. Heat olive oil in a pan and sauté the onion and garlic until softened. Add the bell pepper and carrot and cook until tender. Stir in the tomato pasta sauce and Italian herbs, then simmer for a few minutes. Add the cooked pasta, mix well, season with salt and black pepper and serve."
        ));

        recipeList.add(new Recipe(
                "Scrambled Eggs",
                "Breakfast",
                "10 mins",
                "Eggs, Milk, Butter, Salt, Black Pepper",
                "Crack the eggs into a bowl, add a splash of milk and season with salt and black pepper. Whisk until combined. Melt the butter in a pan over low to medium heat. Pour in the egg mixture and gently stir with a spatula until the eggs are soft and cooked through. Remove from the heat and serve immediately."
        ));

        recipeList.add(new Recipe(
                "Chicken Rice Bowl",
                "Dinner",
                "25 mins",
                "Chicken Breast, Rice, Carrot, Bell Pepper, Onion, Garlic, Soy Sauce, Cooking Oil, Salt, Black Pepper",
                "Cook the rice according to the packet instructions. Cut the chicken breast into bite-sized pieces and season lightly with salt and black pepper. Heat the oil in a pan and cook the chicken until golden and fully cooked. Add the onion, carrot, bell pepper and garlic and sauté until the vegetables are tender. Stir in the soy sauce and cook for another minute. Serve the chicken and vegetables over the cooked rice."

        ));

        displayedRecipes.addAll(recipeList);
        recipeAdapter.notifyDataSetChanged();

        tvRecipeCount.setText(displayedRecipes.size() + " recipes");

        btnAll.setOnClickListener(v -> {
            selectedCategory = "All";
            filterRecipes("All");
        });

        btnBreakfast.setOnClickListener(v -> {
            selectedCategory = "Breakfast";
            filterRecipes("Breakfast");
        });

        btnLunch.setOnClickListener(v -> {
            selectedCategory = "Lunch";
            filterRecipes("Lunch");
        });

        btnDinner.setOnClickListener(v -> {
            selectedCategory = "Dinner";
            filterRecipes("Dinner");
        });

        etSearchRecipes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    btnClearSearch.setVisibility(android.view.View.VISIBLE);
                } else {
                    btnClearSearch.setVisibility(android.view.View.GONE);
                }

                String searchText = s.toString().trim().toLowerCase();

                displayedRecipes.clear();

                for (Recipe recipe : recipeList) {

                    boolean matchesSearch =
                            recipe.getName().toLowerCase().contains(searchText);

                    boolean matchesCategory =
                            selectedCategory.equals("All") ||
                                    recipe.getCategory().equals(selectedCategory);

                    if (matchesSearch && matchesCategory) {
                        displayedRecipes.add(recipe);
                    }
                }

                recipeAdapter.notifyDataSetChanged();

                tvRecipeCount.setText(
                        displayedRecipes.size() + " recipes"
                );
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnClearSearch.setOnClickListener(v -> {
            etSearchRecipes.setText("");
        });

        recipeAdapter.notifyDataSetChanged();

        TextView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void filterRecipes(String category) {

        selectedCategory = category;

        String searchText = etSearchRecipes.getText()
                .toString()
                .trim()
                .toLowerCase();

        displayedRecipes.clear();

        for (Recipe recipe : recipeList) {

            boolean matchesCategory =
                    category.equals("All") ||
                            recipe.getCategory().equals(category);

            boolean matchesSearch =
                    recipe.getName()
                            .toLowerCase()
                            .contains(searchText);

            if (matchesCategory && matchesSearch) {
                displayedRecipes.add(recipe);
            }
        }

        recipeAdapter.notifyDataSetChanged();

        int count = displayedRecipes.size();

        tvRecipeCount.setText(
                count + (count == 1 ? " recipe" : " recipes")
        );

        updateCategoryButtons();
    }

    private void updateCategoryButtons() {

        int selectedColor = android.graphics.Color.parseColor("#3F7336");
        int normalColor = android.graphics.Color.parseColor("#DDE9D7");

        int selectedTextColor = android.graphics.Color.WHITE;
        int normalTextColor = android.graphics.Color.parseColor("#294E25");

        Button[] buttons = {
                btnAll,
                btnBreakfast,
                btnLunch,
                btnDinner
        };

        String[] categories = {
                "All",
                "Breakfast",
                "Lunch",
                "Dinner"
        };

        for (int i = 0; i < buttons.length; i++) {

            if (selectedCategory.equals(categories[i])) {
                buttons[i].setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(selectedColor)
                );
                buttons[i].setTextColor(selectedTextColor);

            } else {
                buttons[i].setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(normalColor)
                );
                buttons[i].setTextColor(normalTextColor);
            }
        }
    }
}