package com.example.petadoptioncenter

import android.content.ClipDescription
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class AddPetActivity: AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var addPetNameET: EditText
    private lateinit var addPetDescriptionET: EditText
    private lateinit var addPetImageURLET: EditText
    private lateinit var addOwnerNameET: EditText
    private lateinit var addOwnerImageURLET: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        db = Firebase.firestore

        addPetNameET = findViewById(R.id.addPetName)
        addPetDescriptionET = findViewById(R.id.addPetDescription)
        addPetImageURLET = findViewById(R.id.addPetImageURL)
        addOwnerNameET = findViewById(R.id.addOwnerName)
        addOwnerImageURLET = findViewById(R.id.addOwnerImageURL)

        addButton = findViewById(R.id.addButton)

        addButton.setOnClickListener(View.OnClickListener { v: View ->
            val addPetName = addPetNameET.text.toString()
            val addPetDescription = addPetDescriptionET.text.toString()
            val addPetImage = addPetImageURLET.text.toString()
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

}