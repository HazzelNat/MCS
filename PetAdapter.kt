package com.example.petadoptioncenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PetAdapter(private val pets: List<Pet>, private val listener: OnItemClickListener) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(pet: Pet)
    }

    inner class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val petImage: ImageView = itemView.findViewById(R.id.petImage)
        val ownerProfilePic: ImageView = itemView.findViewById(R.id.ownerProfilePic)
        val petName: TextView = itemView.findViewById(R.id.petName)
        val ownerName: TextView = itemView.findViewById(R.id.ownerName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(pets[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val currentPet : Pet = pets[position]

        // Bind data to views
        holder.petName.text = currentPet.name
        holder.ownerName.text = currentPet.ownerName

        // Load pet image (Replace with your image loading logic)
        // Example: holder.petImage.setImageResource(R.drawable.placeholder_image)

        // Load owner profile picture (Replace with your image loading logic)
        // Example: holder.ownerProfilePic.setImageResource(R.drawable.placeholder_profile_pic)
    }

    override fun getItemCount() = pets.size
}
