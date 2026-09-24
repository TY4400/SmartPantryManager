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

    private RecipeDao recipeDao;
    private String selectedCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipes);

        AppDatabase database = AppDatabase.getDatabase(this);
        recipeDao = database.recipeDao();

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
        if (recipeDao.getRecipeCount() == 0) {
            recipeList.add(new Recipe(
                    "Avocado Toast",
                    "Breakfast",
                    "10 mins",
                    "Bread, Avocado, Lemon Juice, Salt, Black Pepper, Olive Oil",
                    "Bread|2|Items;Avocado|1|Items;Lemon Juice|10|ml;Salt|1|g;Black Pepper|1|g;Olive Oil|5|ml",
                    "Toast the bread until golden. Cut the avocado in half, remove the pit and mash the flesh in a bowl. Mix in a little lemon juice, salt and black pepper. Spread the avocado mixture over the toast and finish with a light drizzle of olive oil."
            ));

            recipeList.add(new Recipe(
                    "Chicken Salad",
                    "Lunch",
                    "20 mins",
                    "Chicken Breast, Lettuce, Tomato, Cucumber, Red Onion, Olive Oil, Lemon Juice, Salt, Black Pepper",
                    "Chicken Breast|200|g;Lettuce|100|g;Tomato|1|Items;Cucumber|1|Items;Red Onion|1|Items;Olive Oil|15|ml;Lemon Juice|10|ml;Salt|1|g;Black Pepper|1|g",
                    "Season the chicken breast with salt and black pepper. Cook it in a pan until golden and fully cooked, then slice it into strips. Chop the lettuce, tomato, cucumber and red onion and place them in a bowl. Add the sliced chicken. Drizzle with olive oil and lemon juice, toss everything together and serve."
            ));

            recipeList.add(new Recipe(
                    "Vegetable Tomato Pasta",
                    "Dinner",
                    "30 mins",
                    "Pasta, Tomato Pasta Sauce, Onion, Garlic, Bell Pepper, Carrot, Olive Oil, Italian Herbs, Salt, Black Pepper",
                    "Pasta|200|g;Tomato Pasta Sauce|200|ml;Onion|1|Items;Garlic|2|Items;Bell Pepper|1|Items;Carrot|1|Items;Olive Oil|15|ml;Italian Herbs|2|g;Salt|1|g;Black Pepper|1|g",
                    "Cook the pasta according to the packet instructions and drain. Heat olive oil in a pan and sauté the onion and garlic until softened. Add the bell pepper and carrot and cook until tender. Stir in the tomato pasta sauce and Italian herbs, then simmer for a few minutes. Add the cooked pasta, mix well, season with salt and black pepper and serve."
            ));

            recipeList.add(new Recipe(
                    "Scrambled Eggs",
                    "Breakfast",
                    "10 mins",
                    "Eggs, Milk, Butter, Salt, Black Pepper",
                    "Eggs|2|Items;Milk|50|ml;Butter|10|g;Salt|1|g;Black Pepper|1|g",
                    "Crack the eggs into a bowl, add a splash of milk and season with salt and black pepper. Whisk until combined. Melt the butter in a pan over low to medium heat. Pour in the egg mixture and gently stir with a spatula until the eggs are soft and cooked through. Remove from the heat and serve immediately."
            ));


            recipeList.add(new Recipe(
                    "Chicken Rice Bowl",
                    "Dinner",
                    "25 mins",
                    "Chicken Breast, Rice, Carrot, Bell Pepper, Onion, Garlic, Soy Sauce, Cooking Oil, Salt, Black Pepper",
                    "Chicken Breast|200|g;Rice|150|g;Carrot|1|Items;Bell Pepper|1|Items;Onion|1|Items;Garlic|2|Items;Soy Sauce|15|ml;Cooking Oil|15|ml;Salt|1|g;Black Pepper|1|g",
                    "Cook the rice according to the packet instructions. Cut the chicken breast into bite-sized pieces and season lightly with salt and black pepper. Heat the oil in a pan and cook the chicken until golden and fully cooked. Add the onion, carrot, bell pepper and garlic and sauté until the vegetables are tender. Stir in the soy sauce and cook for another minute. Serve the chicken and vegetables over the cooked rice."
            ));

            recipeList.add(new Recipe(
                    "Fluffy Pancakes",
                    "Breakfast",
                    "20 mins",
                    "Flour, Milk, Egg, Sugar, Baking Powder, Butter, Maple Syrup",
                    "Flour|150|g;Milk|200|ml;Egg|1|Items;Sugar|20|g;Baking Powder|5|g;Butter|20|g;Maple Syrup|20|ml",
                    "Mix the flour, sugar and baking powder in a bowl. In a separate bowl, whisk the egg and milk together. Gradually combine the wet and dry ingredients until a smooth batter forms. Melt a little butter in a frying pan and pour in small portions of batter. Cook until bubbles appear, flip and cook until golden. Serve warm with maple syrup."
            ));

            recipeList.add(new Recipe(
                    "French Toast",
                    "Breakfast",
                    "15 mins",
                    "Bread, Eggs, Milk, Cinnamon, Butter, Honey",
                    "Bread|2|Items;Eggs|2|Items;Milk|100|ml;Cinnamon|2|g;Butter|15|g;Honey|15|ml",
                    "Whisk the eggs, milk and cinnamon together in a shallow bowl. Dip each slice of bread into the mixture, coating both sides. Melt the butter in a frying pan over medium heat and cook the bread until golden on both sides. Serve warm with a drizzle of honey."
            ));

            recipeList.add(new Recipe(
                    "Chicken Alfredo",
                    "Dinner",
                    "35 mins",
                    "Fettuccine Pasta, Chicken Breast, Cooking Cream, Parmesan Cheese, Garlic, Butter, Olive Oil, Salt, Black Pepper",
                    "Fettuccine Pasta|200|g;Chicken Breast|200|g;Cooking Cream|200|ml;Parmesan Cheese|50|g;Garlic|2|Items;Butter|20|g;Olive Oil|10|ml;Salt|1|g;Black Pepper|1|g",
                    "Cook the fettuccine according to the packet instructions. Season the chicken with salt and black pepper, then cook it in olive oil until golden and fully cooked. Remove and slice the chicken. Melt the butter in the same pan, add the garlic and cook briefly. Add the cooking cream and Parmesan cheese and stir until smooth. Add the pasta and chicken to the sauce, toss together and serve."
            ));

            recipeList.add(new Recipe(
                    "Spaghetti Bolognese",
                    "Dinner",
                    "40 mins",
                    "Spaghetti, Beef Mince, Tomato Pasta Sauce, Onion, Garlic, Carrot, Olive Oil, Italian Herbs, Parmesan Cheese",
                    "Spaghetti|200|g;Beef Mince|250|g;Tomato Pasta Sauce|250|ml;Onion|1|Items;Garlic|2|Items;Carrot|1|Items;Olive Oil|15|ml;Italian Herbs|2|g;Parmesan Cheese|30|g",
                    "Cook the spaghetti according to the packet instructions. Heat the olive oil in a pan and sauté the onion, garlic and carrot until softened. Add the beef mince and cook until browned. Stir in the tomato pasta sauce and Italian herbs and simmer until the sauce thickens. Serve the Bolognese sauce over the spaghetti and finish with Parmesan cheese."
            ));

            recipeList.add(new Recipe(
                    "Chicken Quesadillas",
                    "Lunch",
                    "25 mins",
                    "Tortilla Wraps, Chicken Breast, Cheddar Cheese, Bell Pepper, Onion, Cooking Oil, Salsa",
                    "Tortilla Wraps|2|Items;Chicken Breast|150|g;Cheddar Cheese|80|g;Bell Pepper|1|Items;Onion|1|Items;Cooking Oil|10|ml;Salsa|30|g",
                    "Cut the chicken into small pieces and cook it in a pan with the cooking oil until fully cooked. Add the sliced bell pepper and onion and cook until softened. Place a tortilla in a clean pan, add cheese, chicken and vegetables, then top with another tortilla. Cook until golden underneath, carefully flip and cook the other side until the cheese has melted. Slice into wedges and serve with salsa."
            ));

            recipeList.add(new Recipe(
                    "Creamy Garlic Chicken",
                    "Dinner",
                    "35 mins",
                    "Chicken Breast, Cooking Cream, Garlic, Parmesan Cheese, Butter, Olive Oil, Salt, Black Pepper",
                    "Chicken Breast|200|g;Cooking Cream|200|ml;Garlic|3|Items;Parmesan Cheese|40|g;Butter|20|g;Olive Oil|10|ml;Salt|1|g;Black Pepper|1|g",
                    "Season the chicken breast with salt and black pepper. Heat the olive oil in a pan and cook the chicken until golden and fully cooked, then remove it from the pan. Melt the butter in the same pan and gently cook the garlic. Add the cooking cream and Parmesan cheese and stir until the sauce becomes smooth and creamy. Return the chicken to the pan and simmer for a few minutes before serving."
            ));

            recipeList.add(new Recipe(
                    "Beef Tacos",
                    "Dinner",
                    "30 mins",
                    "Taco Shells, Beef Mince, Lettuce, Tomato, Cheddar Cheese, Onion, Taco Seasoning, Salsa",
                    "Taco Shells|4|Items;Beef Mince|250|g;Lettuce|50|g;Tomato|1|Items;Cheddar Cheese|60|g;Onion|1|Items;Taco Seasoning|10|g;Salsa|40|g",
                    "Cook the beef mince and chopped onion in a frying pan until the beef is browned. Add the taco seasoning and a small splash of water, then cook until well combined. Warm the taco shells and fill them with the seasoned beef. Top with shredded lettuce, chopped tomato, Cheddar cheese and salsa."
            ));

            recipeList.add(new Recipe(
                    "Creamy Mac and Cheese",
                    "Lunch",
                    "25 mins",
                    "Macaroni, Cheddar Cheese, Milk, Butter, Flour, Salt, Black Pepper",
                    "Macaroni|200|g;Cheddar Cheese|120|g;Milk|250|ml;Butter|25|g;Flour|20|g;Salt|1|g;Black Pepper|1|g",
                    "Cook the macaroni according to the packet instructions and drain. Melt the butter in a saucepan and stir in the flour. Gradually add the milk while stirring until the sauce becomes smooth and thick. Add the Cheddar cheese and stir until melted. Season with salt and black pepper, then mix in the cooked macaroni until evenly coated."
            ));

            recipeList.add(new Recipe(
                    "Chicken Curry",
                    "Dinner",
                    "40 mins",
                    "Chicken Breast, Rice, Onion, Garlic, Curry Powder, Coconut Milk, Tomato, Cooking Oil, Salt",
                    "Chicken Breast|200|g;Rice|150|g;Onion|1|Items;Garlic|2|Items;Curry Powder|10|g;Coconut Milk|200|ml;Tomato|1|Items;Cooking Oil|15|ml;Salt|1|g",
                    "Cook the rice according to the packet instructions. Heat the cooking oil in a pan and sauté the onion and garlic until softened. Add the chicken and cook until lightly browned. Stir in the curry powder and chopped tomato, then add the coconut milk. Simmer until the chicken is fully cooked and the sauce has thickened. Season with salt and serve with the cooked rice."
            ));

            recipeList.add(new Recipe(
                    "Breakfast Burrito",
                    "Breakfast",
                    "20 mins",
                    "Tortilla Wrap, Eggs, Cheddar Cheese, Tomato, Bell Pepper, Onion, Butter, Salsa",
                    "Tortilla Wrap|1|Items;Eggs|2|Items;Cheddar Cheese|40|g;Tomato|1|Items;Bell Pepper|1|Items;Onion|1|Items;Butter|10|g;Salsa|20|g",
                    "Melt the butter in a frying pan and cook the chopped onion and bell pepper until softened. Add the eggs and gently scramble until cooked. Place the tortilla wrap on a plate and add the egg mixture, chopped tomato, Cheddar cheese and salsa. Fold in the sides and roll tightly to form a burrito."
            ));

            recipeList.add(new Recipe(
                    "Teriyaki Chicken",
                    "Dinner",
                    "30 mins",
                    "Chicken Breast, Rice, Soy Sauce, Honey, Garlic, Ginger, Broccoli, Cooking Oil",
                    "Chicken Breast|200|g;Rice|150|g;Soy Sauce|30|ml;Honey|20|ml;Garlic|2|Items;Ginger|5|g;Broccoli|100|g;Cooking Oil|10|ml",
                    "Cook the rice according to the packet instructions. Mix the soy sauce, honey, garlic and ginger together to make the teriyaki sauce. Cut the chicken into bite-sized pieces and cook it in the oil until golden. Add the broccoli and cook until tender. Pour in the teriyaki sauce and simmer until it thickens and coats the chicken. Serve over the cooked rice."
            ));

            recipeList.add(new Recipe(
                    "Cheesy Beef Pasta Bake",
                    "Dinner",
                    "40 mins",
                    "Pasta, Beef Mince, Tomato Pasta Sauce, Onion, Garlic, Cheddar Cheese, Italian Herbs, Olive Oil",
                    "Pasta|200|g;Beef Mince|250|g;Tomato Pasta Sauce|250|ml;Onion|1|Items;Garlic|2|Items;Cheddar Cheese|100|g;Italian Herbs|2|g;Olive Oil|15|ml",
                    "Cook the pasta until just tender and drain. Heat the olive oil in a pan and sauté the onion and garlic. Add the beef mince and cook until browned. Stir in the tomato pasta sauce and Italian herbs. Mix the sauce with the cooked pasta, transfer to a baking dish and top with Cheddar cheese. Bake until the cheese is melted and golden."
            ));

            recipeList.add(new Recipe(
                    "Loaded Baked Potato",
                    "Lunch",
                    "45 mins",
                    "Potato, Cheddar Cheese, Butter, Sour Cream, Spring Onion, Salt, Black Pepper",
                    "Potato|1|Items;Cheddar Cheese|50|g;Butter|15|g;Sour Cream|30|g;Spring Onion|1|Items;Salt|1|g;Black Pepper|1|g",
                    "Bake the potato until the inside is soft and the skin is crisp. Carefully cut it open and fluff the inside with a fork. Add the butter, Cheddar cheese and sour cream. Season with salt and black pepper and finish with chopped spring onion."
            ));

            recipeList.add(new Recipe(
                    "Shakshuka",
                    "Breakfast",
                    "30 mins",
                    "Eggs, Tomatoes, Bell Pepper, Onion, Garlic, Paprika, Olive Oil, Salt, Black Pepper",
                    "Eggs|2|Items;Tomatoes|2|Items;Bell Pepper|1|Items;Onion|1|Items;Garlic|2|Items;Paprika|5|g;Olive Oil|15|ml;Salt|1|g;Black Pepper|1|g",
                    "Heat the olive oil in a pan and sauté the onion, garlic and bell pepper until softened. Add the chopped tomatoes and paprika and simmer until a thick sauce forms. Make two small wells in the sauce and crack an egg into each one. Cover the pan and cook until the egg whites are set but the yolks remain soft. Season with salt and black pepper."
            ));

            recipeList.add(new Recipe(
                    "Garlic Butter Shrimp Pasta",
                    "Dinner",
                    "30 mins",
                    "Spaghetti, Shrimp, Garlic, Butter, Lemon Juice, Parmesan Cheese, Olive Oil, Salt, Black Pepper",
                    "Spaghetti|200|g;Shrimp|200|g;Garlic|3|Items;Butter|25|g;Lemon Juice|15|ml;Parmesan Cheese|40|g;Olive Oil|10|ml;Salt|1|g;Black Pepper|1|g",
                    "Cook the spaghetti according to the packet instructions and drain. Heat the olive oil and butter in a pan and gently cook the garlic. Add the shrimp and cook until pink and fully cooked. Stir in the lemon juice, then add the spaghetti and toss until coated in the garlic butter sauce. Season with salt and black pepper and finish with Parmesan cheese."
            ));

            for (Recipe recipe : recipeList) {
                recipeDao.insert(recipe);
            }

        }
        recipeList.clear();
        recipeList.addAll(recipeDao.getAllRecipes());

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