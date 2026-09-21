package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;

public class MyPantryActivity extends AppCompatActivity {

    private EditText etIngredient;
    private Button btnAddIngredient;
    private TextView btnBack;
    private ListView listPantry;
    private Spinner spinnerCategory;

    private Button btnAll;
    private Button btnFruit;
    private Button btnVegetable;
    private Button btnDairy;
    private Button btnOther;

    // Stores ALL pantry items
    private ArrayList<String> pantryItems;

    // Stores only the items currently being displayed
    private ArrayList<String> displayedItems;

    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pantry);

        // Connect Java to XML
        etIngredient = findViewById(R.id.etIngredient);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnBack = findViewById(R.id.btnBack);
        listPantry = findViewById(R.id.listPantry);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        btnAll = findViewById(R.id.btnAll);
        btnFruit = findViewById(R.id.btnFruit);
        btnVegetable = findViewById(R.id.btnVegetable);
        btnDairy = findViewById(R.id.btnDairy);
        btnOther = findViewById(R.id.btnOther);

        // Category dropdown used when ADDING an ingredient
        String[] categories = {
                "Select Category",
                "Fruit",
                "Vegetable",
                "Dairy",
                "Other"
        };

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        categoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCategory.setAdapter(categoryAdapter);

        // Load saved pantry items
        sharedPreferences =
                getSharedPreferences("PantryPrefs", MODE_PRIVATE);

        HashSet<String> savedItems =
                new HashSet<>(
                        sharedPreferences.getStringSet(
                                "pantryItems",
                                new HashSet<>()
                        )
                );

        pantryItems = new ArrayList<>(savedItems);
        displayedItems = new ArrayList<>(pantryItems);

        // Custom adapter using item_pantry.xml
        adapter = new ArrayAdapter<String>(
                this,
                R.layout.item_pantry,
                displayedItems
        ) {

            @Override
            public View getView(
                    int position,
                    View convertView,
                    ViewGroup parent) {

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext())
                            .inflate(R.layout.item_pantry, parent, false);
                }

                TextView tvItemIcon =
                        convertView.findViewById(R.id.tvItemIcon);

                TextView tvIngredientName =
                        convertView.findViewById(R.id.tvIngredientName);

                TextView tvCategory =
                        convertView.findViewById(R.id.tvCategory);

                TextView tvCategoryIcon =
                        convertView.findViewById(R.id.tvCategoryIcon);

                TextView btnDeleteItem =
                        convertView.findViewById(R.id.btnDeleteItem);

                String item = displayedItems.get(position);

                if (item.contains("|")) {

                    String[] parts = item.split("\\|");

                    String ingredient = parts[0];
                    String category = parts[1];

                    tvIngredientName.setText(ingredient);
                    tvCategory.setText(category);

                    // Set icon depending on category
                    switch (category) {

                        case "Fruit":
                            tvItemIcon.setText("🍎");
                            tvCategoryIcon.setText("🍃");
                            break;

                        case "Vegetable":
                            tvItemIcon.setText("🥕");
                            tvCategoryIcon.setText("🌿");
                            break;

                        case "Dairy":
                            tvItemIcon.setText("🥛");
                            tvCategoryIcon.setText("🌱");
                            break;

                        default:
                            tvItemIcon.setText("🥫");
                            tvCategoryIcon.setText("🍂");
                            break;
                    }

                } else {

                    // For old pantry items saved before categories were added
                    tvIngredientName.setText(item);
                    tvCategory.setText("Other");
                    tvItemIcon.setText("🥫");
                    tvCategoryIcon.setText("🍂");
                }

                // DELETE ITEM
                btnDeleteItem.setOnClickListener(v -> {

                    String itemToDelete = displayedItems.get(position);

                    // Remove from the main pantry list
                    pantryItems.remove(itemToDelete);

                    // Remove from what is currently displayed
                    displayedItems.remove(itemToDelete);

                    // Update saved pantry
                    sharedPreferences.edit()
                            .putStringSet(
                                    "pantryItems",
                                    new HashSet<>(pantryItems)
                            )
                            .apply();

                    // Refresh screen
                    notifyDataSetChanged();

                    Toast.makeText(
                            MyPantryActivity.this,
                            "Ingredient removed",
                            Toast.LENGTH_SHORT
                    ).show();
                });

                return convertView;

            }
        };

        listPantry.setAdapter(adapter);

        // ADD INGREDIENT
        btnAddIngredient.setOnClickListener(v -> {

            String ingredient =
                    etIngredient.getText().toString().trim();

            String category =
                    spinnerCategory.getSelectedItem().toString();

            if (ingredient.isEmpty()) {

                Toast.makeText(
                        MyPantryActivity.this,
                        "Please enter an ingredient",
                        Toast.LENGTH_SHORT
                ).show();

            } else if (category.equals("Select Category")) {

                Toast.makeText(
                        MyPantryActivity.this,
                        "Please select a category",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                String pantryItem =
                        ingredient + "|" + category;

                pantryItems.add(pantryItem);

                // Save permanently
                sharedPreferences.edit()
                        .putStringSet(
                                "pantryItems",
                                new HashSet<>(pantryItems)
                        )
                        .apply();

                // Show all items after adding
                showAllItems();

                etIngredient.setText("");
                spinnerCategory.setSelection(0);

                Toast.makeText(
                        MyPantryActivity.this,
                        ingredient + " added to " + category,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // FILTER BUTTONS
        btnAll.setOnClickListener(v ->
                showAllItems()
        );

        btnFruit.setOnClickListener(v ->
                filterItems("Fruit")
        );

        btnVegetable.setOnClickListener(v ->
                filterItems("Vegetable")
        );

        btnDairy.setOnClickListener(v ->
                filterItems("Dairy")
        );

        btnOther.setOnClickListener(v ->
                filterItems("Other")
        );

        // BACK BUTTON
        btnBack.setOnClickListener(v -> finish());
    }

    // Show every pantry item
    private void showAllItems() {

        displayedItems.clear();
        displayedItems.addAll(pantryItems);

        adapter.notifyDataSetChanged();
    }

    // Show only selected category
    private void filterItems(String selectedCategory) {

        displayedItems.clear();

        for (String item : pantryItems) {

            if (item.contains("|")) {

                String[] parts = item.split("\\|");

                if (parts.length >= 2) {

                    String category = parts[1];

                    if (category.equals(selectedCategory)) {
                        displayedItems.add(item);
                    }
                }

            } else if (selectedCategory.equals("Other")) {

                // Old saved items without a category
                displayedItems.add(item);
            }
        }

        adapter.notifyDataSetChanged();
    }
}