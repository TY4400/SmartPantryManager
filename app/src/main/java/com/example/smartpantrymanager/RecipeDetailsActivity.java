package com.example.smartpantrymanager;

import android.os.Bundle;
import  android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeDetailsActivity extends AppCompatActivity {

    private TextView tvRecipeName;
    private TextView tvRecipeCategory;
    private TextView tvCookingTime;
    private TextView tvIngredients;
    private TextView tvInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_details);

        TextView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        tvRecipeName = findViewById(R.id.tvRecipeName);
        tvRecipeCategory = findViewById(R.id.tvRecipeCategory);
        tvCookingTime = findViewById(R.id.tvCookingTime);
        tvIngredients = findViewById(R.id.tvIngredients);
        tvInstructions = findViewById(R.id.tvInstructions);

        String name = getIntent().getStringExtra("name");
        String category = getIntent().getStringExtra("category");
        String cookingTime = getIntent().getStringExtra("cookingTime");
        String ingredients = getIntent().getStringExtra("ingredients");
        String instructions = getIntent().getStringExtra("instructions");

        tvRecipeName.setText(name);
        tvRecipeCategory.setText(category);
        tvCookingTime.setText("◷ " + cookingTime);
        tvIngredients.setText(ingredients);
        tvInstructions.setText(instructions);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}