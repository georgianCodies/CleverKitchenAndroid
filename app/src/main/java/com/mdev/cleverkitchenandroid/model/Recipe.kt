package com.mdev.cleverkitchenandroid.model

data class Recipe(
     var recipe_id: Int?,
     var recipe_name: String,
     var ingredients: String,
     var description: String,
     var img_location: String,
     var email_id: String
){}
