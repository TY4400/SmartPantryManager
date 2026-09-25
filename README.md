# Smart Pantry Manager

Smart Pantry Manager is an Android application developed in Java using Android Studio. The application helps users manage ingredients in their pantry and discover recipes that they can make using the ingredients they already have available.

The app was developed as part of the Mobile App Development 700 assignment.

## Purpose

The purpose of Smart Pantry Manager is to help users keep track of ingredients they already have at home and reduce unnecessary food waste. By comparing pantry contents with stored recipe requirements, the application can suggest meals that the user is currently able to prepare.

## Features

### Pantry Management
Users can:
- Add pantry ingredients
- Enter ingredient quantity and unit
- Assign ingredients to categories
- Edit existing pantry items
- Delete pantry items
- Search pantry ingredients
- Filter ingredients by category

Pantry information is stored locally and remains available after the application is closed.

### Recipe Database
The application contains 20 preloaded recipes.

Each recipe includes:
- Recipe name
- Category
- Cooking time
- Required ingredients and quantities
- Preparation instructions

Users can search recipes and filter them by Breakfast, Lunch and Dinner.

### Suggested Recipes
The Suggested Recipes feature compares the ingredients required by each recipe with the ingredients currently stored in the user's pantry.

A recipe is only displayed when all required ingredients are available in sufficient quantities.

The matching system also supports compatible unit conversions, including:
- grams (g) and kilograms (kg)
- millilitres (ml) and litres (L)

Basic ingredient name normalisation is used to handle simple singular and plural differences.

If no recipes can currently be made, the application displays a clear message and allows the user to return to the pantry.

### Recipe Details
Selecting a recipe opens a detailed recipe screen showing:
- Recipe name
- Cooking time
- Complete ingredient list
- Preparation instructions

### Home Dashboard
The home screen provides a summary of the user's pantry, including:
- Total number of pantry items
- Number of recipes currently ready to cook
- Quick access to suggested recipes

The dashboard automatically refreshes when the user returns to the home screen.

### Settings
The Settings screen allows the user to save:
- Their name
- Their preferred default recipe category

Settings are stored locally using SharedPreferences.

### Navigation
A consistent bottom navigation bar provides access to:
- Home
- Pantry
- Recipes
- Settings

## Database

Smart Pantry Manager uses the Android Room persistence library with SQLite for local data storage.

Room was selected because it provides structured local database storage while simplifying interaction with SQLite through entities and DAO interfaces. It also allows pantry and recipe information to remain available between application sessions without requiring an internet connection.

The main database components include:
- `AppDatabase`
- `PantryItem`
- `PantryItemDao`
- `Recipe`
- `RecipeDao`

## Technologies Used

- Java
- Android Studio
- Android SDK
- XML layouts
- Room Database (SQLite)
- SharedPreferences
- Git
- GitHub

## Application Screens

The application includes the following main screens:
- Home
- My Pantry
- Recipes
- Suggested Recipes
- Recipe Details
- Settings

## Running the Application

1. Clone or download this repository.
2. Open the project in Android Studio.
3. Allow Gradle to finish synchronising the project.
4. Select an Android emulator or connected Android device.
5. Build and run the application.
6. Smart Pantry Manager will open on the Home screen.

No external server or API configuration is required because the application stores its data locally.

## Testing

The application was tested for:
- Creating pantry items
- Reading and displaying stored pantry items
- Editing pantry items
- Deleting pantry items
- Data persistence
- Recipe searching and filtering
- Strict pantry-to-recipe matching
- Ingredient quantity validation
- Unit conversion during recipe matching
- Zero-match recipe feedback
- Recipe detail navigation
- Settings persistence
- Bottom navigation
- Home dashboard updates

## Version Control

Git and GitHub were used throughout development. The repository contains incremental commits documenting the implementation of the main application features, database functionality, recipe system, suggested recipe matching, user interface improvements and navigation.

## Author

Tyler Arnold

Mobile App Development 700