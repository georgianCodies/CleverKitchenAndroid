package com.mdev.cleverkitchenandroid.fragments.addrecipe

import android.app.Activity.RESULT_OK
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mdev.cleverkitchenandroid.FirebaseStorageManager
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import kotlinx.android.synthetic.main.fragment_add_recipe.*
import java.sql.Date
import java.text.SimpleDateFormat


@Suppress("DEPRECATION")
class AddRecipeFragment : Fragment() {

    private lateinit var recipeName: TextView
    private lateinit var ingredients: TextView
    private lateinit var description: TextView

    private lateinit var video: Button
    private var mStorageRef: StorageReference? = null

    private val pickImage = 100
    private var imageUri: Uri? = null

    private lateinit var chipGroup: ChipGroup
    private var storedIngredientsList: String = "";
    private var finalIngredientsList: MutableList<String> = mutableListOf()
    private var CHANNEL_ID = "MY_CHANNEL"
    private var Notification_ID = 1
    //var part_image: String? = null


    var filePath: ValueCallback<Array<Uri>>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)

        val submitButton = view.findViewById<Button>(R.id.submitButton);

        val cancelButton = view.findViewById<Button>(R.id.cancelButton);

        recipeName = view.findViewById<TextView>(R.id.recipeNameEditText);
        ingredients = view.findViewById<TextView>(R.id.ingredientsEditText)
        description = view.findViewById<TextView>(R.id.descriptionEditText)
        video = view.findViewById<Button>(R.id.videoInputButton);
        mStorageRef = FirebaseStorage.getInstance().getReference()

        val quantity = view.findViewById<EditText>(R.id.quantityEditText);
        val unitText = view.findViewById<AutoCompleteTextView>(R.id.quantityUnitText)
        val unitOptions = arrayOf("grams", "kilograms", "liters", "milliliters", "cups", "teaspoons", "tablespoons")
        val unitAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, unitOptions)
        unitText.setAdapter(unitAdapter);
        unitText.threshold = 1;

        val addIngredientsbutton = view.findViewById<Button>(R.id.addIngredientsbutton);


        chipGroup = view.findViewById(R.id.ingredientsChipGroup)
        var isAllFieldsChecked = false;
        var imgURI = Uri.parse("");
        video.setOnClickListener {
            println("upload image button clicked!")
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)

        }

        addIngredientsbutton.setOnClickListener{
            if(ingredients.text.toString().isNotEmpty()){

                val quantityAdd = quantity.text.toString();
                val unitAdd = unitText.text.toString();
                val ingredientsList = ingredients.text.toString()

                    if(quantityAdd.isNullOrEmpty() ){
                        quantity.error = "Please enter quantity!"
                        return@setOnClickListener;
                    }
                    if(unitAdd.isNullOrEmpty() ){
                        unitText.error = "Please enter units!"
                        return@setOnClickListener;
                    }



                val ingredientsArray:List<String> = ingredientsList.split(",")

                val updatedIngredientsArray = mutableListOf<String>()
                for (ingredient in ingredientsArray) {
                    updatedIngredientsArray.add("$ingredient: $quantityAdd $unitAdd")
                }

                Log.d("shoppingList",ingredientsList);
                Log.d("shoppingListArray",ingredientsArray.toString())
                finalIngredientsList+=updatedIngredientsArray;
                addChip(updatedIngredientsArray.filter { item -> item.isNotEmpty() })
                Log.d("final Array",finalIngredientsList.joinToString())
               // database.insertShoppingList(finalIngredientsList.joinToString { it },emailId)
                ingredients.text = ""
                quantity.text = null;
                unitText.text = null;

            }else{
                ingredients.error = "Please enter ingredient!"
            }
        }

        submitButton.setOnClickListener(View.OnClickListener { // store the returned value of the dedicated function which checks
            // whether the entered data is valid or if any fields are left blank.
            isAllFieldsChecked = checkAllFields()

            // the boolean variable turns to be true then
            // only the user must be proceed to the activity2
            if (isAllFieldsChecked) {

                // initialise db
                val databaseClass = CleverKitchenDatabase(requireActivity())
                val sharedPreferences =
                    activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
                val emailId = sharedPreferences?.getString("emailId", "")

                val timestamp = System.currentTimeMillis()
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val dateTime: String = sdf.format(Date(timestamp))

                val imgURI = video.tag as Uri?
                this.imageUri = imgURI;
                if(imgURI == null){
                    Toast.makeText(requireContext(),"Please select image first",Toast.LENGTH_SHORT).show()
                }else{
                    FirebaseStorageManager().uploadImage(requireContext(),"recipe-images",imgURI,emailId.toString()){ imageUri ->
                       Log.d("Add recipe- uploaded",imageUri.toString())
                       //insertion
                       val insertRecipe = databaseClass.insertRecipe(recipeName.text.toString(),
//                           ingredients.text.toString(),
                           finalIngredientsList.joinToString { it },
                           description.text.toString(),
                           imageUri.toString(),
                           emailId, dateTime)
                       println(insertRecipe.toString())
                       Log.d("insert", insertRecipe.toString())
                   }

                }

                showNotification()
                view.findNavController().navigate(R.id.action_addRecipeFragment_to_homeFragment)
            }
        });

        cancelButton.setOnClickListener(View.OnClickListener {
            println("cancel clicked!")
            view.findNavController().navigate(R.id.action_addRecipeFragment_to_homeFragment)
        });// store the returned value of the dedicated function which checks


            return view;
    }


    fun showNotification() {
        val NOTIFICATION_CHANNEL_ID = "my_channel_id_01"
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "My Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "Channel description"
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val builder = NotificationCompat.Builder(requireContext(), NOTIFICATION_CHANNEL_ID)
        val mNotification: Notification = builder
            .setContentTitle("New recipe added")
            .setContentText("New recipe added successfully")
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
        notificationManager.notify( Notification_ID, mNotification)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            println(imageUri);
            Log.d("image uri",imageUri.toString())
            video.setTag(imageUri)
            //imageView.setImageURI(imageUri)
        }
    }


    private fun checkAllFields(): Boolean {
        if (recipeName.length() === 0) {
            recipeName.error = "Recipe Name is required"
            return false
        }
        if (finalIngredientsList.isEmpty()) {
            ingredients.error = "Ingredients is required"
            return false
        }
        if (description.length() === 0) {
            description.error = "Description is required"
            return false
        }
        if(this.imageUri == null){
            video.error = "Please Upload an Image"
            Toast.makeText(requireContext(),"Please Upload an Image!",Toast.LENGTH_SHORT).show()
            return false
        }
        // after all validation return true.
        return true
    }


    private fun addChip(ingredientsListArray: List<String>){
        Log.d("addchip",ingredientsListArray.toString())

        for(item in ingredientsListArray){
            Log.d("element",item)
            val chip = Chip(requireActivity())
            chip.text = item
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener{
                finalIngredientsList.remove(chip.text);
                chipGroup.removeView(chip)
                Log.d("final arrayy",finalIngredientsList.joinToString { it })
            }
            chipGroup.addView(chip)
        }
    }
}