package com.mdev.cleverkitchenandroid.model

data class Recipe(
     val recipe_id: Int?,
     val recipe_name: String,
     val ingredients: String,
     val description: String,
     val img_location: String,
     val email_id: String
);
