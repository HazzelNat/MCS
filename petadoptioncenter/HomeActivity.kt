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

//        petAdapter = PetAdapter(petsArrayList, this)
//        petRecyclerView.adapter = petAdapter

        loadPets()

        addButton.setOnClickListener {
            val intent = Intent(this, AddPetActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(pet: Pet) {
        val intent = Intent(this, PetDetailActivity::class.java)
        startActivity(intent)
    }

//    private fun getDummyPets(): List<Pet> {
//        return listOf(
//            Pet(
//                name = "Bella",
//                description = "A friendly and playful Labrador Retriever.",
//                imageUrl = "https://example.com/images/bella.jpg",
//                ownerName = "John Doe",
//                ownerProfilePic = "https://example.com/images/john.jpg"
//            ),
//            Pet(
//                name = "Max",
//                description = "An energetic and loyal German Shepherd.",
//                imageUrl = "https://example.com/images/max.jpg",
//                ownerName = "Jane Smith",
//                ownerProfilePic = "https://example.com/images/jane.jpg"
//            ),
//            Pet(
//                name = "Luna",
//                description = "A sweet and gentle Golden Retriever.",
//                imageUrl = "https://example.com/images/luna.jpg",
//                ownerName = "Alice Johnson",
//                ownerProfilePic = "https://example.com/images/alice.jpg"
//            ),
//            Pet(
//                name = "Charlie",
//                description = "A curious and adventurous Beagle.",
//                imageUrl = "https://example.com/images/charlie.jpg",
//                ownerName = "Bob Brown",
//                ownerProfilePic = "https://example.com/images/bob.jpg"
//            ),
//            Pet(
//                name = "Lucy",
//                description = "A loving and protective Bulldog.",
//                imageUrl = "https://example.com/images/lucy.jpg",
//                ownerName = "Emma Davis",
//                ownerProfilePic = "https://example.com/images/emma.jpg"
//            )
//        )
//    }

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
