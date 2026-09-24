package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnViewSuggested;
    private TextView tvPantryCount;
    private TextView tvReadyCount;
    private TextView tvReadyMessage;
    private Button navHome;
    private Button navPantry;
    private Button navRecipes;
    private Button navSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnViewSuggested = findViewById(R.id.btnViewSuggested);
        tvPantryCount = findViewById(R.id.tvPantryCount);
        tvReadyCount = findViewById(R.id.tvReadyCount);
        tvReadyMessage = findViewById(R.id.tvReadyMessage);

        updateDashboard();

        navHome = findViewById(R.id.navHome);
        navPantry = findViewById(R.id.navPantry);
        navRecipes = findViewById(R.id.navRecipes);
        navSettings = findViewById(R.id.navSettings);

        btnViewSuggested.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SuggestedRecipesActivity.class);
            startActivity(intent);
        });

        navHome.setOnClickListener(v -> {
            // Already on Home, so no action is needed
        });

        navPantry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyPantryActivity.class);
            startActivity(intent);
        });

        navRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipesActivity.class);
            startActivity(intent);
        });

        navSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void updateDashboard() {

        AppDatabase database = AppDatabase.getDatabase(this);
        PantryItemDao pantryItemDao = database.pantryItemDao();

        java.util.List<PantryItem> pantryItems = pantryItemDao.getAllItems();
        java.util.List<Recipe> recipes = database.recipeDao().getAllRecipes();

        int pantryCount = pantryItems.size();
        tvPantryCount.setText(String.valueOf(pantryCount));

        int readyCount = 0;

        for (Recipe recipe : recipes) {
            if (SuggestedRecipesActivity.canMakeRecipe(recipe, pantryItems)) {
                readyCount++;
            }
        }

        tvReadyCount.setText(String.valueOf(readyCount));

        if (readyCount == 0) {
            tvReadyMessage.setText(
                    "You don't have all the ingredients for a complete recipe yet."
            );
        } else if (readyCount == 1) {
            tvReadyMessage.setText(
                    "You currently have 1 recipe ready to make from your pantry."
            );
        } else {
            tvReadyMessage.setText(
                    "You currently have " + readyCount +
                            " recipes ready to make from your pantry."
            );
        }
    }

        @Override
        protected void onResume() {
            super.onResume();

            if (tvPantryCount != null) {
                updateDashboard();
            }
        }
    }
