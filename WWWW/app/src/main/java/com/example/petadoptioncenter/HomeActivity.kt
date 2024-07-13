package com.example.petadoptioncenter

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.net.URL

class HomeActivity : AppCompatActivity(), PetAdapter.OnItemClickListener {

    private lateinit var petRecyclerView: RecyclerView
    private lateinit var petAdapter: PetAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var petsArrayList: ArrayList<Pet>
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        petRecyclerView = findViewById(R.id.petRecyclerView)
        petRecyclerView.layoutManager = LinearLayoutManager(this)
        addButton = findViewById(R.id.addButton)
        db = Firebase.firestore

        loadPets()

        addButton.setOnClickListener {
            val intent = Intent(this, AddPetActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(pet: Pet) {
        val intent = Intent(this, PetDetailActivity::class.java).apply {
            putExtra("name", pet.name)
            putExtra("description", pet.description)
            putExtra("imageUrl", pet.imageUrl) // Ensure this is a valid URL
            putExtra("ownerName", pet.ownerName)
            putExtra("ownerProfilePic", pet.ownerProfilePic) // Ensure this is a valid URL
        }
        startActivity(intent)
    }

    private fun loadPets() {
        db.collection("Pet")
            .get()
            .addOnSuccessListener { documents ->
                val pets = ArrayList<Pet>()
                for (document in documents) {
                    val pet = document.toObject(Pet::class.java)
                    pets.add(pet)
                }
                petAdapter = PetAdapter(pets, this)
                petRecyclerView.adapter = petAdapter
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }
    }
}
