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
    private EditText etQuantity;
    private Spinner spinnerUnit;

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
    private PantryItemDao pantryItemDao;
    private PantryItem itemBeingEdited = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pantry);

        // Connect to Room database
        AppDatabase database = AppDatabase.getDatabase(this);
        pantryItemDao = database.pantryItemDao();

        // Connect Java to XML
        etIngredient = findViewById(R.id.etIngredient);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnBack = findViewById(R.id.btnBack);
        listPantry = findViewById(R.id.listPantry);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        etQuantity = findViewById(R.id.etQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);

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
        // Unit dropdown used when adding an ingredient
        String[] units = {
                "Select Unit",
                "Item",
                "g",
                "kg",
                "ml",
                "L"
        };

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );

        unitAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerUnit.setAdapter(unitAdapter);

        // Migrate old SharedPreferences pantry items to Room
        sharedPreferences =
                getSharedPreferences("PantryPrefs", MODE_PRIVATE);

        if (!sharedPreferences.getBoolean("roomMigrationComplete", false)) {

            HashSet<String> savedItems =
                    new HashSet<>(
                            sharedPreferences.getStringSet(
                                    "pantryItems",
                                    new HashSet<>()
                            )
                    );

            for (String item : savedItems) {

                String ingredient;
                String category;

                if (item.contains("|")) {
                    String[] parts = item.split("\\|");
                    ingredient = parts[0];
                    category = parts.length >= 2 ? parts[1] : "Other";
                } else {
                    ingredient = item;
                    category = "Other";
                }

                pantryItemDao.insert(
                        new PantryItem(ingredient, category, 1.0, "item")
                );
            }

            sharedPreferences.edit()
                    .putBoolean("roomMigrationComplete", true)
                    .apply();
        }

// Load pantry items from Room
        pantryItems = new ArrayList<>();

        for (PantryItem item : pantryItemDao.getAllItems()) {
            pantryItems.add(
                    item.getName() + "|" + item.getCategory()
                            + "|" + item.getQuantity()
                            + "|" + item.getUnit()
            );
        }

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

                TextView btnEditItem =
                        convertView.findViewById(R.id.btnEditItem);

                String item = displayedItems.get(position);

                if (item.contains("|")) {

                    String[] parts = item.split("\\|");

                    String ingredient = parts[0];
                    String category = parts[1];

                    String quantity = "";
                    String unit = "";

                    if (parts.length >= 4) {
                        quantity = parts[2];
                        unit = parts[3];
                    }

                    tvIngredientName.setText(ingredient);

                    if (!quantity.isEmpty() && !unit.isEmpty()) {

                        double quantityValue = Double.parseDouble(quantity);

                        String displayQuantity;

                        if (quantityValue == Math.floor(quantityValue)) {
                            displayQuantity = String.valueOf((int) quantityValue);
                        } else {
                            displayQuantity = String.valueOf(quantityValue);
                        }

                        String displayUnit = unit;

                        if (unit.equals("Item") && quantityValue != 1) {
                            displayUnit = "Items";
                        }

                        tvCategory.setText(
                                category + "  •  " + displayQuantity + " " + displayUnit
                        );

                    } else {
                        tvCategory.setText(category);
                    }

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

                // EDIT ITEM
                btnEditItem.setOnClickListener(v -> {

                    String itemToEdit = displayedItems.get(position);
                    String[] itemParts = itemToEdit.split("\\|");

                    for (PantryItem roomItem : pantryItemDao.getAllItems()) {

                        if (roomItem.getName().equals(itemParts[0])) {

                            itemBeingEdited = roomItem;
                            btnAddIngredient.setText("Update");
                            break;
                        }
                    }
                    String[] editParts = itemToEdit.split("\\|");

                    if (editParts.length >= 2) {

                        // Put the existing ingredient name back into the input
                        etIngredient.setText(editParts[0]);

                        // Select the existing category
                        String editCategory = editParts[1];

                        for (int i = 0; i < spinnerCategory.getCount(); i++) {
                            if (spinnerCategory.getItemAtPosition(i).toString()
                                    .equals(editCategory)) {

                                spinnerCategory.setSelection(i);
                                break;
                            }
                        }

                        // Put existing quantity back into the input
                        if (editParts.length >= 3) {
                            etQuantity.setText(editParts[2]);
                        }

                        // Select the existing unit
                        if (editParts.length >= 4) {

                            String editUnit = editParts[3];

                            for (int i = 0; i < spinnerUnit.getCount(); i++) {
                                if (spinnerUnit.getItemAtPosition(i).toString()
                                        .equals(editUnit)) {

                                    spinnerUnit.setSelection(i);
                                    break;
                                }
                            }
                        }
                    }
                });

                // DELETE ITEM
                btnDeleteItem.setOnClickListener(v -> {

                    String itemToDelete = displayedItems.get(position);

                    // Get ingredient name and category
                    String ingredient;
                    String category;

                    if (itemToDelete.contains("|")) {

                        String[] parts = itemToDelete.split("\\|");

                        ingredient = parts[0];
                        category = parts.length >= 2 ? parts[1] : "Other";

                    } else {

                        ingredient = itemToDelete;
                        category = "Other";
                    }

                    // Find the matching item in Room
                    for (PantryItem roomItem : pantryItemDao.getAllItems()) {

                        if (roomItem.getName().equals(ingredient)
                                && roomItem.getCategory().equals(category)) {

                            pantryItemDao.delete(roomItem);
                            break;
                        }
                    }

                    // Reload pantry items from Room
                    pantryItems.clear();

                    for (PantryItem roomItem : pantryItemDao.getAllItems()) {

                        pantryItems.add(
                                roomItem.getName() + "|" + roomItem.getCategory()
                                        + "|" + roomItem.getQuantity()
                                        + "|" + roomItem.getUnit()
                        );
                    }

                    // Refresh what is displayed
                    showAllItems();

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

            String quantityText = etQuantity.getText().toString().trim();
            String unit = spinnerUnit.getSelectedItem().toString();

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

            } else if (quantityText.isEmpty()) {

                Toast.makeText(
                        MyPantryActivity.this,
                        "Please enter a quantity",
                        Toast.LENGTH_SHORT
                ).show();

            } else if (unit.equals("Select Unit")) {

                Toast.makeText(
                        MyPantryActivity.this,
                        "Please select a unit",
                        Toast.LENGTH_SHORT
                ).show();

            }
            else {

                // Convert quantity text to number
                double quantity;

                try {
                    quantity = Double.parseDouble(quantityText);
                } catch (NumberFormatException e) {
                    Toast.makeText(
                            MyPantryActivity.this,
                            "Please enter a valid quantity",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                if (quantity <= 0) {
                    Toast.makeText(
                            MyPantryActivity.this,
                            "Quantity must be greater than 0",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                boolean wasEditing = itemBeingEdited != null;

                if (itemBeingEdited == null) {

                    // ADD a new pantry item
                    PantryItem newItem = new PantryItem(
                            ingredient,
                            category,
                            quantity,
                            unit
                    );

                    pantryItemDao.insert(newItem);

                } else {

                    // UPDATE the existing pantry item
                    itemBeingEdited.setName(ingredient);
                    itemBeingEdited.setCategory(category);
                    itemBeingEdited.setQuantity(quantity);
                    itemBeingEdited.setUnit(unit);

                    pantryItemDao.update(itemBeingEdited);

                    // Editing is finished
                    itemBeingEdited = null;
                    btnAddIngredient.setText("+ Add");
                }

// Reload pantry items from Room
                pantryItems.clear();

                for (PantryItem item : pantryItemDao.getAllItems()) {
                    pantryItems.add(
                            item.getName() + "|" + item.getCategory()
                                    + "|" + item.getQuantity()
                                    + "|" + item.getUnit()
                    );
                }

// Refresh the screen
                showAllItems();

                etIngredient.setText("");
                spinnerCategory.setSelection(0);
                etQuantity.setText("");
                spinnerUnit.setSelection(0);

                Toast.makeText(
                        MyPantryActivity.this,
                        wasEditing
                                ? ingredient + " updated successfully"
                                : ingredient + " added to " + category,
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