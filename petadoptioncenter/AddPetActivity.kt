package com.example.petadoptioncenter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
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
import com.google.firebase.storage.FirebaseStorage

class AddPetActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var addPetNameET: EditText
    private lateinit var addPetDescriptionET: EditText
    private lateinit var petImage: ImageView
    private lateinit var choosePetImageButton: Button
    private lateinit var addOwnerNameET: EditText
    private lateinit var ownerImage: ImageView
    private lateinit var chooseOwnerImageButton: Button
    private lateinit var addButton: Button
    private var imageUri: Uri? = null
    private val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        db = Firebase.firestore

        addPetNameET = findViewById(R.id.petName)
        addPetDescriptionET = findViewById(R.id.petDescription)
        petImage = findViewById(R.id.petImage)
        choosePetImageButton = findViewById(R.id.chooseImageButton)
        addOwnerNameET = findViewById(R.id.ownerName)
        ownerImage = findViewById(R.id.ownerImage)
        chooseOwnerImageButton = findViewById(R.id.chooseImageButton2)
        addButton = findViewById(R.id.addButton)

        choosePetImageButton.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imagePickerActivityResult.launch(galleryIntent)
        }

        chooseOwnerImageButton.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imagePickerActivityResult.launch(galleryIntent)
        }

        addButton.setOnClickListener {
            val addPetName = addPetNameET.text.toString()
            val addPetDescription = addPetDescriptionET.text.toString()
            val addOwnerName = addOwnerNameET.text.toString()
            val addOwnerImageURL = ownerImage.contentDescription.toString()  // Fixed

            if (imageUri != null) {
                val fileName = getFileName(applicationContext, imageUri!!)
                val uploadTask = storageRef.child("images/$fileName").putFile(imageUri!!)

                uploadTask.addOnSuccessListener {
                    storageRef.child("images/$fileName").downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        addPet(addPetName, addPetDescription, imageUrl, addOwnerName, addOwnerImageURL)
                    }.addOnFailureListener { e ->
                        Log.e("Firebase", "Error getting download URL", e)
                    }
                }.addOnFailureListener { e ->
                    Log.e("Firebase", "Error uploading image", e)
                }
            } else {
                addPet(addPetName, addPetDescription, "", addOwnerName, "")
            }

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addPet(name: String, description: String, petImage: String, ownerName: String, ownerImage: String) {
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

    private val imagePickerActivityResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                imageUri = result.data?.data
                if (imageUri != null) {
                    Glide.with(this).load(imageUri).into(petImage)
                } else {
                    Log.e("AddPetActivity", "Image URI is null")
                }
            }
        }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    return it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        return uri.path?.substringAfterLast('/')
    }
}
