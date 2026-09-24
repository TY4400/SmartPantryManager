package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText etUserName;
    private Spinner spinnerDefaultCategory;
    private Button btnSaveProfile;
    private Button navHome;
    private Button navPantry;
    private Button navRecipes;
    private Button navSettings;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etUserName = findViewById(R.id.etUserName);
        spinnerDefaultCategory = findViewById(R.id.spinnerDefaultCategory);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        navHome = findViewById(R.id.navHome);
        navPantry = findViewById(R.id.navPantry);
        navRecipes = findViewById(R.id.navRecipes);
        navSettings = findViewById(R.id.navSettings);

        Button btnBack = findViewById(R.id.btnBack);

        preferences = getSharedPreferences(
                "SmartPantrySettings",
                MODE_PRIVATE
        );

        String[] categories = {
                "All Recipes",
                "Breakfast",
                "Lunch",
                "Dinner"
        };

        ArrayAdapter<String> categoryAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        categoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerDefaultCategory.setAdapter(categoryAdapter);

        // Load previously saved settings
        String savedName = preferences.getString("userName", "");
        String savedCategory =
                preferences.getString("defaultCategory", "All Recipes");

        etUserName.setText(savedName);

        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(savedCategory)) {
                spinnerDefaultCategory.setSelection(i);
                break;
            }
        }

        btnSaveProfile.setOnClickListener(v -> {

            String userName =
                    etUserName.getText().toString().trim();

            String selectedCategory =
                    spinnerDefaultCategory.getSelectedItem().toString();

            if (userName.isEmpty()) {
                etUserName.setError("Please enter your name");
                etUserName.requestFocus();
                return;
            }

            preferences.edit()
                    .putString("userName", userName)
                    .putString("defaultCategory", selectedCategory)
                    .apply();

            Toast.makeText(
                    SettingsActivity.this,
                    "Settings saved successfully",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // BOTTOM NAVIGATION
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        navPantry.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MyPantryActivity.class);
            startActivity(intent);
            finish();
        });

        navRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, RecipesActivity.class);
            startActivity(intent);
            finish();
        });

        navSettings.setOnClickListener(v -> {
            // Already on Settings, so no action is needed
        });

        btnBack.setOnClickListener(v -> finish());
    }
}