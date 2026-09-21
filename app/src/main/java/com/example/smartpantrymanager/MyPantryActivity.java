package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import java.util.HashSet;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyPantryActivity extends AppCompatActivity {

    private EditText etIngredient;
    private Button btnAddIngredient;
    private Button btnBack;
    private ListView listPantry;

    private ArrayList<String> pantryItems;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pantry);

        etIngredient = findViewById(R.id.etIngredient);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnBack = findViewById(R.id.btnBack);
        listPantry = findViewById(R.id.listPantry);

        sharedPreferences = getSharedPreferences("PantryPrefs", MODE_PRIVATE);

        HashSet<String> savedItems =
                new HashSet<>(sharedPreferences.getStringSet("pantryItems", new HashSet<>()));

        pantryItems = new ArrayList<>(savedItems);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                pantryItems
        );

        listPantry.setAdapter(adapter);

        btnAddIngredient.setOnClickListener(v -> {
            String ingredient = etIngredient.getText().toString().trim();

            if (ingredient.isEmpty()) {
                Toast.makeText(
                        MyPantryActivity.this,
                        "Please enter an ingredient",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                pantryItems.add(ingredient);
                sharedPreferences.edit()
                        .putStringSet("pantryItems", new HashSet<>(pantryItems))
                        .apply();
                adapter.notifyDataSetChanged();
                etIngredient.setText("");
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}