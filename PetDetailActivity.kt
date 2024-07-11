package com.example.petadoptioncenter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PetDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_detail)

        val petDetailImage: ImageView = findViewById(R.id.petDetailImage)
        val petDetailName: TextView = findViewById(R.id.petDetailName)
        val petDetailDescription: TextView = findViewById(R.id.petDetailDescription)
        val ownerDetailProfilePic: ImageView = findViewById(R.id.ownerDetailProfilePic)
        val ownerDetailName: TextView = findViewById(R.id.ownerDetailName)
        val backToHomeButton: Button = findViewById(R.id.backToHomeButton)
        val viewOnMapButton: Button = findViewById(R.id.viewOnMapButton)

        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val imageUrl = intent.getStringExtra("imageUrl")
        val ownerName = intent.getStringExtra("ownerName")
        val ownerProfilePic = intent.getStringExtra("ownerProfilePic")

        name?.let { petDetailName.text = it }
        description?.let { petDetailDescription.text = it }
        imageUrl?.let { Glide.with(this).load(it).into(petDetailImage) }
        ownerName?.let { ownerDetailName.text = it }
        ownerProfilePic?.let { Glide.with(this).load(it).into(ownerDetailProfilePic) }

        backToHomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        viewOnMapButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

    }
}
