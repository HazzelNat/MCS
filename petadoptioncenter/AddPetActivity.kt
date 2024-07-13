package com.example.petadoptioncenter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class AddPetActivity: AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var addPetNameET: EditText
    private lateinit var addPetDescriptionET: EditText
    private lateinit var petImage: ImageView
    private lateinit var addPetImage: Button
    private lateinit var addOwnerNameET: EditText
    private lateinit var addOwnerImageURLET: EditText
    private lateinit var addButton: Button
    var storageRef = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        db = Firebase.firestore
        storageRef = Firebase.storage.reference

        addPetNameET = findViewById(R.id.petName)
        addPetDescriptionET = findViewById(R.id.petDescription)
        addPetImage = findViewById(R.id.petImage)
        addOwnerNameET = findViewById(R.id.ownerName)
        addOwnerImageURLET = findViewById(R.id.ownerImage)

        petImage = findViewById(R.id.petImage)

        addPetImage.setOnClickListener {
            // PICK INTENT picks item from data
            // and returned selected item
            val galleryIntent = Intent(Intent.ACTION_PICK)
            // here item is type of image
            galleryIntent.type = "image/*"
            // ActivityResultLauncher callback
            imagePickerActivityResult.launch(galleryIntent)
        }

        addButton = findViewById(R.id.addButton)

        addButton.setOnClickListener(View.OnClickListener {
            val addPetName = addPetNameET.text.toString()
            val addPetDescription = addPetDescriptionET.text.toString()
            val addPetImage = addPetImage.text.toString()
            val addOwnerName = addOwnerNameET.text.toString()
            val addOwnerImageURL = addOwnerImageURLET.text.toString()

            addPet(addPetName, addPetDescription, addPetImage, addOwnerName, addOwnerImageURL)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        })

    }

    private fun addPet (name: String, description: String, petImage:String, ownerName:String, ownerImage:String){
        val pet = Pet(name, description, petImage, ownerName, ownerImage)
        db.collection("Pet")
            .add(pet)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }



    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
    // lambda expression to receive a result back, here we
        // receive single item(photo) on selection
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                // getting URI of selected Image
                val imageUri: Uri? = result.data?.data

                // val fileName = imageUri?.pathSegments?.last()

                // extract the file name with extension
                val sd = getFileName(applicationContext, imageUri!!)

                // Upload Task with upload to directory 'file'
                // and name of the file remains same
                val uploadTask = storageRef.child("file/$sd").putFile(imageUri)

                // On success, download the file URL and display it
                uploadTask.addOnSuccessListener {
                    // using glide library to display the image
                    storageRef.child("upload/$sd").downloadUrl.addOnSuccessListener {
                        Glide.with(this@AddPetActivity)
                            .load(it)
                            .into(petImage)

                        Log.e("Firebase", "download passed")
                    }.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }
                }.addOnFailureListener {
                    Log.e("Firebase", "Image Upload fail")
                }
            }
        }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }

}